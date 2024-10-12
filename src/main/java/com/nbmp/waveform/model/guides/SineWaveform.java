/* (C)2024 */
package com.nbmp.waveform.model.guides;

import com.nbmp.waveform.model.dto.ModulationActiveWaveProps;

import lombok.Getter;

@Getter
public class SineWaveform extends Waveform {
  public SineWaveform(double frequency, double amplitude) {
    this(frequency, amplitude, 0);
  }

  public SineWaveform(double frequency, double amplitude, double phaseRadians) {
    this.cumulativePhaseRadians = phaseRadians;
    this.props = new ModulationActiveWaveProps(frequency, amplitude, phaseRadians);
  }

  @Override
  public Double compute(Double timeStep) {
    var cumulativePhase = integratePhase(timeStep);
    previousAmplitude = props.getAmplitude() * Math.sin(cumulativePhase);
    return previousAmplitude;
  }

  public double integratePhase(double t) {
    cumulativePhaseRadians += omega() * t + props.getInitialPhase();
    return cumulativePhaseRadians;
  }
}
