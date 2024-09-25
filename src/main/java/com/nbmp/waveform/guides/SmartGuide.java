/* (C)2024 */
package com.nbmp.waveform.guides;

import javafx.scene.chart.XYChart;

import com.nbmp.waveform.generation.EfficientWaveGeneration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SmartGuide implements Guide {

  protected XYChart.Series<Number, Number> series = new XYChart.Series<>();
  protected EfficientWaveGeneration generator;
  protected boolean isInteractive = false;

  @Override
  public Double compute(Double t, Double timeStep) {
    double computedValue = computeWaveValue(t);
    return computedValue;
  }

  protected Double computeWaveValue(Double t) {
    return 0.0;
  }

  public void addPoint(Double t, Double timeStep) {
    series.getData().add(new XYChart.Data<>(t, compute(t, timeStep)));
  }
}
