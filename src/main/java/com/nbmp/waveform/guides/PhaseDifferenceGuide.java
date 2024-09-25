/* (C)2024 */
package com.nbmp.waveform.guides;

import org.apache.commons.math3.util.MathUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PhaseDifferenceGuide extends SmartGuide {
  private final WaveGuide wave1, wave2;

  @Override
  public Double compute(Double t, Double timeStep) {
    return compute(t);
  }

  public Double compute(Double t) {
    double deltaPhi = MathUtils.normalizeAngle(
            wave2.cumulativePhaseRadians - wave1.cumulativePhaseRadians, 0.0);
    return deltaPhi / Math.PI;
  }

  public void regenerateSeries() {
    this.getSeries().getData().clear();
    this.getGenerator().regenerate(this);
  }
}