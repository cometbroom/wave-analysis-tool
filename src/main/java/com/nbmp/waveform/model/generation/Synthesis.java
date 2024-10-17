/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.function.BiFunction;

import com.nbmp.waveform.model.dto.BiTimeSeries;

/**
 * Interface representing the synthesis process for generating waveforms.
 */
public interface Synthesis {

  /**
   * Computes a BiTimeSeries waveform for the given duration.
   *
   * @param duration the duration for which the waveform is to be generated
   * @return a BiTimeSeries representing the generated waveform
   */
  BiTimeSeries compute(int duration);

  /**
   * Sets the modulation index (Magnitude of modulation) for the synthesis.
   *
   * @param index the modulation index to be set
   */
  void setModulationIndex(double index);

  /**
   * Sets the recombination mode for waveform synthesis.
   *
   * @param mode the recombination mode as a BiFunction for combining two values
   */
  void setRecombinationMode(BiFunction<Double, Double, Double> mode);

  /**
   * Calculates the sample count based on the given duration in milliseconds.
   *
   * @param durationInMs the duration in milliseconds
   * @return the calculated sample count
   */
  default int getSampleCount(int durationInMs) {
    try {
      double sampleCountDouble = Generator.SAMPLE_RATE * durationInMs / 1000.0;
      return Math.toIntExact(Math.round(sampleCountDouble));
    } catch (ArithmeticException ex) {
      throw new ArithmeticException("Error occurred while getting sample count");
    }
  }
}
