/* (C)2024 */
package com.nbmp.waveform.generation;

import javafx.scene.chart.XYChart;

public interface WaveGenerator {

  public XYChart.Series<Number, Number> generate();
}
