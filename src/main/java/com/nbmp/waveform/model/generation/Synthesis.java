/* (C)2024 */
package com.nbmp.waveform.model.generation;

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
  void compute(int duration);

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
