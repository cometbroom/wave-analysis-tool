/* (C)2024 */
package com.nbmp.waveform.controller;

import java.net.URL;
import java.util.Arrays;
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
import com.nbmp.waveform.controller.component.WaveSliders;
import com.nbmp.waveform.model.generation.SynthesisMode;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WaveController implements Initializable {
  public Label statusLabel;
  public static AtomicReference<Integer> duration = new AtomicReference<>(1);
  @FXML public LabeledComboBox synthesisModeControl;
  @FXML public LabeledTextField durationTextField;
  @FXML public WaveSliders waveSliders;
  @FXML public WaveSliders waveSliders2;

  @Autowired private ControllersState state;

  public enum WaveType {
    SINE,
    SQUARE,
    TRIANGLE,
    SAWTOOTH
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    waveSliders.setupSliders(state.getWaveform1(), (x) -> state.getResynthesizeTrigger().run());
    waveSliders2.setupSliders(state.getWaveform2(), (x) -> state.getResynthesizeTrigger().run());
    setupDurationField();
    setupSynthesisModeChangeCombo();

    state.getResynthesizeTrigger().run();
  }

  public void setupDurationField() {
    durationTextField.setValue(duration.get());
    durationTextField.addListener((value) -> state.getDurationObservable().setValue(value));
  }

  private void setupSynthesisModeChangeCombo() {
    synthesisModeControl.setBoxValues(
        Arrays.stream(SynthesisMode.values()).map(Object::toString).toList());
    synthesisModeControl.addListener(
        (observableValue, s, t1) -> {
          state.changeSynthesisMode(SynthesisMode.valueOf(t1));
        });
  }

  //  private void setupSliders() {
  //    frequencySlider.addListenerForTarget(state.getWaveform1(),
  // WaveLabeledSlider.Target.FREQUENCY);
  //    frequencySlider2.addListenerForTarget(state.getWaveform2(),
  // WaveLabeledSlider.Target.FREQUENCY);
  //    setRefreshTasksForSliders((x) -> state.getResynthesizeTrigger().run());
  //  }
  //
  //  private void setRefreshTasksForSliders(Consumer<Double> refreshTask) {
  //    frequencySlider.setRefreshTask(refreshTask);
  //    frequencySlider2.setRefreshTask(refreshTask);
  //  }
}
