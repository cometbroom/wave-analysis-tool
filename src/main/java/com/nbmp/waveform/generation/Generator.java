/* (C)2024 */
package com.nbmp.waveform.generation;

public abstract class Generator {
  protected final double timeStep, totalTime;

  public Generator(double timeStep, double totalTime) {
    this.timeStep = timeStep;
    this.totalTime = totalTime;
  }

  public Generator() {
    this(0.02, 5);
  }
}
