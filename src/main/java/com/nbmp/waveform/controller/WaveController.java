/* (C)2024 */
package com.nbmp.waveform.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nbmp.waveform.controller.component.*;
import com.nbmp.waveform.model.dto.RecombinationMode;
import com.nbmp.waveform.model.dto.SynthesisMode;
import com.nbmp.waveform.model.generation.GenerationState;
import com.nbmp.waveform.model.waveform.SineWaveform;

/**
 * Controller class for landing page of the application. Most waveforms are managed here.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WaveController implements Initializable {
  public Label statusLabel;
  public static final AtomicReference<Integer> duration = new AtomicReference<>(1000);
  @Autowired private GenerationState genState;
  @FXML public LabeledComboBox synthesisModeControl;
  @FXML public LabeledTextField durationTextField;
  @FXML public WaveSliders waveSliders;
  @FXML public WaveSliders waveSliders2;
  @FXML public LabeledSlider modIndexSlider;
  @FXML public LabeledComboBox recombinatorControl;
  @FXML public ButtonedComboBox exportButton;

  @Autowired private ControllersState state;

  /**
   * Enumeration representing different types of waveforms.
   */
  public enum WaveType {
    SINE,
    SQUARE,
    TRIANGLE,
    SAWTOOTH
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    genState.setWave1(new SineWaveform(5, 1));
    genState.setWave2(new SineWaveform(5, 1));
    waveSliders.setupSliders(genState.getWave1(), (x) -> genState.getResynthesizeTrigger().run());
    waveSliders2.setupSliders(genState.getWave2(), (x) -> genState.getResynthesizeTrigger().run());
    modIndexSlider.addListener(
        (value) -> {
          genState.setModulationIndex(value);
          genState.regen();
        });
    setupDurationField();
    setupSynthesisModeChangeCombo();
    setupRecombinatorCombo();
    setupExportButton();
    updateGenWithControllers(genState);
    genState.getResynthesizeTrigger().run();
  }

  private void updateGenWithControllers(GenerationState genState) {
    genState.setSynthesis(SynthesisMode.valueOf(synthesisModeControl.getComboBox().getValue()));
    genState.setRecombinationMode(
        RecombinationMode.valueOf(recombinatorControl.getComboBox().getValue()).getFunction());
    genState.setDuration(durationTextField.getValue());
    genState.setModulationIndex(modIndexSlider.getValue());
  }

  /**
   * Sets up the duration input field and its listeners.
   */
  private void setupDurationField() {
    durationTextField.setValue(duration.get());
    durationTextField.addListener(
        (value) -> {
          genState.setDuration(value);
          genState.regen();
        });
  }

  /**
   * Sets up the synthesis mode change combo box and its listeners.
   */
  private void setupSynthesisModeChangeCombo() {
    synthesisModeControl.setBoxValues(SynthesisMode.getNames());
    synthesisModeControl.addListener(
        (observableValue, s, t1) -> {
          genState.setSynthesis(SynthesisMode.valueOf(t1));
          modIndexSlider.setDisable(SynthesisMode.INDEPENDENT.equals(SynthesisMode.valueOf(t1)));
          statusLabel.setText(SynthesisMode.valueOf(t1).getTitle());
          updateGenWithControllers(genState);
          genState.regen();
        });
  }

  /**
   * Sets up the recombination mode combo box and its listeners.
   */
  private void setupRecombinatorCombo() {
    recombinatorControl.setBoxValues(RecombinationMode.getNames());
    recombinatorControl.getComboBox().setValue(RecombinationMode.ADD.name());
    recombinatorControl.addListener(
        (observableValue, s, t1) -> {
          genState.setRecombinationMode(RecombinationMode.valueOf(t1).getFunction());
          genState.regen();
        });
  }

  /**
   * Sets up the export button.
   */
  private void setupExportButton() {
    exportButton.setBoxValues(List.of("Implemented in a future release"));
    exportButton.getButton().setDisable(true);
  }
}
