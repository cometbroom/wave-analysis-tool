/* (C)2024 */
package com.nbmp.waveform.model.generation;

import javax.annotation.PostConstruct;

import com.nbmp.waveform.application.AppConfig;
import com.nbmp.waveform.controller.ControllersState;
import com.nbmp.waveform.model.dto.TimeSeries;
import com.nbmp.waveform.view.WavesRegister;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerationState {
  private final WavesRegister wave1, wave2;
  private Synthesis synthesis;
  private int[] frameDurations = new int[256];

  // 256 frames of our generated data
  private double[][][] resultData = new double[256][][];
  private TimeSeries resultSeries;

  public GenerationState(ControllersState controllersState) {
    this.wave1 = controllersState.getWaveform1();
    this.wave2 = controllersState.getWaveform2();
    controllersState.setResynthesizeTrigger(() -> regenSeriesData(AppConfig.duration.getValue()));
    resultSeries = controllersState.getResultData();

    setupObservers(controllersState);
  }

  private void setupObservers(ControllersState controllersState) {
    AppConfig.duration.addObserver(this::regenSeriesData);

    controllersState
        .getModIndex()
        .addObserver(
            (index) -> {
              synthesis.setModulationIndex(index);
              regenSeriesData(AppConfig.duration.getValue());
            });

    controllersState
        .getSynthModeObservable()
        .addObserver(
            (mode) -> {
              synthesis =
                  switch (mode) {
                    case INDEPENDENT -> new IndependentSynthesis(this);
                    case CHAOS_TWO_WAY_FM -> new ChaosSynthesis(this, ChaosSynthesis::twoWayFM);
                    case CHAOS_INDEPENDENT_SELF_MOD_FM -> new ChaosSynthesis(
                        this, ChaosSynthesis::singleSelfFM);
                    case FM_WAVE1MOD_WAVE2CARRIER -> new FMSynthesis(this);
                  };
              regenSeriesData(AppConfig.duration.getValue());
            });

    controllersState
        .getRecombinationMode()
        .addObserver(
            (mode) -> {
              synthesis.setRecombinationMode(mode);
              regenSeriesData(AppConfig.duration.getValue());
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
