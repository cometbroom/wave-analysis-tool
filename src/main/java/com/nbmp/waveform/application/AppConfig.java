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

/**
 * Configuration class for the application context and bean definitions.
 * It provides the necessary beans for the main view and state management.
 */
@Configuration
@ComponentScan(basePackages = "com.nbmp.waveform")
public class AppConfig {
  private final ApplicationContext applicationContext;

  /**
   * Constructor to initialize the AppConfig with the given application context.
   *
   * @param applicationContext the Spring application context
   */
  public AppConfig(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Loads the main view from the FXML file and sets the controller factory.
   *
   * @return the AnchorPane representing the main view
   * @throws IOException if the FXML file cannot be loaded
   */
  @Bean(name = "mainView")
  public AnchorPane loadMainView() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("/components/Application.fxml"));
    loader.setControllerFactory(applicationContext::getBean);
    return loader.load();
  }

  /**
   * Creates a singleton bean for ControllersState.
   *
   * @return a new instance of ControllersState
   */
  @Bean
  @Scope("singleton")
  public ControllersState controllersState() {
    return ControllersState.createInstance();
  }

  /**
   * Creates a singleton bean for GenerationState.
   *
   * @param controllersState the ControllersState instance to be used
   * @return a new instance of GenerationState
   */
  @Bean
  @Scope("singleton")
  public GenerationState generationState(ControllersState controllersState) {
    return new GenerationState(controllersState);
  }
}
