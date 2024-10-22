/* (C)2024 */
package com.nbmp.waveform.controller;

import com.nbmp.waveform.model.dto.SynthesisMode;
import com.nbmp.waveform.model.generation.GenerationState;
import com.nbmp.waveform.view.WavesRegister;

import lombok.Getter;
import lombok.Setter;

/**
 * Class representing the state of waveform UI controllers such as {@link WaveController}
 */
@Getter
@Setter
public class ControllersState {
  private GenerationState genState;

  /**
   * Creates an instance of ControllersState with default waveforms. More options will be provided here
   *
   * @return a new ControllersState instance
   */
  public static ControllersState createInstance(WavesRegister wave1, WavesRegister wave2) {
    var instance = new ControllersState();
    instance.genState = new GenerationState(wave1, wave2);
    return instance;
  }

  /**
   * Changes the synthesis mode and updates the observable value.
   *
   * @param mode the new synthesis mode to be set
   */
  public void changeSynthesisMode(SynthesisMode mode) {
    genState.getSynthModeObservable().setValue(mode);
  }
}
