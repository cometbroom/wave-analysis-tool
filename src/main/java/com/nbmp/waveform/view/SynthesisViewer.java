/* (C)2024 */
package com.nbmp.waveform.view;

import com.nbmp.waveform.controller.WaveController;
import com.nbmp.waveform.controller.WavePropsSliders;
import com.nbmp.waveform.model.generation.Synthesis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class SynthesisViewer {
  private final WavesRegister wave1, wave2;
  private Synthesis synthesis;

  public SynthesisViewer(
      WavesRegister wave1, WavesRegister wave2, Synthesis synthesis) {
    this.wave1 = wave1;
    this.wave2 = wave2;
    this.synthesis = synthesis;
  }

  public void synthesizeForPair(WavePropsSliders slider1, WavePropsSliders slider2) {
    regenSeriesData(WaveController.duration.get());
    setUpdateTask(slider1);
    setUpdateTask(slider2);
  }

  private void regenSeriesData(int duration) {
    var newData = synthesis.compute(duration);
    wave1.refreshData(newData.timeAmplitude1());
    wave2.refreshData(newData.timeAmplitude2());
  }

  private void setUpdateTask(WavePropsSliders slider) {
    slider.setUpdateTask(
        (newValue) -> {
          var newData = synthesis.compute(WaveController.duration.get());
          wave1.refreshData(newData.timeAmplitude1());
          wave2.refreshData(newData.timeAmplitude2());
        });
  }
}
