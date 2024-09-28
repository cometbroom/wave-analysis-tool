/* (C)2024 */
package com.nbmp.waveform.model.generation;

import javafx.scene.chart.XYChart;

import com.nbmp.waveform.view.GraphDashboard;

import lombok.Builder;

@Builder
public class EventLineGenerator {
  @Builder.Default private String LABEL = "Line";
  @Builder.Default private XYChart.Series<Number, Number> series = new XYChart.Series<>();

  public GraphDashboard graph;

  public void addPoint(double t, double y) {
    series.getData().add(new XYChart.Data<>(t, y));
  }
}
