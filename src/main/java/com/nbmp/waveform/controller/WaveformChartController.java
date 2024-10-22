/* (C)2024 */
package com.nbmp.waveform.controller;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nbmp.waveform.application.GenerationScope;

import lombok.Getter;
import lombok.Setter;

/**
 * Controller class for managing waveform charts in the UI. Right side of the landing page.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Getter
@Setter
public class WaveformChartController {
  @FXML public LineChart<Number, Number> waveformChart;
  @FXML public LineChart<Number, Number> waveformChart2;
  @FXML public LineChart<Number, Number> resultChart;
  @Autowired private ControllersState state;

  private XYChart.Series<Number, Number> seriesTemp;

  /**
   * Initializes the waveform chart controller.
   */
  @FXML
  public void initialize() {
    var waveForm1 = state.getGenState().getWave1();
    var waveForm2 = state.getGenState().getWave2();

    waveformChart.getData().add(waveForm1.getSeries());
    waveformChart2.getData().add(waveForm2.getSeries());
    waveForm1
        .getSeries()
        .getData()
        .addListener(
            (ListChangeListener<XYChart.Data<Number, Number>>)
                c -> {
                  if (c.next() && c.wasRemoved()) {
                    GenerationScope.refreshScope();
                  }
                });
    resultChart.getData().add(state.getGenState().getResultSeries().getSeries());
    waveForm1.getSeries().nodeProperty().get().setId(waveForm1.getName());
    waveForm2.getSeries().nodeProperty().get().setId(waveForm2.getName());
    state.getGenState().getResultSeries().getSeries().setName("Combination Result");
    state.getGenState().getResultSeries().getSeries().nodeProperty().get().setId("resultChart");
  }
}
