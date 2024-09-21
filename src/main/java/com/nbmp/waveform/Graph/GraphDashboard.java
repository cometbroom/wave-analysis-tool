/* (C)2024 */
package com.nbmp.waveform.Graph;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;

import com.nbmp.waveform.generation.WaveGenerator;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GraphDashboard {
  private LineChart<Number, Number> lineChart;

  @Builder.Default private int width = 800;
  @Builder.Default private int height = 600;

  @Builder.Default private String xLabel = "Time (s)";
  @Builder.Default private String yLabel = "Amplitude";

  public GraphDashboard addSeries(WaveGenerator... multipleSeries) {
    for (var series : multipleSeries) {
      addSeries(series);
    }
    return this;
  }

  public GraphDashboard addSeries(WaveGenerator series) {
    if (lineChart == null) {
      setupLineChart();
    }
    lineChart.getData().add(series.generate());
    return this;
  }

  public GraphDashboard view(Stage stage) {
    Scene scene = new Scene(lineChart, width, height);
    stage.setScene(scene);
    stage.show();
    return this;
  }

  private void setupLineChart() {
    final NumberAxis xAxis = new NumberAxis(), yAxis = new NumberAxis();
    xAxis.setLabel(xLabel);
    yAxis.setLabel(yLabel);
    lineChart = new LineChart<>(xAxis, yAxis);
    lineChart.setTitle("Graph");
  }
}
