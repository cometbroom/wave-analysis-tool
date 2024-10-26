/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.function.BiFunction;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.nbmp.waveform.application.AppConstants;
import com.nbmp.waveform.application.GenerationScope;
import com.nbmp.waveform.model.dto.RecombinationMode;
import com.nbmp.waveform.model.dto.SynthesisMode;
import com.nbmp.waveform.model.dto.TimeSeries;
import com.nbmp.waveform.model.pipeline.StreamReactor;
import com.nbmp.waveform.model.waveform.Waveform;

import lombok.Getter;
import lombok.Setter;

/**
 * Class representing the generation and synthesis state for waveforms.
 */
@Getter
@Setter
@Service
@Scope("singleton")
public class GenerationState {
  @Autowired private ObjectFactory<StreamReactor> reactor;
  @Autowired private ApplicationContext context;
  private Waveform wave1, wave2;
  private Synthesis synthesis;
  private TimeSeries resultSeries = new TimeSeries();
  private BiFunction<Double, Double, Double> recombinationMode =
      RecombinationMode.ADD.getFunction();
  private double modulationIndex = 0.0;
  private Runnable resynthesizeTrigger = () -> {};

  @PostConstruct
  public void init() {
    this.resynthesizeTrigger = () -> regen(AppConstants.duration.getValue());
  }

  public void setDuration(int duration) {
    AppConstants.duration.setValue(duration);
  }

  public void setSynthesis(SynthesisMode mode) {
    synthesis = context.getBean(mode.getClassName().getSimpleName(), Synthesis.class);
  }

  /**
   * Regenerates the series data for the given duration.
   *
   */
  public void regen() {
    regen(AppConstants.duration.getValue());
  }

  public void regen(int duration) {
    wave1.reset();
    wave2.reset();
    GenerationScope.refreshScope();
    synthesis.compute(duration);
  }
}
