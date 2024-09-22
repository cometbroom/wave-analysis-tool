/* (C)2024 */
package com.nbmp.waveform.guides;

import org.apache.commons.math3.util.MathUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PhaseDifferenceGuide implements Guide {
  private final WaveGuide wave1, wave2;

  @Override
  public Double compute(Double t, Double timeStep) {
    double cumulativePhaseAngle = wave1.cumulativePhaseRadians;
    double cumulativePhaseAngle2 = wave2.cumulativePhaseRadians;
    double deltaPhi =
        MathUtils.normalizeAngle(cumulativePhaseAngle2 - cumulativePhaseAngle, 0.0); // In radians

    return deltaPhi / Math.PI;
  }
}
