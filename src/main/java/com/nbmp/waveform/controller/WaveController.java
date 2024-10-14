/* (C)2024 */
package com.nbmp.waveform.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nbmp.waveform.controller.component.LabeledComboBox;
import com.nbmp.waveform.controller.component.LabeledTextField;
import com.nbmp.waveform.controller.component.WaveLabeledSlider;
import com.nbmp.waveform.model.generation.SynthesisMode;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WaveController implements Initializable {
  public Label statusLabel;
  public static AtomicReference<Integer> duration = new AtomicReference<>(1);
  @FXML public LabeledComboBox synthesisModeControl;
  @FXML public LabeledTextField durationTextField;
  @FXML public WaveLabeledSlider frequencySlider;
  @FXML public WaveLabeledSlider frequencySlider2;

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
    synthesisModeControl.setBoxValues(Arrays.stream(SynthesisMode.values()).map(Object::toString).toList());
    synthesisModeControl.addListener(
        (observableValue, s, t1) -> {
          state.changeSynthesisMode(SynthesisMode.valueOf(t1));
          setRefreshTasksForSliders(state.getSynthesisViewer().recomputeRunner());
          state.resynthesize();
        });
    state.resynthesize();
  }

  private void setupSliders() {
    frequencySlider.addListenerForTarget(state.getWaveform1(), WaveLabeledSlider.Target.FREQUENCY);
    frequencySlider2.addListenerForTarget(state.getWaveform2(), WaveLabeledSlider.Target.FREQUENCY);
    setRefreshTasksForSliders(state.getSynthesisViewer().recomputeRunner());
  }

  private void setRefreshTasksForSliders(Consumer<Double> refreshTask) {
    frequencySlider.setRefreshTask(refreshTask);
    frequencySlider2.setRefreshTask(refreshTask);
  }
}
