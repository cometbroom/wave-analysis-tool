/* (C)2024 */
package com.nbmp.waveform.generation;

import java.util.LinkedList;
import java.util.List;
import javafx.scene.chart.XYChart;

import com.nbmp.waveform.graph.GraphDashboard;
import com.nbmp.waveform.guides.SmartGuide;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class EfficientWaveGeneration {
  private final double timeStep, totalTime;
  private final GraphDashboard graph;
  private List<SmartGuide> dynamicGuides = new LinkedList<>();
  private List<SmartGuide> allGuides = new LinkedList<>();

  public EfficientWaveGeneration(GraphDashboard graph) {
    this(graph.getTimeStep(), graph.getTotalTime(), graph);
  }

  public List<XYChart.Series<Number, Number>> generate() {
    for (double t = 0; t < totalTime; t += timeStep) {
      for (var guide : dynamicGuides) {
        guide.addPoint(t, timeStep);
      }
    }
    dynamicGuides = dynamicGuides.stream().filter(SmartGuide::isInteractive).toList();
    return allGuides.stream().map(SmartGuide::getSeries).toList();
  }

  public void addGuide(SmartGuide guide) {
    dynamicGuides.add(guide);
    allGuides.add(guide);
  }

  public static EfficientWaveGeneration generatorOf(GraphDashboard graph, SmartGuide... guides) {
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