/* (C)2024 */
package com.nbmp.waveform.model.dto;

import java.util.function.Function;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModulationActiveWaveProps extends WaveProps {
  private Function<Double, Double> frequencyModulation, amplitudeModulation, phaseModulation;

  public ModulationActiveWaveProps(double frequency, double amplitude, double initialPhase) {
    super(frequency, amplitude, initialPhase);
    this.frequencyModulation = (f) -> f;
    this.amplitudeModulation = (a) -> a;
    this.phaseModulation = (phi) -> phi;
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
}
