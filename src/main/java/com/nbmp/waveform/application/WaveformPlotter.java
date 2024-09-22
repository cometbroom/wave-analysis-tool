/* (C)2024 */
package com.nbmp.waveform.application;

import javafx.application.Application;
import javafx.stage.Stage;

import com.nbmp.waveform.generation.PhaseAnalyzerApp;

public class WaveformPlotter extends Application {

  private final String STAGE_TITLE = "Sine Wave";

  @Override
  public void start(Stage stage) {
    stage.setTitle(STAGE_TITLE);

    PhaseAnalyzerApp.analyzePhaseRelationships(stage);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
