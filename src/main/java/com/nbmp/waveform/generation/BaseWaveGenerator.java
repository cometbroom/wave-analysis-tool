/* (C)2024 */
package com.nbmp.waveform.generation;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

import com.nbmp.waveform.utils.GlobalUtils;
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

  @Builder.Default protected SmartData<Double> peakTime = new SmartData<>(Double.NEGATIVE_INFINITY, GlobalUtils.makeCountLabel("peakTime"));
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

}
