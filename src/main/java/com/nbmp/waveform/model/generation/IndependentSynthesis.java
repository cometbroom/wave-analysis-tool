/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.function.BiFunction;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nbmp.waveform.application.AppConstants;
import com.nbmp.waveform.controller.ControllersState;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class representing independent waveform synthesis.
 */
@RequiredArgsConstructor
@Setter
@Component("IndependentSynthesis")
public class IndependentSynthesis implements Synthesis {
  @Autowired private GenerationState state;
  @Autowired private ControllersState controllersState;

  private BiFunction<Double, Double, Double> recombinationMode;

  @PostConstruct
  public void init() {
    this.recombinationMode = state.getRecombinationMode();
  }

  /**
   * Computes the waveform for the given duration. No modulation is applied.
   *
   * @param duration the duration for which the waveform is to be generated
   * @return a BiTimeSeries representing the generated waveform
   */
  @Override
  public void compute(int duration) {
    state
        .getPipeline()
        .getObject()
        .addObserver(
            (i) -> {
              var recombinationMode = state.getRecombinationMode();
              double wave1Amplitude = state.getWave1().compute(AppConstants.TIME_STEP);
              double wave2Amplitude = state.getWave2().compute(AppConstants.TIME_STEP);
              double recombination = recombinationMode.apply(wave1Amplitude, wave2Amplitude);
              state
                  .getPipeline()
                  .getObject()
                  .addOutputs(i, wave1Amplitude, wave2Amplitude, recombination);
            });
    state.getPipeline().getObject().run();
  }
}
