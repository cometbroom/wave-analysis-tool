/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.Map;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.google.common.base.MoreObjects;
import com.nbmp.waveform.application.AppConstants;
import com.nbmp.waveform.model.dto.SynthesisMode;
import com.nbmp.waveform.model.generation.synth.BaseSynthesis;
import com.nbmp.waveform.model.pipeline.StreamReactor;
import com.nbmp.waveform.model.waveform.Waveform;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;

/**
 * Class representing the generation and synthesis state for waveforms.
 */
@Service
@Scope("singleton")
public class GenerationState {
  @Autowired private ObjectFactory<StreamReactor> reactor;
  private Map<SynthesisMode, BaseSynthesis> synthesisMap;
  @Delegate private BaseSynthesis synthesis;

  @Autowired
  public GenerationState(Map<SynthesisMode, BaseSynthesis> synthesisMap) {
    this.synthesisMap = synthesisMap;
  }

  @Getter @Setter private Runnable resynthesizeTrigger = () -> {};

  @PostConstruct
  public void init() {
    this.resynthesizeTrigger = () -> regen(AppConstants.duration.getValue());
  }

  public void setDuration(int duration) {
    AppConstants.duration.setValue(duration);
  }

  public void setSynthesis(SynthesisMode mode) {
    synthesis = synthesisMap.get(mode);
  }

  public void setWave1(Waveform wave1) {
    synthesisMap.values().forEach(s -> s.setWave1(wave1));
  }

  public void setWave2(Waveform wave2) {
    synthesisMap.values().forEach(s -> s.setWave2(wave2));
  }

  /**
   * Regenerates the series data for the given duration.
   *
   */
  public void regen() {
    regen(AppConstants.duration.getValue());
  }

  public void regen(int duration) {
    synthesisMap.values().forEach(s -> s.getWave1().reset());
    synthesisMap.values().forEach(s -> s.getWave2().reset());
    reactor.getObject().refresh();
    synthesis.compute(duration);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("synthesis", synthesis.getClass().getCanonicalName())
        .toString();
  }
}
