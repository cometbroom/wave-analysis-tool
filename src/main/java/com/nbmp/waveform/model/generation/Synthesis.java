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
}
