/* (C)2024 */
package com.nbmp.waveform.generation;

import java.util.LinkedList;
import java.util.List;
import javafx.scene.chart.XYChart;

import com.nbmp.waveform.graph.GraphDashboard;
import com.nbmp.waveform.guides.Guide;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class EfficientWaveGeneration {
  private final double timeStep, totalTime;
  private final GraphDashboard graph;
  private List<Guide> dynamicGuides = new LinkedList<>();
  private List<Guide> allGuides = new LinkedList<>();

  private EfficientWaveGeneration(GraphDashboard graph) {
    this(graph.getTimeStep(), graph.getTotalTime(), graph);
  }

  public List<XYChart.Series<Number, Number>> generate() {
    for (double t = 0; t < totalTime; t += timeStep) {
      for (var guide : dynamicGuides) {
        guide.addPoint(t, timeStep);
      }
    }
    dynamicGuides = dynamicGuides.stream().filter(Guide::isInteractive).toList();
    return allGuides.stream().map(Guide::getSeries).toList();
  }

  public void addGuide(Guide guide) {
    dynamicGuides.add(guide);
    allGuides.add(guide);
  }

  public static EfficientWaveGeneration generatorOf(GraphDashboard graph, Guide... guides) {
    var gen = new EfficientWaveGeneration(graph);
    for (var guide : guides) {
      guide.setGenerator(gen);
      gen.addGuide(guide);
    }
    return gen;
  }

  public void regenerate() {
    dynamicGuides.forEach(guide -> guide.getSeries().getData().clear());
    generate();
  }
}
