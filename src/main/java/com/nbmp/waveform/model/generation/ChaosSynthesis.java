/* (C)2024 */
package com.nbmp.waveform.model.generation;

import com.nbmp.waveform.view.WavesRegister;
import com.nbmp.waveform.model.dto.BiTimeSeries;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChaosSynthesis {
  // Modulation index
  private double k = 0.3;
  private final WavesRegister wave1, wave2;

  public BiTimeSeries compute(int duration) {
    int sampleCount = Generator.SAMPLE_RATE * duration;

    double[][] wave1Gen = new double[sampleCount][2];
    double[][] wave2Gen = new double[sampleCount][2];

    // Generate first point AKA initial conditions
    wave1Gen[0] = wave1.getWaveform().computeTY(0.0);
    wave2Gen[0] = wave2.getWaveform().computeTY(0.0);

    var waveProps1 = wave1.getWaveform().getProps();
    var waveProps2 = wave2.getWaveform().getProps();

    for (int i = 1; i < sampleCount; i++) {
      double t = i / (double) Generator.SAMPLE_RATE;

      // Basic coupling by using last computer value of the other wave
      waveProps2.setPhaseModulation(
          (phi) -> phi + wave1.getWaveform().getNormalizedPreviousAmplitude() * k);
      wave2Gen[i] = wave2.getWaveform().computeTY(t);

      waveProps1.setPhaseModulation(
          (phi) -> phi + wave2.getWaveform().getNormalizedPreviousAmplitude() * k);
      wave1Gen[i] = wave1.getWaveform().computeTY(t);
    }
    wave1.getWaveform().setCumulativePhaseRadians(0);
    wave2.getWaveform().setCumulativePhaseRadians(0);
    return new BiTimeSeries(wave1Gen, wave2Gen);
  }
}
