/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import com.nbmp.waveform.model.dto.*;
import com.nbmp.waveform.model.waveform.Waveform;

import lombok.Setter;

/**
 * Class representing chaotic waveform synthesis.
 */
@Setter
public class ChaosSynthesis implements Synthesis {
  /** Modulation index, AKA magnitude of the modulation. */
  private static AtomicReference<Double> k = new AtomicReference<>(0.3);

  private final GenerationState state;
  private BiFunction<Double, Double, Double> recombinationMode =
      RecombinationMode.ADD.getFunction();
  private BiConsumer<Waveform, Waveform> modulationFunction;

  /**
   * Constructor for ChaosSynthesis.
   *
   * @param state the generation state
   */
  public ChaosSynthesis(GenerationState state, SynthesisMode mode) {
    this.state = state;
    modFunctionSwitcher(mode);
    //    this.modulationFunction = modulationFunction;
  }

  /**
   * Computes the waveform for the given duration.
   *
   * @param duration the duration for which the waveform is to be generated
   * @return a BiTimeSeries representing the generated waveform
   */
  @Override
  public BiTimeSeries compute(int duration) {
    int sampleCount = getSampleCount(duration);

    double t = 0.0, timeStep = 1.0 / Generator.SAMPLE_RATE;

    Waveform waveform1 = state.getWave1().getWaveform(), waveform2 = state.getWave2().getWaveform();

    state.getReactor().addWave1(waveform1::compute, 0, 1);
    //    state.getReactor().getSectionTasks().put(StreamReactor.Section.START, waveform1::compute);
    //    state.getReactor().getSectionTasks().put(StreamReactor.Section.START, waveform2::compute);

    state.getReactor().addWave2(waveform2::compute, 0, 1);
    modulationFunction.accept(waveform1, waveform2);

    state.getReactor().addWave1(waveform1::compute, 1, getSampleCount(duration));
    state.getReactor().addWave2(waveform2::compute, 1, getSampleCount(duration));
    state
        .getReactor()
        .addResultWave(
            (time) -> recombinationMode.apply(waveform1.compute(time), waveform2.compute(time)),
            0,
            getSampleCount(duration));

    //    state.getResultSeries().refreshData();

    //    var signalProcessor = new TwoPlusOneDSP(signal1, signal2, result);
    //
    //    signalProcessor.applyEffect(HighPassFilters::removeDcOffsetMeanTechnique);
    //    signalProcessor.applyEffect(HighPassFilters::removeDcOffset);
    //    signalProcessor.applyEffect(LowPassFilters::applyButterWorth, 500);
    //    signalProcessor.applyEffect(WaveStatUtils::oneToOneNormalize);

    resetWaveforms();
    return new BiTimeSeries(null, null);
  }

  private void modFunctionSwitcher(SynthesisMode mode) {
    modulationFunction =
        switch (mode) {
          case CHAOS_TWO_WAY_FM -> this::twoWayFM;
          case CHAOS_INDEPENDENT_SELF_MOD_FM -> this::singleSelfFM;
          default -> modulationFunction;
        };
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
        .setPhaseModulation((phi) -> phi + wave1.getCompressedPreviousAmplitude() * k.get());
    wave1
        .getProps()
        .setPhaseModulation((phi) -> phi + wave2.getCompressedPreviousAmplitude() * k.get());
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
        .setPhaseModulation((phi) -> phi + wave2.getCompressedPreviousAmplitude() * k.get());
    wave1
        .getProps()
        .setPhaseModulation((phi) -> phi + wave1.getCompressedPreviousAmplitude() * k.get());
  }

  @Override
  public void setModulationIndex(double index) {
    k.set(index);
  }

  /**
   * Resets the waveforms to their initial state.
   */
  private void resetWaveforms() {
    state.getWave1().getWaveform().getProps().resetModulations();
    state.getWave2().getWaveform().getProps().resetModulations();
    state.getWave1().getWaveform().setCumulativePhaseRadians(0);
    state.getWave2().getWaveform().setCumulativePhaseRadians(0);
  }
}
