package com.nbmp.waveform.model.generation;

import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import javafx.concurrent.Task;
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
    this.buffer = new XYChart.Data[size];
    this.series = series;
  }

  public synchronized void addPoint(X t, Y y) {
    if (index.intValue() == buffer.length) {
      flushBuffer();
      buffer = new XYChart.Data[buffer.length];
      index.setValue(0);
    }
    buffer[index.getAndIncrement()] = new XYChart.Data<>(t, y);
  }

  private void flushBuffer() {
    XYChart.Data<X, Y>[] pointsToAdd = buffer.clone();
    // Use a background thread to update the UI incrementally
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws Exception {
            int batchSize = 10; // Add points in batches to smooth out updates
            int total = pointsToAdd.length;
            for (int i = 0; i < total; i += batchSize) {
              final int start = i;
              final int end = Math.min(i + batchSize, total);
              Platform.runLater(
                  () -> {
                    for (int k = start; k < end; k++) {
                      series.getData().add(pointsToAdd[k]);
                    }
                  });
              Thread.sleep(10); // Small delay for smoother UI updates
            }
            return null;
          }
        };
    new Thread(task).start();
  }
}
