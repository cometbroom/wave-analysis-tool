/* (C)2024 */
package com.nbmp.waveform.model.utils;

public class RealtimeMethods {
  public static final double clip(double value, double min, double max) {
    return Math.min(Math.max(value, min), max);
  }
}
