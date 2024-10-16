/* (C)2024 */
package com.nbmp.waveform.model.waveform;

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
    previousAmplitude = props.getAmplitude() * Math.sin(cumulativePhase + props.getInitialPhase());
    return previousAmplitude;
  }

  /**
   * Taking timestep here as phase function is being integrated (Added over time)
   * @param t timestep
   * @return integrated phase value which contains any frequency changes. Modulation is not integrated here as it's already integrated in the original waveform.
   */
  public double integratePhase(double t) {
    double deltaf = props.getModulatorCompute().apply(t) * props.getModulationIndex();
    currentPhase += props.getOmega().apply(props.getFrequency()) * t;
    currentPhase = currentPhase % (2 * Math.PI);
    return currentPhase + deltaf;
  }
}
