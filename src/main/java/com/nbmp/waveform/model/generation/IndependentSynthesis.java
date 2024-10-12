/* (C)2024 */
package com.nbmp.waveform.model.generation;

import com.nbmp.waveform.model.dto.BiTimeSeries;
import com.nbmp.waveform.view.WavesRegister;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IndependentSynthesis implements Synthesis {
  private final WavesRegister wave1, wave2;

  public BiTimeSeries compute(int duration) {
    int sampleCount = Generator.SAMPLE_RATE * duration;
    double timeStep = 1.0 / Generator.SAMPLE_RATE;
    double t = 0;

    double[][] wave1Gen = new double[sampleCount][2];
    double[][] wave2Gen = new double[sampleCount][2];

    for (int i = 0; i < sampleCount; i++) {
      wave2Gen[i] = wave2.getWaveform().computeTY(t);
      wave1Gen[i] = wave1.getWaveform().computeTY(t);

      t += timeStep;
    }
    wave1.getWaveform().setCumulativePhaseRadians(0);
    wave2.getWaveform().setCumulativePhaseRadians(0);
    return new BiTimeSeries(wave1Gen, wave2Gen);
  }
}
