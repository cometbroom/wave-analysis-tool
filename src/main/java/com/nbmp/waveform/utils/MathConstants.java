/* (C)2024 */
package com.nbmp.waveform.utils;

import java.util.function.Function;

public class MathConstants {
  public static Function<Double, Double> angularFrequency = (f) -> 2 * Math.PI * f;
}
