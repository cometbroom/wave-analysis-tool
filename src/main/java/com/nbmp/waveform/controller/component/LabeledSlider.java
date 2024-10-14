/* (C)2024 */
package com.nbmp.waveform.controller.component;

import java.io.IOException;
import java.util.function.Consumer;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabeledSlider extends VBox {
  @FXML public VBox root;
  @FXML public Label label;
  @FXML public Label valueLabel;
  @FXML public Slider slider;
  public String text, valueText, unit = "";
  public double value;
  public int min, max, pauseTime = 50;
  boolean load;
  public Consumer<Double> refreshTask = (newValue) -> {};

  public LabeledSlider() {
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getResource("/components/LabeledSlider.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  public void addListener(Consumer<Double> updateTask) {
    PauseTransition pause = new PauseTransition(javafx.util.Duration.millis(pauseTime));

    slider
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              valueLabel.setText(String.format("%.2f " + unit, newValue.doubleValue()));
              pause.setOnFinished(
                  event -> {
                    updateTask.accept(newValue.doubleValue());
                    refreshTask.accept(newValue.doubleValue());
                  });
              pause.playFromStart();
            });
  }

  // All will be run when attributes are loaded
  public void setLoad(boolean load) {
    if (load) {
      label.setText(text);
      slider.setValue(value);
      slider.setMin(min);
      slider.setMax(max);
      valueLabel.setText(String.format("%.2f " + unit, value));
    }
  }
}
