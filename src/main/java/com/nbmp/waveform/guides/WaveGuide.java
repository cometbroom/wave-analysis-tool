/* (C)2024 */
package com.nbmp.waveform.guides;

import com.nbmp.waveform.generation.EfficientWaveGeneration;
import com.nbmp.waveform.graph.SliderBox;
import com.nbmp.waveform.models.SliderTarget;
import com.nbmp.waveform.models.SmartData;
import com.nbmp.waveform.ui_elements.WaveSlider;
import com.nbmp.waveform.utils.GlobalUtils;
import com.nbmp.waveform.utils.MathConstants;

import javafx.beans.value.ObservableValue;
import javafx.scene.chart.XYChart;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.function.Consumer;

@Getter
@Setter
public class WaveGuide extends SmartGuide {
  protected double amplitude = 1;
  protected double frequency = 440;
  protected double phaseRadians = 0;
  protected double cumulativePhaseRadians = 0;
  protected String WAVE_LABEL = "Wave";
  private Double maxValue = 0.0;
  public SmartData<Double> peakTime =
      new SmartData<>(Double.NEGATIVE_INFINITY, GlobalUtils.makeCountLabel("WaveGuide-peakTime"));


  @Override
  public Double compute(Double t, Double timeStep) {
    double computedValue = computeWaveValue(t);
    cumulativePhaseRadians = MathConstants.angularFrequency.apply(frequency) * t;
    if (computedValue > maxValue && computedValue > computeWaveValue(t + timeStep)) {
      maxValue = computedValue;
      peakTime.setValue(t);
    }
    return computedValue;
  }

@Override
  public Consumer<Double> recompute(SliderTarget target) {
    return switch (target) {
      case FREQUENCY -> this::setFrequency;
      case AMPLITUDE -> this::setAmplitude;
      case PHASE -> this::setPhaseRadians;
    };
  }
  public void setFrequency(double frequency) {
    this.frequency = frequency;
    regenerateSeries();
  }

  public void setAmplitude(double amplitude) {
    this.amplitude = amplitude;
    regenerateSeries();
  }

  public void setPhaseRadians(double phaseRadians) {
    this.phaseRadians = phaseRadians;
    regenerateSeries();
  }

  @Override
  protected Double computeWaveValue(Double t) {
    return 0.0;
  }
}
