/* (C)2024 */
package com.nbmp.waveform.controller;

import java.util.function.BiFunction;

import com.nbmp.waveform.model.dto.RecombinationMode;
import com.nbmp.waveform.model.dto.SynthesisMode;
import com.nbmp.waveform.model.dto.TimeSeries;
import com.nbmp.waveform.view.WavesRegister;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControllersState {
  private WavesRegister waveform1;
  private WavesRegister waveform2;
  private TimeSeries resultData = new TimeSeries();
  private SmartObservable<SynthesisMode> synthModeObservable = new SmartObservable<>();
  private SmartObservable<Double> modIndex = new SmartObservable<>(0.0);
  private SmartObservable<BiFunction<Double, Double, Double>> recombinationMode =
      new SmartObservable<>(RecombinationMode.ADD.getFunction());
  private Runnable resynthesizeTrigger = () -> {};

  public static ControllersState createInstance() {
    var instance = new ControllersState();
    instance.waveform1 = WavesRegister.createWaveform("sine1", WaveController.WaveType.SINE, 5, 1);
    instance.waveform2 = WavesRegister.createWaveform("sine2", WaveController.WaveType.SINE, 5, 1);
    return instance;
  }

  public void changeSynthesisMode(SynthesisMode mode) {
    synthModeObservable.setValue(mode);
  }
}
