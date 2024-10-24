/* (C)2024 */
package com.nbmp.waveform.application;

import com.nbmp.waveform.controller.SmartObservable;

/**
 * Application constants.
 * It holds static values and observable properties used throughout the application.
 */
public class AppConstants {
  /** The sample rate for waveform generation and analysis. */
  public static final int SAMPLE_RATE = 88200;

  public static final int MAX_DURATION = 10000;

  /** The default duration for waveform generation, as an observable property. */
  public static final SmartObservable<Integer> duration = new SmartObservable<>(1000);

  public static final double TIME_STEP = 1.0 / SAMPLE_RATE;

  public static final int VIEW_RESOLUTION = Math.min(200, SAMPLE_RATE / 2);

  private static final int viewResolutionDuration = VIEW_RESOLUTION * (duration.getValue() / 1000);

  public static final int VIEW_STEP = Math.max(getSampleCount() / viewResolutionDuration, 3);

  public static double getTimeFromIndex(int index) {
    return index * AppConstants.TIME_STEP;
  }

  /**
   * Calculates the sample count based on the given duration in milliseconds.
   *
   * @return the calculated sample count
   */
  public static int getSampleCount() {
    try {
      double sampleCountDouble = SAMPLE_RATE * duration.getValue() / 1000.0;
      return Math.toIntExact(Math.round(sampleCountDouble));
    } catch (ArithmeticException ex) {
      throw new ArithmeticException("Error occurred while getting sample count");
    }
  }
}
