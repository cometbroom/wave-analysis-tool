/* (C)2024 */
package com.nbmp.waveform.guides;

import com.nbmp.waveform.extras.Reactive;
import com.nbmp.waveform.extras.Sliderable;
import com.nbmp.waveform.models.SliderTarget;

public class SineWaveGuide extends WaveGuide implements Sliderable, Reactive {
  private double changeTime = 0, startingFrequency, targetFrequency;

  public SineWaveGuide(double frequency, GuideOptions... options) {
    super();
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
    regenerateSeries();
  }
}
