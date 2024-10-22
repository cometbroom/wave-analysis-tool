/* (C)2024 */
package com.nbmp.waveform.model.dto;

import javafx.scene.chart.XYChart;

import org.springframework.beans.factory.annotation.Autowired;

import com.nbmp.waveform.controller.WaveController;
import com.nbmp.waveform.model.generation.GenConstants;
import com.nbmp.waveform.model.generation.StreamReactor;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.nbmp.waveform.view.WavesRegister.VIEW_RESOLUTION;

@AllArgsConstructor
@Getter
public class TimeSeries {
  private double[][] timeXAmplitude;
  private XYChart.Series<Number, Number> series;
  @Autowired private StreamReactor reactor;

  public TimeSeries() {
    this(new double[][] {new double[] {}});
  }

  public TimeSeries(double[][] timeXAmplitude) {
    this(timeXAmplitude, new XYChart.Series<>());
  }

  public <Y, X> TimeSeries(double[][] timeXAmplitude, XYChart.Series<Number, Number> xySeries) {
    this.timeXAmplitude = timeXAmplitude;
    this.series = xySeries;
  }

  public void refreshData() {
    int step = VIEW_RESOLUTION * (WaveController.duration.get() / 1000);
    series.getData().clear();
    reactor
        .getStream()
        .buffer(step)
        .subscribe(
            buffer -> {
              var lastBuffer = buffer.getFirst();
              series.getData().add(new XYChart.Data<>(lastBuffer.time(), lastBuffer.resultWave()));
            });
  }

  public void refreshData(double[][] data) {
    var viewResolutionDuration = VIEW_RESOLUTION * (WaveController.duration.get() / 1000);
    int totalSamples = data.length;
    int step = Math.max(totalSamples / viewResolutionDuration, 1);
    series.getData().clear();
    for (int i = 0; i < totalSamples; i += step) {
      series
          .getData()
          .add(new XYChart.Data<>(data[i][GenConstants.TIME], data[i][GenConstants.AMPLITUDE]));
    }
  }
}
