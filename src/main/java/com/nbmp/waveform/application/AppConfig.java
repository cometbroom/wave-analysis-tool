/* (C)2024 */
package com.nbmp.waveform.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = "com.nbmp.waveform")
@Configuration
public class AppConfig {
  public static final int SAMPLE_RATE = 1000;
}
