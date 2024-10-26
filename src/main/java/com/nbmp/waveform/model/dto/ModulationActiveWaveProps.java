/* (C)2024 */
package com.nbmp.waveform.model.dto;

import java.util.function.Function;

import com.google.common.base.MoreObjects;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModulationActiveWaveProps extends WaveProps {
  private Function<Double, Double> frequencyModulation, amplitudeModulation, phaseModulation;
  private Function<Double, Double> omega;
  private double deltaFFmMod = 0.0;

  public ModulationActiveWaveProps(double frequency, double amplitude, double initialPhase) {
    super(frequency, amplitude, initialPhase);
    this.frequencyModulation = (f) -> f;
    this.amplitudeModulation = (a) -> a;
    this.phaseModulation = (phi) -> phi;
    this.omega = (f) -> 2 * Math.PI * f;
  }

  public ModulationActiveWaveProps() {
    this(5, 1, 0);
  }

  public double getFrequency() {
    return frequencyModulation.apply(frequency);
  }

  public double getAmplitude() {
    return amplitudeModulation.apply(amplitude);
  }

  public double getInitialPhase() {
    return phaseModulation.apply(initialPhase);
  }

  public void resetModulations() {
    this.frequencyModulation = (f) -> f;
    this.amplitudeModulation = (a) -> a;
    this.phaseModulation = (phi) -> phi;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("deltaFFmMod", deltaFFmMod)
        .add("frequency", frequency)
        .add("amplitude", amplitude)
        .add("initialPhase", initialPhase)
        .toString();
  }
}
