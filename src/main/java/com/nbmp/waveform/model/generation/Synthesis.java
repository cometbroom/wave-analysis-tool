/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.function.BiFunction;

import com.nbmp.waveform.model.dto.BiTimeSeries;

public interface Synthesis {
  BiTimeSeries compute(int duration);

  void setModulationIndex(double index);

  void setRecombinationMode(BiFunction<Double, Double, Double> mode);
}
