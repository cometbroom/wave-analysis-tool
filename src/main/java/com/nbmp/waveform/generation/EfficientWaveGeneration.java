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
  private List<SmartGuide> guidesRegister = new LinkedList<>();

  public EfficientWaveGeneration(GraphDashboard graph) {
    totalTime = graph.getTotalTime();
    timeStep = graph.getTimeStep();
    this.graph = graph;
  }

  public List<XYChart.Series<Number, Number>> generate() {
    return generateWithWrappers(guidesRegister);
  }

  public List<XYChart.Series<Number, Number>> generateWithWrappers(List<SmartGuide> guides) {
    for (double t = 0; t < totalTime; t += timeStep) {
      for (var guide : guidesRegister) {
        guide.addPoint(t, timeStep);
      }
    }
    guidesRegister = guidesRegister.stream().filter(SmartGuide::isInteractive).toList();
    return guides.stream().map(SmartGuide::getSeries).toList();
  }

  public EfficientWaveGeneration addGuide(SmartGuide guide) {
    guidesRegister.add(guide);
    return this;
  }

  public static EfficientWaveGeneration generatorOf(GraphDashboard graph, SmartGuide... guides) {
    var gen = new EfficientWaveGeneration(graph);
    for (var guide : guides) {
      guide.setGenerator(gen);
      gen.addGuide(guide);
    }
    return gen;
  }

  public void regenerate(SmartGuide guide) {
    generateWithWrappers(List.of(guide));
  }

  public void regenerate() {
    guidesRegister.forEach(guide -> guide.getSeries().getData().clear());
    generateWithWrappers(guidesRegister);
  }
}
