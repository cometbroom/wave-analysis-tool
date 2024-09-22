/* (C)2024 */
package com.nbmp.waveform.generation;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.chart.XYChart;

import com.nbmp.waveform.guides.Guide;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EfficientWaveGeneration {
  private final double timeStep, totalTime;

  public List<XYChart.Series<Number, Number>> generate(List<Guide> guides) {
    var listSeries = new ArrayList<XYChart.Series<Number, Number>>();

    guides.forEach(guide -> listSeries.add(new XYChart.Series<>()));
    int guidesBound = guides.size();

    // Loop over the time steps and compute the values
    for (double t = 0; t < totalTime; t += timeStep) {
      for (int i = 0; i < guidesBound; i++) {
        Guide guide = guides.get(i);
        listSeries.get(i).getData().add(new XYChart.Data<>(t, guide.compute(t, timeStep)));
      }
    }
    return listSeries;
  }
}
