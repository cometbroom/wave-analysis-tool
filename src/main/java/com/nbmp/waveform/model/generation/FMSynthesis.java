/* (C)2024 */
package com.nbmp.waveform.model.generation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nbmp.waveform.controller.ControllersState;
import com.nbmp.waveform.model.dto.Signal;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class representing frequency modulation synthesis.
 */
@RequiredArgsConstructor
@Setter
@Component("FMSynthesis")
public class FMSynthesis implements Synthesis {
  /** Modulation index. */
  @Autowired private GenerationState state;

  @Autowired private ControllersState controllersState;

  /**
   * Computes the waveform for the given duration.
   *
   * @param duration the duration for which the waveform is to be generated
   * @return a BiTimeSeries representing the generated waveform
   */
  @Override
  public void compute(int duration) {
    var recombinationMode = state.getRecombinationMode();
    double k = state.getModulationIndex();
    int sampleCount = getSampleCount(duration);

    double timeStep = 1.0 / Generator.SAMPLE_RATE;
    var refTime =
        new Object() {
          double t = 0.0;
        };

    var waveform1 = state.getWave1();
    var waveform2 = state.getWave2();

    var signal1 = new Signal(sampleCount);
    var signal2 = new Signal(sampleCount);
    var result = new Signal(sampleCount);

    signal1.addPoint(0.0, waveform1.compute(timeStep));
    signal2.addPoint(0.0, waveform2.compute(timeStep));

    for (int i = 1; i < sampleCount; i++) {
      var wave1Amplitude = waveform1.compute(timeStep);
      signal1.addPoint(refTime.t, wave1Amplitude);
      waveform2.getProps().setDeltaFFmMod(wave1Amplitude * k);
      signal2.addPoint(refTime.t, waveform2.compute(timeStep));
      result.addPoint(
          refTime.t, recombinationMode.apply(signal1.getAmplitude(i), signal2.getAmplitude(i)));
      refTime.t += timeStep;
    }
    resetWaveforms();
    controllersState.getView1().refreshData(signal1.getTimeAmplitude());
    controllersState.getView2().refreshData(signal2.getTimeAmplitude());
    controllersState.getResultView().refreshData(result.getTimeAmplitude());
  }

  /**
   * Resets the waveforms to their initial state.
   */
  private void resetWaveforms() {
    state.getWave1().getProps().resetModulations();
    state.getWave2().getProps().resetModulations();
    state.getWave1().setCumulativePhaseRadians(0);
    state.getWave2().setCumulativePhaseRadians(0);
  }
}
