/* (C)2024 */
package com.nbmp.waveform.controller;

import com.nbmp.waveform.model.generation.SynthesisMode;
import com.nbmp.waveform.view.SynthesisViewer;
import com.nbmp.waveform.view.WavesRegister;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControllersState {
  private WavesRegister waveform1;
  private WavesRegister waveform2;
  private SynthesisViewer synthesisViewer;
  private SynthesisMode synthesisMode;
  private SmartObservable<SynthesisMode> synthModeObservable = new SmartObservable<>();
  private SmartObservable<Integer> durationObservable = new SmartObservable<>(1);
  private Runnable resynthesizeTrigger = () -> {};

  public static ControllersState createInstance(WavesRegister waveform1, WavesRegister waveform2) {
    var instance = new ControllersState();
    instance.waveform1 = waveform1;
    instance.waveform2 = waveform2;
    return instance;
  }

  public void changeSynthesisMode(SynthesisMode mode) {
    synthModeObservable.setValue(mode);
  }
}
