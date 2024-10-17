/* (C)2024 */
package com.nbmp.waveform.model.waveform;

import com.nbmp.waveform.model.dto.ModulationActiveWaveProps;
import com.nbmp.waveform.model.generation.Generator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Waveform {
  protected ModulationActiveWaveProps props = new ModulationActiveWaveProps();
  protected double cumulativePhaseRadians = 0,
      mavValue = 0.0,
      previousAmplitude = 0.0,
      currentPhase = 0.0;

  public abstract Double compute(Double timeStep);

  public double[] computeTY(Double t) {
    return computeTY(t, Generator.timeStep);
  }

  public double[] computeTY(Double t, Double timeStep) {
    return new double[] {t, compute(timeStep)};
  }

  /**
   * dynamically compresses greater than -1/1 signals
   * @return
   */
  public double getCompressedPreviousAmplitude() {
    double boundedAmplitude = previousAmplitude;

    if (previousAmplitude > 1 || previousAmplitude < -1) {
      if (previousAmplitude > mavValue) {
        mavValue = previousAmplitude;
      }
      boundedAmplitude = previousAmplitude / mavValue;
    }
    // bound previous amplitude to radians from -pi to pi
    return 2 * Math.asin(boundedAmplitude);
  }
}
