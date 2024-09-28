/* (C)2024 */
package com.nbmp.waveform.model.guides;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class WaveGuide {
  protected double amplitude = 1,
      frequency = 440,
      phaseRadians = 0,
      cumulativePhaseRadians = 0,
      currentValue = Double.NEGATIVE_INFINITY,
      mavValue = 0.0;

  abstract public Double compute(Double t, Double timeStep);
}
