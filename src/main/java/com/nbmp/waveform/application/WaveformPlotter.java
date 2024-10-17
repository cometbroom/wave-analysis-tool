/* (C)2024 */
package com.nbmp.waveform.application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import atlantafx.base.theme.PrimerLight;

public class WaveformPlotter extends Application {

  private final String STAGE_TITLE = "Waveform Analysis Graph";

  @Autowired private AnchorPane mainView;

  private ApplicationContext springContext;

  @Override
  public void init() throws Exception {
    //    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");

    springContext = new AnnotationConfigApplicationContext(AppConfig.class);
    springContext.getAutowireCapableBeanFactory().autowireBean(this);
  }

  @Override
  public void start(Stage stage) {
    Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    stage.setTitle(STAGE_TITLE);
    stage.setScene(new Scene(mainView));
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
