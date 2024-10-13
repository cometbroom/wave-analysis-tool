/* (C)2024 */
package com.nbmp.waveform.view;

import java.util.function.Consumer;

import com.nbmp.waveform.controller.WaveController;
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

  public SynthesisViewer(WavesRegister wave1, WavesRegister wave2, Synthesis synthesis) {
    this.wave1 = wave1;
    this.wave2 = wave2;
    this.synthesis = synthesis;
  }

  public void synthesizeForPair() {
    regenSeriesData(WaveController.duration.get());
  }

  public Consumer<Double> getUpdateTask() {
    return (newValue) -> {
      var newData = synthesis.compute(WaveController.duration.get());
      wave1.refreshData(newData.timeAmplitude1());
      wave2.refreshData(newData.timeAmplitude2());
    };
  }

  private void regenSeriesData(int duration) {
    var newData = synthesis.compute(duration);
    wave1.refreshData(newData.timeAmplitude1());
    wave2.refreshData(newData.timeAmplitude2());
  }
}
