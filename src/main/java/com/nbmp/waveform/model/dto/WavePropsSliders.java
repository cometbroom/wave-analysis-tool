/* (C)2024 */
package com.nbmp.waveform.model.dto;

import java.util.function.Consumer;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.util.Duration;
import javafx.util.Pair;

import com.nbmp.waveform.view.WavesRegister;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WavePropsSliders {
  private WavesRegister waveform;
  private Slider slider;
  private Label label;
  private Consumer<Double> updateTask = (newValue) -> {};

  public enum Target {
    FREQUENCY,
    AMPLITUDE,
    PHASE;
  }

  public WavePropsSliders(WavesRegister waveform, Slider slider, Label label) {
    this.waveform = waveform;
    this.slider = slider;
    this.label = label;
  }

  public void addListenerAccordingToTarget(Target target) {
    switch (target) {
      case FREQUENCY -> addListener(
          slider,
          "Frequency",
          label,
          (newValue) -> {
            waveform.getWaveform().getProps().setFrequency(newValue);
            updateTask.accept(newValue);
          });
      case AMPLITUDE -> addListener(
          slider,
          "Amplitude",
          label,
          (newValue) -> {
            waveform.getWaveform().getProps().setAmplitude(newValue);
            updateTask.accept(newValue);
          });
      case PHASE -> addListener(
          slider,
          "Phase",
          label,
          (newValue) -> {
            waveform.getWaveform().getProps().setInitialPhase(newValue);
            updateTask.accept(newValue);
          });
    }
  }

  public void addListener(
      Slider controlSlider,
      String labelText,
      Label labelOfAffectedSlider,
      Consumer<Double> updateFunction) {
    PauseTransition pause = new PauseTransition(Duration.millis(50));
    controlSlider
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              labelOfAffectedSlider.setText(
                  "%s: %.2f Hz".formatted(labelText, newValue.doubleValue()));
              pause.setOnFinished(
                  event -> {
                    updateFunction.accept(newValue.doubleValue());
                  });
              pause.playFromStart();
            });
  }

  private Pair<Slider, Label> fSlider, aSlider, phiSlider;
}
