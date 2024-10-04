/* (C)2024 */
package com.nbmp.waveform.controller;

import java.util.LinkedList;
import java.util.List;
import javafx.scene.chart.XYChart;

import org.springframework.stereotype.Component;

import com.nbmp.waveform.model.generation.WaveformGenerator;
import com.nbmp.waveform.model.guides.SineWaveGuide;

@Component
public class WaveService extends WaveformGenerator {
  private List<WavesRegister> waves = new LinkedList<>();

  public WavesRegister createWaveform(
      WaveController.WaveType type, double frequency, double amplitude) {
    var series = new XYChart.Series<Number, Number>();
    WavesRegister guide =
        switch (type) {
          case SINE -> new WavesRegister(new SineWaveGuide(frequency, amplitude), series);
          case SQUARE, TRIANGLE, SAWTOOTH -> throw new IllegalArgumentException(
              "Wave type not supported");
        };
    waves.add(guide);
    return guide;
  }

  public double[][] generateSineWave(double frequency, double amplitude, int duration) {
    var waveGuide = new SineWaveGuide(frequency, amplitude);

    return super.generate(waveGuide::compute, duration);
  }

  public void getPointsFor(WavesRegister wave) {
    var data = super.generate(wave.guide(), 0, 1);
    for (double[] point : data) {
      wave.series().getData().add(new XYChart.Data<>(point[0], point[1]));
    }
  }

  public void getPoints() {
    for (var wave : waves) {
      var data = super.generate(wave.guide(), 0, 1);
      for (double[] point : data) {
        wave.series().getData().add(new XYChart.Data<>(point[0], point[1]));
      }
    }
  }
}
