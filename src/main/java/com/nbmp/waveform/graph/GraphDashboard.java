/* (C)2024 */
package com.nbmp.waveform.graph;

import java.util.List;
import java.util.Map;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import lombok.Getter;

@Getter
public class GraphDashboard extends UiView implements UiViewable {
  private LineChart<Number, Number> lineChart;

  public GraphDashboard() {
    this.lineChart = createLineChart();
  }

  public GraphDashboard addSeries(List<XYChart.Series<Number, Number>> multipleSeries) {
    multipleSeries.forEach(this::addSeries);
    return this;
  }

  public GraphDashboard addSeries(XYChart.Series<Number, Number> series) {
    lineChart.getData().add(series);
    return this;
  }

  public GraphDashboard view(Stage stage) {
    Scene scene = new Scene(lineChart, width, height);
    stage.setScene(scene);
    stage.show();
    return this;
  }

  @Override
  public void start(Stage stage) {
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
  }

  private LineChart<Number, Number> createLineChart() {
    final NumberAxis xAxis = new NumberAxis(), yAxis = new NumberAxis();
    xAxis.setLabel(xLabel);
    yAxis.setLabel(yLabel);
    var lineChart = new LineChart<>(xAxis, yAxis);
    lineChart.setCreateSymbols(false);
    lineChart.setTitle("Graph");
    return lineChart;
  }
}
