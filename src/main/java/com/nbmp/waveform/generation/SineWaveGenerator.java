/* (C)2024 */
package com.nbmp.waveform.generation;

import javafx.scene.chart.XYChart;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class SineWaveGenerator extends BaseWaveGenerator {

  @Override
  public XYChart.Series<Number, Number> generate() {
    this.WAVE_LABEL = "Sine Wave";

    return super.generate();
  }

  @Override
  protected double computeWaveValue(double t) {
    return amplitude * Math.sin(2 * Math.PI * frequency * t + phase);
  }
}
