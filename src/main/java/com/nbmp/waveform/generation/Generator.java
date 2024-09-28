/* (C)2024 */
package com.nbmp.waveform.generation;

public abstract class Generator {
  protected final double timeStep, totalTime;
  public static final double SAMPLE_RATE = 1000.0;

  public Generator(double timeStep, double totalTime) {
    this.timeStep = timeStep;
    this.totalTime = totalTime;
  }

  public Generator() {
    this(1.0/SAMPLE_RATE, 5);
  }
}
