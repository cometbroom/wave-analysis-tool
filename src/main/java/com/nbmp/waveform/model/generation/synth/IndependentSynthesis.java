/* (C)2024 */
package com.nbmp.waveform.model.generation.synth;

import java.util.function.BiFunction;
import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.nbmp.waveform.application.AppConstants;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class representing independent waveform synthesis.
 */
@RequiredArgsConstructor
@Setter
@Component("IndependentSynthesis")
public class IndependentSynthesis extends BaseSynthesis {
  private BiFunction<Double, Double, Double> recombinationMode;

  @PostConstruct
  public void init() {
    this.recombinationMode = getRecombinationMode();
  }

  /**
   * Computes the waveform for the given duration. No modulation is applied.
   *
   * @param duration the duration for which the waveform is to be generated
   * @return a BiTimeSeries representing the generated waveform
   */
  @Override
  public void compute(int duration) {
    getReactor()
        .getObject()
        .addObserver(
            (i) -> {
              double wave1Amplitude = getWave1().compute(AppConstants.TIME_STEP);
              double wave2Amplitude = getWave2().compute(AppConstants.TIME_STEP);
              double recombination = recombinationMode.apply(wave1Amplitude, wave2Amplitude);
              outStream.addOutputs3Channel(i, wave1Amplitude, wave2Amplitude, recombination);
            });
    getReactor().getObject().run(0, AppConstants.getSampleCount(duration));
  }
}
