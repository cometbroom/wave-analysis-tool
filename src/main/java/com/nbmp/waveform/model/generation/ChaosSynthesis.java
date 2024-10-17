/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import com.nbmp.waveform.model.dto.BiTimeSeries;
import com.nbmp.waveform.model.dto.RecombinationMode;
import com.nbmp.waveform.model.dto.Signal;
import com.nbmp.waveform.model.filter.HighPassFilters;
import com.nbmp.waveform.model.filter.LowPassFilters;
import com.nbmp.waveform.model.utils.WaveStatUtils;
import com.nbmp.waveform.model.waveform.Waveform;

import lombok.Setter;

@Setter
public class ChaosSynthesis implements Synthesis {
  // Modulation index AKA Magnitude of the modulation
  private static AtomicReference<Double> k = new AtomicReference<>(0.3);
  private final GenerationState state;
  private BiFunction<Double, Double, Double> recombinationMode =
      RecombinationMode.ADD.getFunction();
  private BiConsumer<Waveform, Waveform> modulationFunction;

  public ChaosSynthesis(GenerationState state) {
    this(state, (wave1, wave2) -> {});
  }

  public ChaosSynthesis(GenerationState state, BiConsumer<Waveform, Waveform> modulationFunction) {
    this.state = state;
    this.modulationFunction = modulationFunction;
  }

  @Override
  public BiTimeSeries compute(int duration) {
    int sampleCount = getSampleCount(duration);

    double t = 0.0, timeStep = 1.0 / Generator.SAMPLE_RATE;

    Waveform waveform1 = state.getWave1().getWaveform(), waveform2 = state.getWave2().getWaveform();

    Signal signal1 = new Signal(), signal2 = new Signal(), result = new Signal();

    signal1.addPoint(0.0, waveform1.compute(timeStep));
    signal2.addPoint(0.0, waveform2.compute(timeStep));
    modulationFunction.accept(waveform1, waveform2);

    for (int i = 1; i < sampleCount; i++) {
      signal1.addPoint(t, waveform1.compute(timeStep));
      signal2.addPoint(t, waveform2.compute(timeStep));
      // (signal1.getAmplitude(i) + signal2.getAmplitude(i)) / 2
      result.addPoint(t, recombinationMode.apply(signal1.getAmplitude(i), signal2.getAmplitude(i)));
      t += timeStep;
    }
    var signalProcessor = new TwoPlusOneDSP(signal1, signal2, result);

    signalProcessor.applyEffect(HighPassFilters::removeDcOffsetMeanTechnique);
    signalProcessor.applyEffect(HighPassFilters::removeDcOffset);
    signalProcessor.applyEffect(LowPassFilters::applyButterWorth, 500);
    signalProcessor.applyEffect(WaveStatUtils::oneToOneNormalize);

    resetWaveforms();
    state.getResultSeries().refreshData(result.getTimeAmplitude());
    return new BiTimeSeries(signal1.getTimeAmplitude(), signal2.getTimeAmplitude());
  }

  public static void twoWayFM(Waveform wave1, Waveform wave2) {
    wave2
        .getProps()
        .setPhaseModulation((phi) -> phi + wave1.getCompressedPreviousAmplitude() * k.get());
    wave1
        .getProps()
        .setPhaseModulation((phi) -> phi + wave2.getCompressedPreviousAmplitude() * k.get());
  }

  public static void singleSelfFM(Waveform wave1, Waveform wave2) {
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
