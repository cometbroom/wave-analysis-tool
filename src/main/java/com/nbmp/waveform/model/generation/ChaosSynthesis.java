/* (C)2024 */
package com.nbmp.waveform.model.generation;

import com.nbmp.waveform.model.dto.BiTimeSeries;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChaosSynthesis implements Synthesis {
  // Modulation index
  private double k = 0.3;
  private final GenerationState state;

  @Override
  public BiTimeSeries compute(int duration) {
    double durationDeci = duration / 100.0;
    double sampleCountDouble = Generator.SAMPLE_RATE * durationDeci;
    int sampleCount = Math.toIntExact(Math.round(sampleCountDouble));
    double timeStep = 1.0 / Generator.SAMPLE_RATE;
    double t = 0.0;
    var waveform1 = state.getWave1().getWaveform();
    var waveform2 = state.getWave2().getWaveform();

    double[][] wave1Gen = new double[sampleCount][2];
    double[][] wave2Gen = new double[sampleCount][2];

    // Generate first point AKA initial conditions
    wave1Gen[0] = waveform1.computeTY(t);
    wave2Gen[0] = waveform2.computeTY(t);

    var waveProps1 = waveform1.getProps();
    var waveProps2 = waveform2.getProps();
    // Basic coupling by using last computer value of the other wave
    waveProps2.setPhaseModulation((phi) -> phi + waveform1.getNormalizedPreviousAmplitude() * k);
    waveProps1.setPhaseModulation((phi) -> phi + waveform2.getNormalizedPreviousAmplitude() * k);

    for (int i = 1; i < sampleCount; i++) {
      wave2Gen[i] = waveform2.computeTY(t);
      wave1Gen[i] = waveform1.computeTY(t);
      t += timeStep;
    }
    resetWaveforms();
    return new BiTimeSeries(wave1Gen, wave2Gen);
  }

  private void resetWaveforms() {
    state.getWave1().getWaveform().getProps().resetModulations();
    state.getWave2().getWaveform().getProps().resetModulations();
    state.getWave1().getWaveform().setCumulativePhaseRadians(0);
    state.getWave2().getWaveform().setCumulativePhaseRadians(0);
  }
}
