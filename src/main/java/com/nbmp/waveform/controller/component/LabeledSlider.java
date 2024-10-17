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
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class LabeledSlider extends VBox {
  @FXML public VBox root;
  @FXML public Label label;
  @FXML public Label valueLabel;
  @FXML public Slider slider;
  public boolean showLabel = true;
  public String orientation = "HORIZONTAL";
  public String text, valueText, unit = "";
  public double value;
  public int min, max, pauseTime = 50, minorTickCount = 0;
  boolean load, showTickLabels = false, showTickMarks = false;
  public double majorTickUnit = 1;
  public Consumer<Double> refreshTask = (newValue) -> {};

  public LabeledSlider() {
    String resourceName = "/components/LabeledSlider.fxml";
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resourceName));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
    log.debug("LabeledSlider from {} bound to controller {}", resourceName, this);
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

  // Will be run when all attributes are loaded
  public void setLoad(boolean load) {
    if (load) {
      if (showLabel) {
        label.setText(text);
        valueLabel.minWidth(50);
      } else {
        label.setVisible(false);
        valueLabel.setVisible(false);
        valueLabel.minWidth(1);
        valueLabel.maxWidth(1);
      }
      slider.setValue(value);
      slider.setMin(min);
      slider.setMax(max);
      slider.setOrientation(
          orientation.equals("HORIZONTAL")
              ? javafx.geometry.Orientation.HORIZONTAL
              : javafx.geometry.Orientation.VERTICAL);
      valueLabel.setText(String.format("%.2f " + unit, value));
      slider.setMajorTickUnit(majorTickUnit);
      slider.setMinorTickCount(minorTickCount);
      slider.setShowTickLabels(showTickLabels);
      slider.setShowTickMarks(showTickMarks);
    }
  }
}
