/* (C)2024 */
package com.nbmp.waveform.view;

import java.util.List;
import java.util.Map;

import com.nbmp.waveform.controller.SliderBox;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
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
  @Builder.Default private double totalTime = 5;
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

  public GraphDashboard viewVBox(Stage stage) {
    // Arrange the UI elements vertically
    VBox vbox = new VBox();
    vbox.setAlignment(Pos.CENTER);
    vbox.setSpacing(10);
    vbox.getChildren().add(lineChart);

    for (Map.Entry<Label, Slider> entry : SliderBox.sliders.entrySet()) {
      vbox.getChildren().addAll(entry.getValue(), entry.getKey());
    }

    // Set up the scene and show the stage
    Scene scene = new Scene(vbox, 800, 600);
    stage.setScene(scene);
    stage.show();
    return this;
  }

  private void setupLineChart() {
    final NumberAxis xAxis = new NumberAxis(), yAxis = new NumberAxis();
    xAxis.setLabel(xLabel);
    yAxis.setLabel(yLabel);

    lineChart = new LineChart<>(xAxis, yAxis);
    lineChart.setCreateSymbols(false);
    lineChart.setTitle("Graph");
  }
}
