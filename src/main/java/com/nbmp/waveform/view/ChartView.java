/* (C)2024 */
package com.nbmp.waveform.view;

import javafx.scene.chart.XYChart;

import com.nbmp.waveform.application.AppConstants;
import com.nbmp.waveform.controller.WaveController;
import com.nbmp.waveform.model.generation.Generator;
import com.nbmp.waveform.model.utils.GenConstants;

import lombok.Getter;
import lombok.experimental.Delegate;

@Getter
public class ChartView {
  @Delegate private XYChart.Series<Number, Number> series;
  // TODO: Have VIEW_RESOLUTION be a bit dependent on duration
  public static final int VIEW_RESOLUTION = Math.min(500, Generator.SAMPLE_RATE / 2);

  public void init(String name) {
    this.series = new XYChart.Series<>();
    this.setName(name);
  }

  public void addPoint(int i, double value) {
    if (i % AppConstants.VIEW_STEP == 0) {
      series.getData().add(new XYChart.Data<>(AppConstants.getTimeFromIndex(i), value));
    }
  }

  public void addData(int i, double value) {
    series.getData().add(new XYChart.Data<>(AppConstants.getTimeFromIndex(i), value));
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
