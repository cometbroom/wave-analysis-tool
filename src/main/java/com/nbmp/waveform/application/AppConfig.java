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
import com.nbmp.waveform.model.generation.GenerationState;

@Configuration
@ComponentScan(basePackages = "com.nbmp.waveform")
public class AppConfig {
  private final ApplicationContext applicationContext;

  public AppConfig(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Bean(name = "mainView")
  public AnchorPane loadMainView() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("/components/Application.fxml"));
    loader.setControllerFactory(applicationContext::getBean);
    return loader.load();
  }

  @Bean
  @Scope("singleton")
  public ControllersState controllersState() {
    return ControllersState.createInstance();
  }

  @Bean
  @Scope("singleton")
  public GenerationState generationState(ControllersState controllersState) {
    return new GenerationState(controllersState);
  }
}
