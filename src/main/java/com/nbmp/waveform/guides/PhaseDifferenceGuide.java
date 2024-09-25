/* (C)2024 */
package com.nbmp.waveform.guides;

import org.apache.commons.math3.util.MathUtils;

import com.nbmp.waveform.extras.Reactive;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PhaseDifferenceGuide extends SmartGuide implements Reactive {
  private final WaveGuide wave1, wave2;

  public PhaseDifferenceGuide(WaveGuide wave1, WaveGuide wave2, WaveOptions... options) {
    this(wave1, wave2);
    this.setupOptions(options);
  }

  @Override
  public Double compute(Double t, Double timeStep) {
    double deltaPhi =
            MathUtils.normalizeAngle(wave2.cumulativePhaseRadians - wave1.cumulativePhaseRadians, 0.0);
    return deltaPhi / Math.PI;
  }

  @Override
  public void regenerateSeries() {
    this.getSeries().getData().clear();
    this.getGenerator().regenerate();
  }
}
