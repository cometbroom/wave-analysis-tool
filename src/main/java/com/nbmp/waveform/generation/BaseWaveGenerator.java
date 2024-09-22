/* (C)2024 */
package com.nbmp.waveform.generation;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import javafx.scene.chart.XYChart;

import com.nbmp.waveform.graph.GraphDashboard;
import com.nbmp.waveform.models.SmartData;

import lombok.Builder;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class BaseWaveGenerator implements Generator {
  @Builder.Default protected double amplitude = 1;
  @Builder.Default protected double frequency = 440;
  @Builder.Default protected double phase = 0;
  @Builder.Default protected String WAVE_LABEL = "Wave";

  @Builder.Default
  protected AtomicReference<Double> maxValue = new AtomicReference<>(Double.NEGATIVE_INFINITY);

  @Builder.Default protected SmartData<Double> peakTime = new SmartData<>(Double.NEGATIVE_INFINITY);
  @Builder.Default private XYChart.Series<Number, Number> series = new XYChart.Series<>();

  public GraphDashboard graph;
  protected Double timeStep;

  public String getName() {
    return WAVE_LABEL;
  }

  @Override
  public XYChart.Series<Number, Number> getSeries() {
    return this.series;
  }

  @Override
  public Function<Double, Double> getComputeFunction() {
    return this::computeWaveValue;
  }

  @Override
  public List<Consumer<Double>> getEventRuns() {
    double actualTimeStep = this.timeStep == null ? graph.getTimeStep() : this.timeStep;
    return List.of(detectFirstPeakTime(this::computeWaveValue, actualTimeStep));
  }

  public XYChart.Series<Number, Number> generate() {
    double timeStepToUse = this.timeStep == null ? this.graph.getTimeStep() : this.timeStep;
    this.series =
        loopAndGenerate(
            this.graph.getTotalTime(),
            timeStepToUse,
            this.WAVE_LABEL,
            this::computeWaveValue,
            detectFirstPeakTime(this::computeWaveValue, timeStepToUse));

    return series;
  }

  protected double computeWaveValue(double t) {
    return 1.0;
  }

  private Consumer<Double> detectFirstPeakTime(
      Function<Double, Double> computeFunction, double timeStep) {
    return (t) -> {
      double nextValue = computeFunction.apply(t + timeStep);
      double currentValue = computeFunction.apply(t);

      if (currentValue > maxValue.get() && currentValue > nextValue) {
        maxValue.set(currentValue);
        peakTime.setValue(t);
      }
    };
  }

  private void loopAndGenerate(
      double totalTime, double timeStep, Consumer<Double> computeFunction) {
    for (double t = 0; t < totalTime; t += timeStep) {
      computeFunction.accept(t);
    }
  }

  private XYChart.Series<Number, Number> loopAndGenerate(
      double totalTime,
      double timeStep,
      String label,
      Function<Double, Double> computeFunction,
      Consumer<Double>... eventRuns) {
    series.setName(label);
    loopAndGenerate(
        totalTime,
        timeStep,
        (t) -> {
          this.series.getData().add(new XYChart.Data<>(t, computeFunction.apply(t)));
          for (Consumer<Double> run : eventRuns) {
            run.accept(t);
          }
        });
    return series;
  }

  private XYChart.Series<Number, Number> loopAndGenerate(
      double totalTime, String label, Function<Double, Double> computeFunction) {
    return loopAndGenerate(totalTime, 0.01, label, computeFunction);
  }

  private XYChart.Series<Number, Number> loopAndGenerate(
      String label, Function<Double, Double> computeFunction) {
    return loopAndGenerate(10, 0.01, label, computeFunction);
  }
}
