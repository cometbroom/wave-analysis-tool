/* (C)2024 */
package com.nbmp.waveform.application;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.nbmp.waveform.controller.ControllersState;
import com.nbmp.waveform.controller.WaveController;
import com.nbmp.waveform.view.WavesRegister;

@Configuration
@ComponentScan(basePackages = "com.nbmp.waveform")
@ComponentScan(basePackages = "com.nbmp.waveform.controller.component")
public class SpringFXMLLoader {
  private final ApplicationContext applicationContext;

  public SpringFXMLLoader(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Bean(name = "mainView")
  public AnchorPane loadMainView() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("/Application.fxml"));
    loader.setControllerFactory(applicationContext::getBean);
    return loader.load();
  }

  @Bean
  @Scope("singleton")
  public ControllersState controllersState() {
    var sine1 = WavesRegister.createWaveform("sine1", WaveController.WaveType.SINE, 5, 1);
    var sine2 = WavesRegister.createWaveform("sine2", WaveController.WaveType.SINE, 7, 1);
    return ControllersState.createInstance(sine1, sine2);
  }
}
