/* (C)2024 */
package com.nbmp.waveform.view;

import javafx.scene.chart.XYChart;

import com.nbmp.waveform.controller.WaveController;
import com.nbmp.waveform.model.generation.GenConstants;
import com.nbmp.waveform.model.generation.Generator;
import com.nbmp.waveform.model.waveform.SineWaveform;
import com.nbmp.waveform.model.waveform.Waveform;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class WavesRegister {
  private final Waveform waveform;
  private final XYChart.Series<Number, Number> series;
  public static final int VIEW_RESOLUTION = Math.min(500, Generator.SAMPLE_RATE / 2);

  private WavesRegister modulator;
  private String name = "";

  public static WavesRegister createWaveform(
      String name, WaveController.WaveType type, double frequency, double amplitude) {
    var series = new XYChart.Series<Number, Number>();
    WavesRegister waveRegister =
        switch (type) {
          case SINE -> new WavesRegister(new SineWaveform(frequency, amplitude), series);
          case SQUARE, TRIANGLE, SAWTOOTH -> throw new IllegalArgumentException(
              "Wave type not supported");
        };
    waveRegister.name = name;
    series.setName(name);
    return waveRegister;
  }

  public void addData(double[][] data) {
    var viewResolutionDuration = VIEW_RESOLUTION * (WaveController.duration.get() / 1000);
    int totalSamples = data.length;
    int step = Math.max(totalSamples / viewResolutionDuration, 1);
    for (int i = 0; i < totalSamples; i += step) {
      series
          .getData()
          .add(new XYChart.Data<>(data[i][GenConstants.TIME], data[i][GenConstants.AMPLITUDE]));
    }
  }

  public void refreshData(double[][] newData) {
    series.getData().clear();
    addData(newData);
  }
}
