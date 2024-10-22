/* (C)2024 */
package com.nbmp.waveform.view;

import com.nbmp.waveform.model.generation.StreamReactor;
import javafx.scene.chart.XYChart;

import com.nbmp.waveform.controller.WaveController;
import com.nbmp.waveform.model.generation.GenConstants;
import com.nbmp.waveform.model.generation.Generator;
import com.nbmp.waveform.model.waveform.SineWaveform;
import com.nbmp.waveform.model.waveform.Waveform;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class WavesRegister {
  private final Waveform waveform;
  private final XYChart.Series<Number, Number> series;
  public static final int VIEW_RESOLUTION = Math.min(500, Generator.SAMPLE_RATE / 2);
  @Autowired private StreamReactor reactor;

  private WavesRegister modulator;
  private String name = "";

  public WavesRegister(String name, WaveController.WaveType type, double frequency, double amplitude) {
    this.series = new XYChart.Series<Number, Number>();
    this.waveform =
        switch (type) {
          case SINE -> new SineWaveform(frequency, amplitude);
          case SQUARE, TRIANGLE, SAWTOOTH -> throw new IllegalArgumentException(
              "Wave type not supported");
        };
    this.name = name;
    series.setName(name);
  }

  public void refreshData() {
    var viewResolutionDuration = VIEW_RESOLUTION * (WaveController.duration.get() / 1000);
    int step = Math.max(viewResolutionDuration, 1);

    series.getData().clear();
    reactor.getStream().buffer(step).subscribe(
        buffer -> {
          var lastBuffer = buffer.getFirst();
          series.getData().add(new XYChart.Data<>(lastBuffer.time(), this.name.contains("1") ? lastBuffer.wave1() : lastBuffer.wave2()));
        });
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
