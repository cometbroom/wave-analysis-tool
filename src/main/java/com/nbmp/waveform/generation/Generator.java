/* (C)2024 */
package com.nbmp.waveform.generation;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import javafx.scene.chart.XYChart;

public interface Generator {

  XYChart.Series<Number, Number> generate();

  String getName();

  XYChart.Series<Number, Number> getSeries();

  Function<Double, Double> getComputeFunction();

  List<Consumer<Double>> getEventRuns();
}
