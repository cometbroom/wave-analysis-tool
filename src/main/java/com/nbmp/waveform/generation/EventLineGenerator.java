/* (C)2024 */
package com.nbmp.waveform.generation;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import javafx.scene.chart.XYChart;

import com.nbmp.waveform.graph.GraphDashboard;

import lombok.Builder;

@Builder
public class EventLineGenerator implements Generator {
  @Builder.Default private String LABEL = "Line";
  @Builder.Default private XYChart.Series<Number, Number> series = new XYChart.Series<>();

  public GraphDashboard graph;

  public void addPoint(double t, double y) {
    series.getData().add(new XYChart.Data<>(t, y));
  }

  public String getName() {
    return LABEL;
  }

  @Override
  public XYChart.Series<Number, Number> getSeries() {
    return this.series;
  }

  @Override
  public Function<Double, Double> getComputeFunction() {
    return (t) -> 0d;
  }

  @Override
  public List<Consumer<Double>> getEventRuns() {
    return List.of();
  }
}
