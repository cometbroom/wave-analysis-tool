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

import com.nbmp.waveform.application.AppConfig;
import com.nbmp.waveform.controller.component.LabeledComboBox;
import com.nbmp.waveform.controller.component.LabeledSlider;
import com.nbmp.waveform.controller.component.LabeledTextField;
import com.nbmp.waveform.controller.component.WaveSliders;
import com.nbmp.waveform.model.dto.RecombinationMode;
import com.nbmp.waveform.model.dto.SynthesisMode;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WaveController implements Initializable {
  public Label statusLabel;
  public static AtomicReference<Integer> duration = new AtomicReference<>(1000);
  @FXML public LabeledComboBox synthesisModeControl;
  @FXML public LabeledTextField durationTextField;
  @FXML public WaveSliders waveSliders;
  @FXML public WaveSliders waveSliders2;
  @FXML public LabeledSlider modIndexSlider;
  @FXML public LabeledComboBox recombinatorControl;

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
    modIndexSlider.addListener((value) -> state.getModIndex().setValue(value));
    setupDurationField();
    setupSynthesisModeChangeCombo();
    setupRecombinatorCombo();

    state.getResynthesizeTrigger().run();
  }

  public void setupDurationField() {
    durationTextField.setValue(duration.get());
    durationTextField.addListener((value) -> AppConfig.duration.setValue(value));
  }

  private void setupSynthesisModeChangeCombo() {
    synthesisModeControl.setBoxValues(
        Arrays.stream(SynthesisMode.values()).map(Object::toString).toList());
    synthesisModeControl.addListener(
        (observableValue, s, t1) -> {
          state.changeSynthesisMode(SynthesisMode.valueOf(t1));
          modIndexSlider.setDisable(SynthesisMode.INDEPENDENT.equals(SynthesisMode.valueOf(t1)));
          switch (SynthesisMode.valueOf(t1)) {
            case INDEPENDENT -> {
              statusLabel.setText("Independent synthesis mode");
              modIndexSlider.setValue(0.0);
            }
            case CHAOS -> {
              statusLabel.setText("Chaos synthesis mode");
              modIndexSlider.setValue(0.3);
            }
            case FM_WAVE1MOD_WAVE2CARRIER -> {
              statusLabel.setText("FM synthesis mode");
              modIndexSlider.setValue(2.0);
            }
          }
        });
  }

  private void setupRecombinatorCombo() {
    recombinatorControl.setBoxValues(RecombinationMode.getNames());
    recombinatorControl.getComboBox().setValue(RecombinationMode.ADD.name());
    recombinatorControl.addListener(
        (observableValue, s, t1) -> {
          switch (RecombinationMode.valueOf(t1)) {
            case ADD -> state.getRecombinationMode().setValue(RecombinationMode.ADD.getFunction());
            case SUBTRACT -> state
                .getRecombinationMode()
                .setValue(RecombinationMode.SUBTRACT.getFunction());
            case MULTIPLY -> state
                .getRecombinationMode()
                .setValue(RecombinationMode.MULTIPLY.getFunction());
            case DIVIDE -> state
                .getRecombinationMode()
                .setValue(RecombinationMode.DIVIDE.getFunction());
          }
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
