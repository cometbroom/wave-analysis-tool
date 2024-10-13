/* (C)2024 */
package com.nbmp.waveform.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.nbmp.waveform.controller.WaveController;

@ComponentScan(basePackages = "com.nbmp.waveform")
@Configuration
public class AppConfig {
  public static final int SAMPLE_RATE = 44100;

  @Bean
  @Scope("prototype")
  public WaveController waveController() {
    return new WaveController();
  }
}
