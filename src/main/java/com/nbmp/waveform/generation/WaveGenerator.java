/* (C)2024 */
package com.nbmp.waveform.generation;

import java.util.function.Consumer;
import java.util.function.Function;
import javafx.scene.chart.XYChart;

public interface WaveGenerator {

  public XYChart.Series<Number, Number> generate();

  default void loopAndGenerate(
      double totalTime, double timeStep, Consumer<Double> computeFunction) {
    for (double t = 0; t < totalTime; t += timeStep) {
      computeFunction.accept(t);
    }
  }

  default XYChart.Series<Number, Number> loopAndGenerate(
      double totalTime,
      double timeStep,
      String label,
      Function<Double, Double> computeFunction,
      Consumer<Double>... eventRuns) {
    XYChart.Series<Number, Number> series = new XYChart.Series<>();
    series.setName(label);
    loopAndGenerate(
        totalTime,
        timeStep,
        (t) -> {
          series.getData().add(new XYChart.Data<>(t, computeFunction.apply(t)));
          for (Consumer<Double> run : eventRuns) {
            run.accept(t);
          }
        });
    return series;
  }

  default XYChart.Series<Number, Number> loopAndGenerate(
      double totalTime, String label, Function<Double, Double> computeFunction) {
    return loopAndGenerate(totalTime, 0.01, label, computeFunction);
  }

  default XYChart.Series<Number, Number> loopAndGenerate(
      String label, Function<Double, Double> computeFunction) {
    return loopAndGenerate(10, 0.01, label, computeFunction);
  }
}
