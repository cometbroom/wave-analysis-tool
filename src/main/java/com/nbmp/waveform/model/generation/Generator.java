/* (C)2024 */
package com.nbmp.waveform.model.generation;

import lombok.Getter;

@Getter
public abstract class Generator {
  protected final double timeStep;
  public static double SAMPLE_RATE;

  public Generator(double sampleRate) {
    Generator.SAMPLE_RATE = sampleRate;
    this.timeStep = 1.0 / sampleRate;
  }

  public Generator() {
    this(1000.0);
  }
}