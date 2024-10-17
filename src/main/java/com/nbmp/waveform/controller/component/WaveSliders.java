/* (C)2024 */
package com.nbmp.waveform.controller.component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import com.nbmp.waveform.view.WavesRegister;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * A custom component that combines three (Amplitude, Frequency and Phase) {@link WaveLabeledSlider}s
 * within an HBox layout.
 */
@Getter
@Setter
@Slf4j
public class WaveSliders extends HBox implements Initializable {
  @FXML public HBox root;
  @FXML public WaveLabeledSlider amplitudeSlider;
  @FXML public WaveLabeledSlider frequencySlider;
  @FXML public WaveLabeledSlider phaseSlider;
  @FXML public Label titleLabel;
  public String title;

  public WaveSliders() {
    String resourceName = "/components/WaveSliders.fxml";
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resourceName));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
    log.debug("WaveSliders from {} bound to controller {}", resourceName, this);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  public void setupSliders(WavesRegister waveform, Consumer<Double> refreshTask) {
    amplitudeSlider.addListenerForTarget(waveform, WaveLabeledSlider.Target.AMPLITUDE);
    amplitudeSlider.setRefreshTask(refreshTask);
    frequencySlider.addListenerForTarget(waveform, WaveLabeledSlider.Target.FREQUENCY);
    frequencySlider.setRefreshTask(refreshTask);
    phaseSlider.addListenerForTarget(waveform, WaveLabeledSlider.Target.PHASE);
    phaseSlider.setRefreshTask(refreshTask);
  }

  public void setTitle(String title) {
    this.title = title;
    titleLabel.setText(title);
  }

  public void setLoad(boolean load) {}

  public HBox getRoot() {
    return root;
  }
}
