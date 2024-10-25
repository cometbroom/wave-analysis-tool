/* (C)2024 */
package com.nbmp.waveform.model.generation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nbmp.waveform.application.AppConstants;
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

  /**
   * Computes the waveform for the given duration.
   *
   * @param duration the duration for which the waveform is to be generated
   * @return a BiTimeSeries representing the generated waveform
   */
  @Override
  public void compute(int duration) {
    double k = state.getModulationIndex();
    var waveform1 = state.getWave1();
    var waveform2 = state.getWave2();

    var signal1 = new Signal(AppConstants.getSampleCount(duration));
    var signal2 = new Signal(AppConstants.getSampleCount(duration));
    var result = new Signal(AppConstants.getSampleCount(duration));

    signal1.addPoint(0.0, waveform1.compute(AppConstants.TIME_STEP));
    signal2.addPoint(0.0, waveform2.compute(AppConstants.TIME_STEP));
    var reactor = state.getReactor().getObject();
    reactor.addObserver(
        (i) -> {
          var recombinationMode = state.getRecombinationMode();
          double wave1Amplitude = waveform1.compute(AppConstants.TIME_STEP);
          waveform2.getProps().setDeltaFFmMod(wave1Amplitude * k);
          double wave2Amplitude = waveform2.compute(AppConstants.TIME_STEP);
          double recombination = recombinationMode.apply(wave1Amplitude, wave2Amplitude);

          reactor.addOutputs(i, wave1Amplitude, wave2Amplitude, recombination);
          signal1.addPoint(i, wave1Amplitude);
          signal2.addPoint(i, wave2Amplitude);
          result.addPoint(i, recombination);
        });
    reactor.run(0, AppConstants.getSampleCount());
    resetWaveforms();
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
