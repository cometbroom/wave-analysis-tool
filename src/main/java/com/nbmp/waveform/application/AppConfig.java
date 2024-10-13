/* (C)2024 */
package com.nbmp.waveform.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.nbmp.waveform.controller.ControllersState;
import com.nbmp.waveform.controller.GraphController;
import com.nbmp.waveform.controller.WaveController;
import com.nbmp.waveform.view.WavesRegister;

@ComponentScan(basePackages = "com.nbmp.waveform")
@Configuration
public class AppConfig {
  public static final int SAMPLE_RATE = 44100;

  @Bean
  @Scope("singleton")
  public ControllersState controllersState() {
    var sine1 = WavesRegister.createWaveform("sine1", WaveController.WaveType.SINE, 5, 1);
    var sine2 = WavesRegister.createWaveform("sine2", WaveController.WaveType.SINE, 7, 1);
    return ControllersState.createInstance(sine1, sine2);
  }

  @Bean
  @Scope("prototype")
  public WaveController waveController() {
    return new WaveController();
  }

  @Bean
  @Scope("prototype")
  public GraphController graphController() {
    return new GraphController();
  }
}
