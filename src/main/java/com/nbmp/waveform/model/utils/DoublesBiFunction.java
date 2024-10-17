/* (C)2024 */
package com.nbmp.waveform.model.utils;

import java.util.function.BiFunction;

public interface DoublesBiFunction extends BiFunction<Double, Double, Double> {
  double applyAsDouble(double a, double b);

  @Override
  default Double apply(Double a, Double b) {
    return applyAsDouble(a, b);
  }
}
