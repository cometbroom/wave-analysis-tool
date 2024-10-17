/* (C)2024 */
package com.nbmp.waveform.model.generation;

import com.nbmp.waveform.application.AppConstants;

import lombok.Getter;

@Getter
public abstract class Generator {
  public static int SAMPLE_RATE = AppConstants.SAMPLE_RATE;
  public static double timeStep = 1.0 / SAMPLE_RATE;

  public Generator(int sampleRate) {
    Generator.SAMPLE_RATE = sampleRate;
    this.timeStep = 1.0 / sampleRate;
  }

  public Generator() {
    this(1000);
  }
}
