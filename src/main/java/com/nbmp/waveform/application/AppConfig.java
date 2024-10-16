/* (C)2024 */
package com.nbmp.waveform.application;

import org.springframework.context.annotation.Configuration;

import com.nbmp.waveform.controller.SmartObservable;

@Configuration
public class AppConfig {
  public static final int SAMPLE_RATE = 88200;
  public static SmartObservable<Integer> duration = new SmartObservable<>(1000);
}
