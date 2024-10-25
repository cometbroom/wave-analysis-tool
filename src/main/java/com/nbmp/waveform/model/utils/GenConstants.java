/* (C)2024 */
package com.nbmp.waveform.model.utils;

public class GenConstants {
  public static final int AMPLITUDE = 1;
  public static final int TIME = 0;

  public static int boundValueTo(int value, int min, int max) {
    return Math.max(min, Math.min(value, max));
  }
}
