/* (C)2024 */
package com.nbmp.waveform.model.generation.synth;

import org.springframework.stereotype.Component;

import com.nbmp.waveform.application.AppConstants;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class representing frequency modulation synthesis.
 */
@RequiredArgsConstructor
@Setter
@Component("FMSynthesis")
public class FMSynthesis extends BaseSynthesis {

  /**
   * Computes the waveform for the given duration.
   *
   * @param duration the duration for which the waveform is to be generated
   */
  @Override
  public void compute(int duration) {
    double k = getModulationIndex();
    var waveform1 = getWave1();
    var waveform2 = getWave2();

    var reactor = getReactor().getObject();
    reactor.addObserver(
        (i) -> {
          double wave1Amplitude = waveform1.compute(AppConstants.TIME_STEP);
          waveform2.getProps().setDeltaFFmMod(wave1Amplitude * k);
          double wave2Amplitude = waveform2.compute(AppConstants.TIME_STEP);
          double recombination = recombinationMode.apply(wave1Amplitude, wave2Amplitude);
          outStream.addOutputs3Channel(i, wave1Amplitude, wave2Amplitude, recombination);
        });
    reactor.run(0, AppConstants.getSampleCount());
    resetWaveforms();
  }

  /**
   * Resets the waveforms to their initial
   */
  private void resetWaveforms() {
    getWave1().getProps().resetModulations();
    getWave2().getProps().resetModulations();
    getWave1().setCumulativePhaseRadians(0);
    getWave2().setCumulativePhaseRadians(0);
  }
}
