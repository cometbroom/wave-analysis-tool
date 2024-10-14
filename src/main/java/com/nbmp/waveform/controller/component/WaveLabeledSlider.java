/* (C)2024 */
package com.nbmp.waveform.controller.component;

import com.nbmp.waveform.view.WavesRegister;

public class WaveLabeledSlider extends LabeledSlider {

  public WaveLabeledSlider() {
    super();
  }

  public enum Target {
    FREQUENCY,
    AMPLITUDE,
    PHASE;
  }

  public void addListenerForTarget(WavesRegister waveform, Target target) {
    switch (target) {
      case FREQUENCY -> addListener(
          (newValue) -> {
            waveform.getWaveform().getProps().setFrequency(newValue);
          });
      case AMPLITUDE -> addListener(
          (newValue) -> {
            waveform.getWaveform().getProps().setAmplitude(newValue);
          });
      case PHASE -> addListener(
          (newValue) -> {
            waveform.getWaveform().getProps().setInitialPhase(newValue);
          });
    }
  }
}
