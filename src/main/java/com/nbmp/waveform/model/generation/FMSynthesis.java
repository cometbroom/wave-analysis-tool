/* (C)2024 */
package com.nbmp.waveform.model.generation;

import com.nbmp.waveform.model.dto.BiTimeSeries;
import com.nbmp.waveform.model.dto.Signal;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FMSynthesis implements Synthesis {
  // Modulation index
  private double k = 2;
  private final GenerationState state;

  @Override
  public BiTimeSeries compute(int duration) {
    int sampleCount = getSampleCount(duration);

    double timeStep = 1.0 / Generator.SAMPLE_RATE;
    var refTime =
        new Object() {
          double t = 0.0;
        };

    var waveform1 = state.getWave1().getWaveform();
    var waveform2 = state.getWave2().getWaveform();

    var signal1 = new Signal();
    var signal2 = new Signal();
    var result = new Signal();

    signal1.addPoint(0.0, waveform1.compute(timeStep));
    signal2.addPoint(0.0, waveform2.compute(timeStep));
    waveform2.getProps().setModulationIndex(k);
    waveform2.getProps().setModulatorCompute(waveform1::compute);

    for (int i = 1; i < sampleCount; i++) {
      signal1.addPoint(refTime.t, waveform1.compute(timeStep));
      signal2.addPoint(refTime.t, waveform2.compute(timeStep));
      result.addPoint(refTime.t, (signal1.getAmplitude(i) + signal2.getAmplitude(i)) / 2);
      refTime.t += timeStep;
    }
    resetWaveforms();
    state.getResultSeries().refreshData(result.getTimeAmplitude());
    return new BiTimeSeries(signal1.getTimeAmplitude(), signal2.getTimeAmplitude());
  }

  @Override
  public void setModulationIndex(double index) {
    this.k = index;
  }

  // Problematic method. fix get sample count 500. Which is not true.
  private int getSampleCount(int durationInMs) {
    double sampleCountDouble = Generator.SAMPLE_RATE * durationInMs / 1000.0;
    return Math.toIntExact(Math.round(sampleCountDouble));
  }

  private void resetWaveforms() {
    state.getWave1().getWaveform().getProps().resetModulations();
    state.getWave2().getWaveform().getProps().resetModulations();
    state.getWave1().getWaveform().setCumulativePhaseRadians(0);
    state.getWave2().getWaveform().setCumulativePhaseRadians(0);
  }
}