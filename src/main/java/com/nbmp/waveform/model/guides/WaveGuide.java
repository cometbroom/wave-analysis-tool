/* (C)2024 */
package com.nbmp.waveform.model.guides;

import com.nbmp.waveform.data.SmartData;
import com.nbmp.waveform.utils.GlobalUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class WaveGuide extends Guide {
  protected double amplitude = 1,
      frequency = 440,
      phaseRadians = 0,
      cumulativePhaseRadians = 0,
      mavValue = 0.0;
  public SmartData<Double> peakTime =
      new SmartData<>(Double.NEGATIVE_INFINITY, GlobalUtils.makeCountLabel("WaveGuide-peakTime"));

  public WaveGuide(String name) {
    super(name);
  }
}
