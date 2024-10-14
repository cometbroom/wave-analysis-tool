/* (C)2024 */
package com.nbmp.waveform.model.generation;

import javax.annotation.PostConstruct;

import com.nbmp.waveform.controller.ControllersState;
import com.nbmp.waveform.view.WavesRegister;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerationState {
  private final WavesRegister wave1, wave2;
  private Synthesis synthesis;

  public GenerationState(ControllersState controllersState) {
    this.wave1 = controllersState.getWaveform1();
    this.wave2 = controllersState.getWaveform2();
    controllersState.setResynthesizeTrigger(
        () -> regenSeriesData(controllersState.getDurationObservable().getValue()));
    controllersState.getDurationObservable().addObserver(this::regenSeriesData);
    controllersState
        .getSynthModeObservable()
        .addObserver(
            (mode) -> {
              synthesis =
                  switch (mode) {
                    case INDEPENDENT -> new IndependentSynthesis(this);
                    case CHAOS -> new ChaosSynthesis(this);
                  };
              regenSeriesData(controllersState.getDurationObservable().getValue());
            });
  }

  @PostConstruct
  public void init() {
    synthesis = new IndependentSynthesis(this);
  }

  public void regenSeriesData(int duration) {
    var newData = synthesis.compute(duration);
    wave1.refreshData(newData.timeAmplitude1());
    wave2.refreshData(newData.timeAmplitude2());
  }
}
