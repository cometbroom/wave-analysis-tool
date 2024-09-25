/* (C)2024 */
package com.nbmp.waveform.guides;

import com.nbmp.waveform.extras.Reactive;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PhaseDifferenceGuide extends Guide implements Reactive {
  private final WaveGuide wave1, wave2;

  public PhaseDifferenceGuide(WaveGuide wave1, WaveGuide wave2, GuideOptions... options) {
    this(wave1, wave2);
    this.setupOptions(options);
  }

  @Override
  public Double compute(Double t, Double timeStep) {
    double deltaAmplitude = wave2.currentValue - wave1.currentValue;
    return deltaAmplitude / 2;
  }
}
