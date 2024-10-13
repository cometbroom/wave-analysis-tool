/* (C)2024 */
package com.nbmp.waveform.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nbmp.waveform.controller.component.LabeledComboBox;
import com.nbmp.waveform.model.generation.SynthesisMode;

@Component
public class WaveController implements Initializable {
  public Label statusLabel;
  //  @FXML public ComboBox<String> synthesisMode;
  @FXML public TextField durationField;
  public static AtomicReference<Integer> duration = new AtomicReference<>(1);
  @FXML public LabeledComboBox synthesisModeControl;
  @FXML private Slider frequencySlider;
  @FXML private Slider frequencySlider2;
  @FXML private Label sliderLabel;
  @FXML private Label sliderLabel2;
  private WavePropsSliders slider1;
  private WavePropsSliders slider2;

  @Autowired private ControllersState state;

  @FXML
  public void durationEntered(ActionEvent actionEvent) {
    System.out.println("hi");
  }

  public enum WaveType {
    SINE,
    SQUARE,
    TRIANGLE,
    SAWTOOTH
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setupSliders();
    setupDurationField();
    setupSynthesisModeChangeCombo();
    state.resynthesize();
  }

  private void setupSynthesisModeChangeCombo() {
    var synthesisMode = synthesisModeControl.getComboBox();
    synthesisMode
        .getItems()
        .addAll(SynthesisMode.INDEPENDENT.toString(), SynthesisMode.CHAOS.toString());
    synthesisMode.getSelectionModel().select(0);
    synthesisMode
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observableValue, s, t1) -> {
              state.changeSynthesisMode(SynthesisMode.valueOf(t1));
              slider1.setUpdateTask(state.getSynthesisViewer().getUpdateTask());
              slider2.setUpdateTask(state.getSynthesisViewer().getUpdateTask());
              state.resynthesize();
            });
    state.resynthesize();
  }

  private void setupSliders() {
    slider1 = new WavePropsSliders(state.getWaveform1(), frequencySlider, sliderLabel);
    slider2 = new WavePropsSliders(state.getWaveform2(), frequencySlider2, sliderLabel2);
    slider1.addListenerAccordingToTarget(WavePropsSliders.Target.FREQUENCY);
    slider2.addListenerAccordingToTarget(WavePropsSliders.Target.FREQUENCY);
    slider1.setUpdateTask(state.getSynthesisViewer().getUpdateTask());
    slider2.setUpdateTask(state.getSynthesisViewer().getUpdateTask());
  }

  private void setupDurationField() {
    durationField.setText(duration + "");

    durationField.setOnMouseClicked(
        (event) -> {
          durationField.selectAll();
        });

    durationField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (!newValue.matches("\\d*")) {
                durationField.setText(newValue.replaceAll("[^\\d]", ""));
              }
              PauseTransition pause = new PauseTransition(Duration.millis(500));
              pause.setOnFinished(
                  event -> {
                    int newDuration = getDuration();
                    if (newDuration > 5) {
                      durationField.setText("5");
                    }
                    if (newDuration < 1) {
                      durationField.setText("1");
                    }
                    duration.set(getDuration());
                    state.resynthesize();
                  });
              pause.playFromStart();
            });
  }

  public int getDuration() {
    return Integer.parseInt(durationField.getText());
  }
}
