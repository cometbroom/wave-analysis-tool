/* (C)2024 */
package com.nbmp.waveform.generation;

import java.util.function.Consumer;
import java.util.function.Function;
import javafx.scene.chart.XYChart;

import com.nbmp.waveform.graph.GraphDashboard;
import com.nbmp.waveform.models.SmartData;

import lombok.Builder;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class BaseWaveGenerator implements WaveGenerator {
  @Builder.Default protected double amplitude = 1;
  @Builder.Default protected double frequency = 440;
  @Builder.Default protected double phase = 0;
  @Builder.Default protected String WAVE_LABEL = "Wave";
  @Builder.Default protected SmartData<Double> maxValue = new SmartData<>(Double.NEGATIVE_INFINITY);
  protected Double timeStep;
  public GraphDashboard graph;

  public XYChart.Series<Number, Number> generate() {
    double timeStepToUse = this.timeStep == null ? this.graph.getTimeStep() : this.timeStep;
    ;
    var series =
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
      double currentValue = computeFunction.apply(t - timeStep);

      if (currentValue > maxValue.get() && currentValue > nextValue) {
        maxValue.set(currentValue);
      }
    };
  }
}
