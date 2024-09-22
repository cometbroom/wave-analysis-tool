/* (C)2024 */
package com.nbmp.waveform.generation;

import javafx.scene.chart.XYChart;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class SineWaveGenerator extends BaseWaveGenerator {

  @Override
  protected double computeWaveValue(double t) {
    return amplitude * Math.sin(2 * Math.PI * frequency * t + phase);
  }
}
