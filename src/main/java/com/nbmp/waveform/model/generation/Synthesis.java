/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.function.BiFunction;

import com.nbmp.waveform.model.dto.BiTimeSeries;

public interface Synthesis {
  BiTimeSeries compute(int duration);

  void setModulationIndex(double index);

  void setRecombinationMode(BiFunction<Double, Double, Double> mode);

  default int getSampleCount(int durationInMs) {
    try {
      double sampleCountDouble = Generator.SAMPLE_RATE * durationInMs / 1000.0;
      return Math.toIntExact(Math.round(sampleCountDouble));
    } catch (ArithmeticException ex) {
      throw new ArithmeticException("Error occurred while getting sample count");
    }
  }
}
