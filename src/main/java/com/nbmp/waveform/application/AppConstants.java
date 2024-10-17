/* (C)2024 */
package com.nbmp.waveform.application;

import org.springframework.context.annotation.Configuration;

import com.nbmp.waveform.controller.SmartObservable;

/**
 * Application constants.
 * It holds static values and observable properties used throughout the application.
 */
public class AppConstants {
  /** The sample rate for waveform generation and analysis. */
  public static final int SAMPLE_RATE = 88200;

  /** The default duration for waveform generation, as an observable property. */
  public static final SmartObservable<Integer> duration = new SmartObservable<>(1000);
}
