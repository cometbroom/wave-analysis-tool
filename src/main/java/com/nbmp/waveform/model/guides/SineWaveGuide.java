/* (C)2024 */
package com.nbmp.waveform.model.guides;

import com.nbmp.waveform.controller.SliderTarget;
import com.nbmp.waveform.controller.Sliderable;

public class SineWaveGuide extends WaveGuide implements Sliderable {
  private double changeTime = 0, startingFrequency, targetFrequency;

  public SineWaveGuide(double frequency, String name, GuideOptions... options) {
    super(name);
    this.frequency = frequency;
    this.startingFrequency = frequency;
    this.targetFrequency = frequency;
    this.cumulativePhaseRadians = phaseRadians;
    this.setupOptions(options);
  }

  @Override
  public Double compute(Double t, Double timeStep) {
    if (t < changeTime) {
      frequency = startingFrequency + ((targetFrequency - startingFrequency) / changeTime) * t;
    } else {
      frequency = targetFrequency;
    }
    cumulativePhaseRadians += 2 * Math.PI * frequency * timeStep;
    return amplitude * Math.sin(cumulativePhaseRadians);
  }

  public void addFrequencyChange(double targetFrequency, double changeTime) {
    this.targetFrequency = targetFrequency;
    this.changeTime = changeTime;
  }

  @Override
  public void updateValue(SliderTarget target, double value) {
    switch (target) {
      case FREQUENCY -> this.targetFrequency = value;
      case AMPLITUDE -> this.amplitude = value;
      case PHASE -> this.phaseRadians = value;
    }
  }
}
