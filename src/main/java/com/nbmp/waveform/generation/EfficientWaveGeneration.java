/* (C)2024 */
package com.nbmp.waveform.generation;

import java.util.LinkedList;
import java.util.List;

import com.nbmp.waveform.graph.GraphDashboard;
import javafx.scene.chart.XYChart;

import com.nbmp.waveform.guides.Guide;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class EfficientWaveGeneration extends Generator {
  protected List<Guide> allGuides = new LinkedList<>();
  private List<Guide> dynamicGuides = new LinkedList<>();

  public EfficientWaveGeneration(double timeStep, double totalTime, Guide... guides) {
    super(timeStep, totalTime);
    bindGuides(guides);
  }

  public EfficientWaveGeneration(Guide... guides) {
    bindGuides(guides);
  }

  public void generate() {
    for (double t = 0; t < totalTime; t += timeStep) {
      for (var guide : dynamicGuides) {
        guide.addPoint(t, timeStep);
      }
    }
    dynamicGuides = dynamicGuides.stream().filter(Guide::isInteractive).toList();
  }

  public void generateAndBindToGraph(GraphDashboard graph) {
    generate();
    graph.addSeries(allGuides.stream().map(Guide::getSeries).toList());
  }

  public void populateSeriesList(Guide guide) {
    dynamicGuides.add(guide);
    allGuides.add(guide);
    guide.bindForRegeneration(this);
  }

  public void regenerate() {
    dynamicGuides.forEach(
        guide -> {
          if (guide.isInteractive()) guide.getSeries().getData().clear();
        });
    generate();
  }

  private void bindGuides(Guide[] guides) {
    for (var guide : guides) {
      populateSeriesList(guide);
    }
  }
}
