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
}
