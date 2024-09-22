/* (C)2024 */
package com.nbmp.waveform.guides;

import lombok.AllArgsConstructor;

public class SineWaveGuide extends WaveGuide {
  public SineWaveGuide(double frequency) {
    super();
    this.frequency = frequency;
  }

  @Override
  protected Double computeWaveValue(Double t) {
    return amplitude * Math.sin(2 * Math.PI * frequency * t + phaseRadians);
  }
}
