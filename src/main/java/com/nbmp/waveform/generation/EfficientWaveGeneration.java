/* (C)2024 */
package com.nbmp.waveform.generation;

import java.util.List;

import com.nbmp.waveform.graph.GraphDashboard;
import com.nbmp.waveform.guides.SmartGuide;
import com.nbmp.waveform.guides.WaveGuide;
import javafx.scene.chart.XYChart;

import com.nbmp.waveform.guides.Guide;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class EfficientWaveGeneration implements Generator {
  private final double timeStep, totalTime;
  private final GraphDashboard graph;

  public EfficientWaveGeneration(GraphDashboard graph) {
    totalTime = graph.getTotalTime();
    timeStep = graph.getTimeStep();
    this.graph = graph;
  }

//  public List<XYChart.Series<Number, Number>> generate(List<Guide> guides) {
//    return generateWithWrappers(guides.stream().map(guide -> new SmartGuide(this)).toList());
//  }

  public List<XYChart.Series<Number, Number>> generateWithWrappers(List<SmartGuide> guides) {
    for (double t = 0; t < totalTime; t += timeStep) {
      for (var guide : guides) {
        guide.addPoint(t, timeStep);
      }
    }
    return guides.stream().map(SmartGuide::getSeries).toList();
  }

  public void regenerate(SmartGuide guide) {
    generateWithWrappers(List.of(guide));
  }
}
