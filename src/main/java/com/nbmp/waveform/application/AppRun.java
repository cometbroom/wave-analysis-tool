/* (C)2024 */
package com.nbmp.waveform.application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import atlantafx.base.theme.PrimerLight;

/**
 * The entry point of the application.
 * This class extends the JavaFX Application class and is responsible for initializing and starting the main UI.
 */
@Component
public class AppRun extends Application {
  @Autowired private AnchorPane mainView;

  /**
   * Initializes the application by setting up the Spring application context and autowiring beans.
   */
  @Override
  public void init() {
    ApplicationContext springContext = new AnnotationConfigApplicationContext(AppConfig.class);
    springContext.getAutowireCapableBeanFactory().autowireBean(this);
  }

  /**
   * Starts the JavaFX stage and displays the main view.
   *
   * @param stage the primary stage for this application
   */
  @Override
  public void start(Stage stage) {
    String STAGE_TITLE = "Waveform Analysis Graph";

    Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    stage.setTitle(STAGE_TITLE);
    stage.setScene(new Scene(mainView));
    stage.show();
  }

  /**
   * The main method that launches the JavaFX application.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}
