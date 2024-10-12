/* (C)2024 */
package com.nbmp.waveform.controller;

import javafx.scene.chart.XYChart;

import com.nbmp.waveform.model.generation.GenConstants;
import com.nbmp.waveform.model.guides.SineWaveform;
import com.nbmp.waveform.model.guides.Waveform;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class WavesRegister {
  private final Waveform waveform;
  private final XYChart.Series<Number, Number> series;

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

  public WavesRegister addToChart(XYChart<Number, Number> chart) {
    chart.getData().add(series);
    series.nodeProperty().get().setId(name);
    return this;
  }

  public void addData(double[][] data) {
    for (double[] point : data) {
      series
          .getData()
          .add(new XYChart.Data<>(point[GenConstants.TIME], point[GenConstants.AMPLITUDE]));
    }
  }

  public void refreshData(double[][] newData) {
    series.getData().clear();
    addData(newData);
  }
}
