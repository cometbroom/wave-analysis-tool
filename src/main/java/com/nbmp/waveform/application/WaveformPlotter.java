/* (C)2024 */
package com.nbmp.waveform.application;

import java.io.IOException;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class WaveformPlotter extends Application {

  private final String STAGE_TITLE = "Waveform Analysis Graph";

  @Override
  public void start(Stage stage) {
    stage.setTitle(STAGE_TITLE);
    try {
      Parent root =
          FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/waveView.fxml")));

      stage.setScene(new Scene(root));
      stage.show();
    } catch (IOException e) {
      throw new RuntimeException("Failed to load fxml class", e);
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
