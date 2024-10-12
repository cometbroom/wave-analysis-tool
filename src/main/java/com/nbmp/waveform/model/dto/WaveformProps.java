/* (C)2024 */
package com.nbmp.waveform.model.dto;

import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class WaveformProps {
  private double frequency, amplitude, initialPhase;
  private Function<Double, Double> frequencyModulation, amplitudeModulation, phaseModulation;

  public enum Selector {
    FREQUENCY,
    AMPLITUDE,
    INITIAL_PHASE
  }

  public WaveformProps(double frequency, double amplitude, double initialPhase) {
    this.frequency = frequency;
    this.amplitude = amplitude;
    this.initialPhase = initialPhase;
    this.frequencyModulation = (f) -> f;
    this.amplitudeModulation = (a) -> a;
    this.phaseModulation = (phi) -> phi;
  }

  public static WaveformProps defaultWaveformProps() {
    var props = new WaveformProps(5, 1, 0);
    return props;
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
