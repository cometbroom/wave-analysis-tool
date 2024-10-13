/* (C)2024 */
package com.nbmp.waveform.application;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class WaveformPlotter extends Application {

  private final String STAGE_TITLE = "Waveform Analysis Graph";
  private AnnotationConfigApplicationContext applicationContext;

  @Override
  public void init() throws Exception {
    super.init();
    applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
  }

  @Override
  public void start(Stage stage) {
    stage.setTitle(STAGE_TITLE);
    try {
      var fxmlLoader =
          new SpringFXMLLoader(
              this.getClass().getResource("/Application.fxml"), applicationContext);
      Parent root = fxmlLoader.load();
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
