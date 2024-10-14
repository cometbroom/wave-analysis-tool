/* (C)2024 */
package com.nbmp.waveform.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nbmp.waveform.controller.component.LabeledComboBox;
import com.nbmp.waveform.controller.component.LabeledTextField;
import com.nbmp.waveform.model.generation.SynthesisMode;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WaveController implements Initializable {
  public Label statusLabel;
  //  @FXML public ComboBox<String> synthesisMode;
  @FXML public TextField durationField;
  public static AtomicReference<Integer> duration = new AtomicReference<>(1);
  @FXML public LabeledComboBox synthesisModeControl;
  @FXML public LabeledTextField durationTextField;
  @FXML public Slider frequencySlider;
  @FXML public Slider frequencySlider2;
  @FXML public Label sliderLabel;
  @FXML public Label sliderLabel2;
  private WavePropsSliders slider1;
  private WavePropsSliders slider2;

  @Autowired private ControllersState state;

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
    setupDurationField();

    state.resynthesize();
  }

  public void setupDurationField() {
    durationTextField.setValue(duration.get());
    durationTextField.addListener(
        (value) -> {
          duration.set(value);
          state.resynthesize();
        });
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
}
