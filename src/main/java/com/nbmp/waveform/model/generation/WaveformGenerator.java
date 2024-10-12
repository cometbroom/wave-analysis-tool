/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.nbmp.waveform.model.guides.SineWaveform;
import com.nbmp.waveform.model.guides.Waveform;

@Service
@Scope("singleton")
public class WaveformGenerator extends Generator {
  private Map<String, Waveform> guides = new HashMap<>();

  public double[][] generateSineWave(
      String name, double frequency, double amplitude, int duration) {
    if (guides.get(name) == null) guides.put(name, new SineWaveform(frequency, amplitude));
    return generate(guides.get(name)::compute, duration);
  }

  public double[][] generate(Waveform guide, int duration) {
    return generate(guide::compute, duration);
  }

  public double[][] generate(Waveform guide, int start, int duration) {
    return generate(guide::compute, start, duration);
  }

  public double[][] generate(Function<Double, Double> computeFunction, int duration) {
    return generate(computeFunction, 0, duration);
  }

  public double[][] generate(Function<Double, Double> computeFunction, int start, int duration) {
    return generate(computeFunction, (double) start, duration * (int) SAMPLE_RATE);
  }

  public double[][] generate(
      Function<Double, Double> computeFunction, double start, int sampleCount) {
    var data = new double[sampleCount][2];
    for (int i = 0; i < sampleCount; i++) {
      data[i] = new double[] {start, computeFunction.apply(timeStep)};
    }
    return data;
  }
}
