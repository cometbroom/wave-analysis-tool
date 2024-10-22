/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.function.BiFunction;
import javafx.scene.chart.XYChart;

import com.nbmp.waveform.application.AppConstants;
import com.nbmp.waveform.controller.ControllersState;
import com.nbmp.waveform.controller.SmartObservable;
import com.nbmp.waveform.controller.WaveController;
import com.nbmp.waveform.model.dto.RecombinationMode;
import com.nbmp.waveform.model.dto.SynthesisMode;
import com.nbmp.waveform.model.dto.TimeSeries;
import com.nbmp.waveform.view.WavesRegister;

import lombok.Getter;
import lombok.Setter;

/**
 * Class representing the generation and synthesis state for waveforms.
 */
@Getter
@Setter
public class GenerationState {
  public static final int VIEW_RESOLUTION = Math.min(500, Generator.SAMPLE_RATE / 2);

  private WavesRegister wave1, wave2;
  private Synthesis synthesis;
  private TimeSeries resultSeries = new TimeSeries();
  private StreamReactor reactor;
  private SmartObservable<SynthesisMode> synthModeObservable =
      new SmartObservable<>(SynthesisMode.INDEPENDENT);
  private SmartObservable<Double> modIndex = new SmartObservable<>(0.0);
  private SmartObservable<BiFunction<Double, Double, Double>> recombinationMode =
      new SmartObservable<>(RecombinationMode.ADD.getFunction());
  private Runnable resynthesizeTrigger = () -> {};

  /**
   * Constructor for GenerationState. As opposed to {@link ControllersState} this holds state for Signal Generation data.
   *
   */
  public GenerationState(WavesRegister wave1, WavesRegister wave2) {
    resynthesizeTrigger = () -> regenSeriesData(AppConstants.duration.getValue());
    this.wave1 = wave1;
    this.wave2 = wave2;
    this.reactor = new StreamReactor();
    setupObservers();
    synthModeObservable.fireEvents();
  }

  /**
   * Sets up observers to listen for changes in the controllers state. Such as changed Synthesizer mode on the ui
   *
   */
  private void setupObservers() {
    AppConstants.duration.addObserver(this::regenSeriesData);

    modIndex.addObserver(
        (index) -> {
          synthesis.setModulationIndex(index);
          regenSeriesData(AppConstants.duration.getValue());
        });

    synthModeObservable.addObserver(
        (mode) -> {
          synthesis =
              switch (mode) {
                case INDEPENDENT -> new IndependentSynthesis(this);
                case CHAOS_TWO_WAY_FM -> new ChaosSynthesis(this, mode);
                case CHAOS_INDEPENDENT_SELF_MOD_FM -> new ChaosSynthesis(this, mode);
                case FM_WAVE1MOD_WAVE2CARRIER -> new FMSynthesis(this);
              };
          regenSeriesData(AppConstants.duration.getValue());
        });

    recombinationMode.addObserver(
        (mode) -> {
          synthesis.setRecombinationMode(mode);
          regenSeriesData(AppConstants.duration.getValue());
        });
  }

  public void refreshData(WavesRegister target) {
    var viewResolutionDuration = VIEW_RESOLUTION * (WaveController.duration.get() / 1000);
    int step = Math.max(viewResolutionDuration, 1);

    target.getSeries().getData().clear();
    reactor
        .getStream()
        .buffer(step)
        .subscribe(
            buffer -> {
              var lastBuffer = buffer.getFirst();
              target
                  .getSeries()
                  .getData()
                  .add(
                      new XYChart.Data<>(
                          lastBuffer.time(),
                          target.getName().contains("1")
                              ? lastBuffer.wave1()
                              : lastBuffer.wave2()));
            });
  }

  /**
   * Regenerates the series data for the given duration.
   *
   * @param duration the duration for which the series data should be regenerated
   */
  public void regenSeriesData(int duration) {
    var newData = synthesis.compute(duration);
    refreshData(wave1);
    refreshData(wave2);
    reactor = new StreamReactor();
    //    wave1.refreshData(newData.timeAmplitude1());
    //    wave2.refreshData(newData.timeAmplitude2());
  }
}
