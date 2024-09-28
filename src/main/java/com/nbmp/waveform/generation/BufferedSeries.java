/* (C)2024 */
package com.nbmp.waveform.generation;

import javafx.scene.chart.XYChart;

import org.apache.commons.lang3.mutable.MutableInt;

import lombok.RequiredArgsConstructor;

public class BufferedSeries<X, Y> {
  private XYChart.Data<X, Y>[] buffer;
  private final MutableInt index = new MutableInt();
  private final XYChart.Series<X, Y> series;

  @RequiredArgsConstructor
  public enum BufferType {
    TIME(0),
    AMPLITUDE(1);
    private final int index;
  }

  public BufferedSeries(int size, XYChart.Series<X, Y> series) {
    buffer = new XYChart.Data[size];
    this.series = series;
  }

  public void addPoint(X t, Y y) {
    if (index.intValue() == buffer.length) {
      index.setValue(0);
      series.getData().addAll(buffer);
      buffer = new XYChart.Data[buffer.length];
    }
    buffer[index.getAndIncrement()] = new XYChart.Data<>(t, y);
  }
}
