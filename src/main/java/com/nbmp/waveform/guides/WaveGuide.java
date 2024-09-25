/* (C)2024 */
package com.nbmp.waveform.guides;

import com.nbmp.waveform.models.SmartData;
import com.nbmp.waveform.utils.GlobalUtils;
import com.nbmp.waveform.utils.MathConstants;

import lombok.Getter;
import lombok.Setter;

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
  protected Double computeWaveValue(Double t) {
    return 0.0;
  }
}
