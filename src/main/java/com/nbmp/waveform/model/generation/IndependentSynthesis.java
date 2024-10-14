/* (C)2024 */
package com.nbmp.waveform.model.generation;

import com.nbmp.waveform.model.dto.BiTimeSeries;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IndependentSynthesis implements Synthesis {
  private final GenerationState state;

  public BiTimeSeries compute(int duration) {
    int sampleCount = Generator.SAMPLE_RATE * duration;
    double timeStep = 1.0 / Generator.SAMPLE_RATE;
    double t = 0;

    var waveform1 = state.getWave1().getWaveform();
    var waveform2 = state.getWave2().getWaveform();

    double[][] wave1Gen = new double[sampleCount][2];
    double[][] wave2Gen = new double[sampleCount][2];

    for (int i = 0; i < sampleCount; i++) {
      wave2Gen[i] = waveform2.computeTY(t);
      wave1Gen[i] = waveform1.computeTY(t);

      t += timeStep;
    }
    waveform1.setCumulativePhaseRadians(0);
    waveform2.setCumulativePhaseRadians(0);
    return new BiTimeSeries(wave1Gen, wave2Gen);
  }
}
