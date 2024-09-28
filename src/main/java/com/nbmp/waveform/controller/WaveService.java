package com.nbmp.waveform.controller;

import com.nbmp.waveform.model.WaveformGenerator;
import com.nbmp.waveform.model.guides.SineWaveGuide;
import javafx.scene.chart.XYChart;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class WaveService {
    private List<WavesRegister> waves = new LinkedList<>();
    private WaveformGenerator generator = new WaveformGenerator();

  public WavesRegister createGuide(WaveController.WaveType type, double frequency, double amplitude) {
    var series = new XYChart.Series<Number, Number>();
    series.setName("guide");
    WavesRegister guide =
        switch (type) {
          case SINE -> new WavesRegister(new SineWaveGuide(frequency, amplitude), series);
          case SQUARE, TRIANGLE, SAWTOOTH ->
              throw new IllegalArgumentException("Wave type not supported");
        };
    waves.add(guide);
    return guide;
  }

  public void getPointsFor(WavesRegister wave) {
    var data = generator.generate(wave.guide(), 0, 1);
    for (double[] point : data) {
      wave.series().getData().add(new XYChart.Data<Number, Number>(point[0], point[1]));
    }
  }

    public void getPoints() {
    for (var wave : waves) {
      var data = generator.generate(wave.guide(), 0, 1);
      for (double[] point : data) {
        wave.series().getData().add(new XYChart.Data<Number, Number>(point[0], point[1]));
      }
    }
  }
}
