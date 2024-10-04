/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.nbmp.waveform.model.guides.SineWaveGuide;
import com.nbmp.waveform.model.guides.WaveGuide;

@Service
@Scope("singleton")
public class WaveformGenerator extends Generator {
  //    public double[] generateOnlyY(Guide guide, int duration) {
  //        var data = new double[duration * (int) SAMPLE_RATE];
  //        int i = 0;
  //        for (double t = 0; t < duration; t += timeStep) {
  //            data[i++] = guide.compute(t, timeStep);
  //        }
  //        return data;
  //    }

  private Map<String, WaveGuide> guides = new HashMap<>();

  public double[][] generateSineWave(
      String name, double frequency, double amplitude, int duration) {
    if (guides.get(name) == null) guides.put(name, new SineWaveGuide(frequency, amplitude));
    return generate(guides.get(name)::compute, duration);
  }

  public double[][] generate(WaveGuide guide, int duration) {
    return generate(guide::compute, duration);
  }

  public double[][] generate(WaveGuide guide, int start, int duration) {
    return generate(guide::compute, start, duration);
  }

  public double[][] generate(BiFunction<Double, Double, Double> computeFunction, int duration) {
    return generate(computeFunction, 0, duration);
  }

  public double[][] generate(
      BiFunction<Double, Double, Double> computeFunction, int start, int duration) {
    return generate(computeFunction, (double) start, duration * (int) SAMPLE_RATE);
  }

  public double[][] generate(
      BiFunction<Double, Double, Double> computeFunction, double start, int sampleCount) {
    var data = new double[sampleCount][2];
    for (int i = 0; i < sampleCount; i++) {
      data[i] = new double[] {start, computeFunction.apply(start += timeStep, timeStep)};
    }
    return data;
  }
}
