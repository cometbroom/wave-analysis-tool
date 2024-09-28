/* (C)2024 */
package com.nbmp.waveform.model.guides;

public class PhaseDifferenceGuide extends WaveGuide {
  private final WaveGuide wave1, wave2;

  public PhaseDifferenceGuide(
      String name, WaveGuide wave1, WaveGuide wave2, GuideOptions... options) {
    this.wave1 = wave1;
    this.wave2 = wave2;
  }

  @Override
  public Double compute(Double t, Double timeStep) {
    double deltaAmplitude = wave2.currentValue - wave1.currentValue;
    return deltaAmplitude / 2;
  }
}
