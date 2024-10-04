/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.function.BiFunction;

public class GenerationApi {
  private BiFunction<Double, Double, Double> computeFunction;
  private WaveformGenerator generator;
  public static final int AMPLITUDE = 1;
  public static final int TIME = 0;

  public GenerationApi(BiFunction<Double, Double, Double> computeFunction) {
    this.computeFunction = computeFunction;
  }

  public GenerationApi createWaveform(BiFunction<Double, Double, Double> computeFunction) {
    this.generator = new WaveformGenerator();
    this.computeFunction = computeFunction;
    return this;
  }

  public double readWaveform(double time) {
    var data = generator.generate(computeFunction, time, 1);
    return data[0][AMPLITUDE];
  }
}
