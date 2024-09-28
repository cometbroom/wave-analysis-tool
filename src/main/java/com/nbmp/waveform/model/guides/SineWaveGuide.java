/* (C)2024 */
package com.nbmp.waveform.model.guides;

import com.nbmp.waveform.controller.Sliderable;

public class SineWaveGuide extends WaveGuide {
  public SineWaveGuide(double frequency, double amplitude) {
    this(frequency, amplitude, 0);
  }

  public SineWaveGuide(double frequency, double amplitude, double phaseRadians) {
    this.frequency = frequency;
    this.cumulativePhaseRadians = phaseRadians;
    this.amplitude = amplitude;
  }

  @Override
  public Double compute(Double t, Double timeStep) {
    cumulativePhaseRadians += 2 * Math.PI * frequency * timeStep;
    currentValue = amplitude * Math.sin(cumulativePhaseRadians);
    return currentValue;
  }


//  @Override
//  public void updateValue(SliderTarget target, double value) {
//    switch (target) {
//      case FREQUENCY -> this.targetFrequency = value;
//      case AMPLITUDE -> this.amplitude = value;
//      case PHASE -> this.phaseRadians = value;
//    }

}
