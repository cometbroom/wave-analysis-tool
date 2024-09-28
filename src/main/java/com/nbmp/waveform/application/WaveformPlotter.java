/* (C)2024 */
package com.nbmp.waveform.application;

import javafx.application.Application;
import javafx.stage.Stage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class WaveformPlotter extends Application {

  private final String STAGE_TITLE = "Waveform Analysis Graph";

  @Override
  public void start(Stage stage) {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    PhaseAnalyzerApp phaseAnalyzerApp = context.getBean(PhaseAnalyzerApp.class);
    stage.setTitle(STAGE_TITLE);

    PhaseAnalyzerApp.analyzePhaseRelationships(stage, 2, 5);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
