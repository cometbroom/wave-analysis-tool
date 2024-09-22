/* (C)2024 */
package com.nbmp.waveform.models;

import java.util.List;
import java.util.function.Consumer;
import javafx.scene.chart.XYChart;

import com.nbmp.waveform.generation.Generator;

import lombok.Builder;

@Builder
public class GeneratorRunProps {
  public Generator generator;
  public List<Consumer<Double>> eventRuns;
  @Builder.Default public XYChart.Series<Number, Number> series = new XYChart.Series<>();
}
