/* (C)2024 */
package com.nbmp.waveform.graph;

import java.util.List;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GraphDashboard {
  private LineChart<Number, Number> lineChart;

  @Builder.Default private int width = 800;
  @Builder.Default private int height = 600;
  @Builder.Default private double timeStep = 0.01;
  @Builder.Default private double totalTime = 10;
  @Builder.Default private String xLabel = "Time (s)";
  @Builder.Default private String yLabel = "Amplitude";

  public GraphDashboard addSeries(List<XYChart.Series<Number, Number>> multipleSeries) {
    multipleSeries.forEach(this::addSeries);
    return this;
  }

  public GraphDashboard addSeries(XYChart.Series<Number, Number> series) {
    if (lineChart == null) {
      setupLineChart();
    }
    lineChart.getData().add(series);
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
