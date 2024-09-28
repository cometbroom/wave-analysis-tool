/* (C)2024 */
package com.nbmp.waveform.model.generation;

public abstract class Generator {
  protected final double timeStep, totalTime;
  public static double SAMPLE_RATE;

  public Generator(double totalTime, double sampleRate) {
    this.totalTime = totalTime;
    Generator.SAMPLE_RATE = sampleRate;
    this.timeStep = 1.0 / sampleRate;
  }

  public Generator() {
    this(5, 1000.0);
  }
}