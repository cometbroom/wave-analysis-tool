/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.function.BiFunction;

import com.nbmp.waveform.model.dto.BiTimeSeries;
import com.nbmp.waveform.model.dto.RecombinationMode;
import com.nbmp.waveform.model.dto.Signal;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class representing frequency modulation synthesis.
 */
@RequiredArgsConstructor
@Setter
public class FMSynthesis implements Synthesis {
  /** Modulation index. */
  private double k = 2;

  private final GenerationState state;
  private BiFunction<Double, Double, Double> recombinationMode =
      RecombinationMode.ADD.getFunction();

  /**
   * Computes the waveform for the given duration.
   *
   * @param duration the duration for which the waveform is to be generated
   * @return a BiTimeSeries representing the generated waveform
   */
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

    var signal1 = new Signal(sampleCount);
    var signal2 = new Signal(sampleCount);
    var result = new Signal(sampleCount);

    signal1.addPoint(0.0, waveform1.compute(timeStep));
    signal2.addPoint(0.0, waveform2.compute(timeStep));
    waveform2.getProps().setModulationIndex(k);
    waveform2.getProps().setModulatorCompute(waveform1::compute);

    for (int i = 1; i < sampleCount; i++) {
      signal1.addPoint(refTime.t, waveform1.compute(timeStep));
      signal2.addPoint(refTime.t, waveform2.compute(timeStep));
      result.addPoint(
          refTime.t, recombinationMode.apply(signal1.getAmplitude(i), signal2.getAmplitude(i)));
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
