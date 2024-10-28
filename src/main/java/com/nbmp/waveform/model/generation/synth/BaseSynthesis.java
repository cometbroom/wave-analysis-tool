/* (C)2024 */
package com.nbmp.waveform.model.generation.synth;

import java.util.function.BiFunction;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.nbmp.waveform.model.dto.RecombinationMode;
import com.nbmp.waveform.model.generation.output.BufferedOutputStream;
import com.nbmp.waveform.model.pipeline.StreamReactor;
import com.nbmp.waveform.model.waveform.Waveform;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseSynthesis implements Synthesis {
  @Autowired protected ObjectFactory<StreamReactor> reactor;
  @Autowired protected BufferedOutputStream outStream;
  protected Waveform wave1, wave2;
  protected BiFunction<Double, Double, Double> recombinationMode =
      RecombinationMode.ADD.getFunction();
  protected double modulationIndex = 0.0;

  public abstract void compute(int duration);

  public void refresh() {
    outStream.flushBuffers();
  }
}
