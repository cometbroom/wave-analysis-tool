/* (C)2024 */
package com.nbmp.waveform.models;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.Getter;

@Getter
public class WaveCompute {

  private AtomicReference<Double> maxValue = new SmartData<>(Double.NEGATIVE_INFINITY);

  public Function<Double, Double> sineWave(double amplitude, double frequency, double phase) {
    return t -> amplitude * Math.sin(2 * Math.PI * frequency * t + phase);
  }

  private Function<Double, Double> phaseDiff(
      double frequency1, double frequency2, double phase1, double phase2) {
    // Calculate phase difference at each time step
    return (t) -> {
      double phaseDiff =
          (2 * Math.PI * frequency2 * t + phase2) - (2 * Math.PI * frequency1 * t + phase1);
      phaseDiff = phaseDiff % (2 * Math.PI); // Ensure it's within 0 to 2π

      // Optionally convert phase difference to degrees
      return Math.toDegrees(phaseDiff);
    };
  }

  private Consumer<Double> detectFirstPeakTime(
      Function<Double, Double> computeFunction, double timeStep) {

    return (t) -> {
      double nextValue = computeFunction.apply(t + timeStep);
      double currentValue = computeFunction.apply(t - timeStep);

      if (currentValue > maxValue.get() && currentValue > nextValue) {
        maxValue.set(currentValue);
      }
    };
  }
}
