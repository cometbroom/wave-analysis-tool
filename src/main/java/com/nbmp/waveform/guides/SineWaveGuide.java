/* (C)2024 */
package com.nbmp.waveform.guides;

import com.nbmp.waveform.extras.Sliderable;
import com.nbmp.waveform.models.SliderTarget;

public class SineWaveGuide extends WaveGuide implements Sliderable {
  private double changeTime = 0, startingFrequency, targetFrequency, cumulativePhaseRadians;

  public SineWaveGuide(double frequency) {
    super();
    this.frequency = frequency;
    this.startingFrequency = frequency;
    this.targetFrequency = frequency;
    this.cumulativePhaseRadians = phaseRadians;
  }

  @Override
  public Double compute(Double t, Double timeStep) {
    if (t < changeTime) {
      this.frequency = startingFrequency + ((targetFrequency - startingFrequency) / changeTime) * t;
    } else {
      this.frequency = targetFrequency;
    }
    cumulativePhaseRadians += 2 * Math.PI * frequency * timeStep;
    return computeWaveValue();
  }

  public void addFrequencyChange(double targetFrequency, double changeTime) {
    this.targetFrequency = targetFrequency;
    this.changeTime = changeTime;
  }

  public void updateValue(SliderTarget target, double value) {
    switch (target) {
      case FREQUENCY -> this.targetFrequency = value;
      case AMPLITUDE -> this.amplitude = value;
      case PHASE -> this.phaseRadians = value;
    }
    this.series.getData().clear();
    this.generator.regenerate();
  }

  protected Double computeWaveValue() {
    return amplitude * Math.sin(cumulativePhaseRadians);
  }
}
