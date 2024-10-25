/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.function.BiConsumer;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nbmp.waveform.application.AppConstants;
import com.nbmp.waveform.model.dto.Signal;
import com.nbmp.waveform.model.filter.HighPassFilters;
import com.nbmp.waveform.model.filter.LowPassFilters;
import com.nbmp.waveform.model.utils.GenConstants;
import com.nbmp.waveform.model.utils.WaveStatUtils;
import com.nbmp.waveform.model.waveform.Waveform;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Class representing chaotic waveform synthesis.
 */
@Slf4j
@Component("ChaosSynthesis")
@Setter
public class ChaosSynthesis implements Synthesis {
  /** Modulation index, AKA magnitude of the modulation. */
  @Autowired private GenerationState state;

  protected BiConsumer<Waveform, Waveform> modulationFunction;

  @PostConstruct
  public void init() {
    this.modulationFunction = this::twoWayFM;
  }

  /**
   * Computes the waveform for the given duration.
   *
   * @param duration the duration for which the waveform is to be generated
   * @return a BiTimeSeries representing the generated waveform
   */
  @Override
  public void compute(int duration) {
    Signal signal1 = new Signal(AppConstants.getSampleCount(duration)),
        signal2 = new Signal(AppConstants.getSampleCount(duration)),
        result = new Signal(AppConstants.getSampleCount(duration));

    var reactor = state.getReactor().getObject();

    reactor.addObserver(
        (i) -> {
          var recombinationMode = state.getRecombinationMode();
          double wave1Amplitude = state.getWave1().compute(AppConstants.TIME_STEP);
          double wave2Amplitude = state.getWave2().compute(AppConstants.TIME_STEP);
          double recombination = recombinationMode.apply(wave1Amplitude, wave2Amplitude);
          reactor.addOutputs(i, wave1Amplitude, wave2Amplitude, recombination);
          signal1.addPoint(i, wave1Amplitude);
          signal2.addPoint(i, wave2Amplitude);
          result.addPoint(i, recombination);
        });
    reactor.runFor(GenConstants.boundValueTo(1, 0, AppConstants.getSampleCount(duration)));
    modulationFunction.accept(state.getWave1(), state.getWave2());
    reactor.resume(AppConstants.getSampleCount(duration));

    var signalProcessor = new TwoPlusOneDSP(signal1, signal2, result);
    signalProcessor.applyEffect(HighPassFilters::removeDcOffsetMeanTechnique);
    signalProcessor.applyEffect(HighPassFilters::removeDcOffset);
    signalProcessor.applyEffect(LowPassFilters::applyButterWorth, 500);
    signalProcessor.applyEffect(WaveStatUtils::oneToOneNormalize);
    reactor.getClockStream().getObservers().clear();
    reactor.addObserver(
        (i) -> {
          reactor.addOutputs(
              i, signal1.getAmplitude(i), signal2.getAmplitude(i), result.getAmplitude(i));
        });
    reactor.run(0, AppConstants.getSampleCount(duration));
    resetWaveforms();
  }

  /**
   * Applies a two-way frequency modulation between two waveforms.
   *
   * @param wave1 the first waveform
   * @param wave2 the second waveform
   */
  public void twoWayFM(Waveform wave1, Waveform wave2) {
    wave2
        .getProps()
        .setPhaseModulation(
            (phi) -> phi + wave1.getCompressedPreviousAmplitude() * state.getModulationIndex());
    wave1
        .getProps()
        .setPhaseModulation(
            (phi) -> phi + wave2.getCompressedPreviousAmplitude() * state.getModulationIndex());
  }

  /**
   * Applies a single self-frequency modulation to both waveforms.
   *
   * @param wave1 the first waveform
   * @param wave2 the second waveform
   */
  public void singleSelfFM(Waveform wave1, Waveform wave2) {
    wave2
        .getProps()
        .setPhaseModulation(
            (phi) -> phi + wave2.getCompressedPreviousAmplitude() * state.getModulationIndex());
    wave1
        .getProps()
        .setPhaseModulation(
            (phi) -> phi + wave1.getCompressedPreviousAmplitude() * state.getModulationIndex());
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
