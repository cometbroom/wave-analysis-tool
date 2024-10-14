/* (C)2024 */
package com.nbmp.waveform.controller;

import com.nbmp.waveform.controller.component.LabeledSlider;
import com.nbmp.waveform.view.WavesRegister;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WavePropsSliders {
  public enum Target {
    FREQUENCY,
    AMPLITUDE,
    PHASE;
  }

  public static void addListenerForTarget(
      LabeledSlider slider, WavesRegister waveform, Target target) {
    switch (target) {
      case FREQUENCY -> slider.addListener(
          (newValue) -> {
            waveform.getWaveform().getProps().setFrequency(newValue);
          });
      case AMPLITUDE -> slider.addListener(
          (newValue) -> {
            waveform.getWaveform().getProps().setAmplitude(newValue);
          });
      case PHASE -> slider.addListener(
          (newValue) -> {
            waveform.getWaveform().getProps().setInitialPhase(newValue);
          });
    }
  }
}
