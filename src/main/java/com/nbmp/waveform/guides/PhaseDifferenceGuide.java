/* (C)2024 */
package com.nbmp.waveform.guides;

import com.nbmp.waveform.generation.EfficientWaveGeneration;
import com.nbmp.waveform.models.SliderTarget;
import org.apache.commons.math3.util.MathUtils;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;


@RequiredArgsConstructor
public class PhaseDifferenceGuide extends SmartGuide {
  private final WaveGuide wave1, wave2;


  @Override
  public Double compute(Double t, Double timeStep) {
    return compute(t);
  }

  public Double compute(Double t) {
    double cumulativePhaseAngle = wave1.cumulativePhaseRadians;
    double cumulativePhaseAngle2 = wave2.cumulativePhaseRadians;
    double deltaPhi =
            MathUtils.normalizeAngle(cumulativePhaseAngle2 - cumulativePhaseAngle, 0.0); // In radians

    return deltaPhi / Math.PI;
  }

  public void computeChange(Double t) {
    compute(t);
    regenerateSeries();
  }

  @Override
  public Consumer<Double> recompute(SliderTarget target) {
    return this::computeChange;
  }

  public void regenerateSeries() {
    this.getSeries().getData().clear();
    this.getGenerator().regenerate(this);
  }
}
