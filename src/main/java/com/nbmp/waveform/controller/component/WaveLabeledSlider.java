/* (C)2024 */
package com.nbmp.waveform.controller.component;

import com.nbmp.waveform.view.WavesRegister;

/**
 * A custom extension of LabeledSlider that provides additional functionality for waveform properties.
 * This component is designed specifically to adjust frequency, amplitude, and phase properties of a waveform.
 */
public class WaveLabeledSlider extends LabeledSlider {

  /**
   * Constructor that initializes the WaveLabeledSlider component.
   */
  public WaveLabeledSlider() {
    super();
  }

  /**
   * Enumeration representing the target property of the waveform that the slider controls.
   */
  public enum Target {
    FREQUENCY,
    AMPLITUDE,
    PHASE
  }

  /**
   * Adds a listener for a specific target property of the given waveform.
   *
   * @param waveform the waveform whose property is to be adjusted
   * @param target the target property to be controlled by the slider
   */
  public void addListenerForTarget(WavesRegister waveform, Target target) {
    switch (target) {
      case FREQUENCY -> addListener(
          (newValue) -> waveform.getWaveform().getProps().setFrequency(newValue));
        // Bound amplitude to 0/1
      case AMPLITUDE -> addListener(
          (newValue) -> waveform.getWaveform().getProps().setAmplitude(newValue / max));
        // Bound phase to 0/PI. TODO: Add negative phase support
      case PHASE -> addListener(
          (newValue) ->
              waveform.getWaveform().getProps().setInitialPhase((newValue / max) * Math.PI));
    }
  }
}
