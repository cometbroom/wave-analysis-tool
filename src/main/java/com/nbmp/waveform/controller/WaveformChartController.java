/* (C)2024 */
package com.nbmp.waveform.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
    var waveForm1 = state.getWaveform1();
    var waveForm2 = state.getWaveform2();

    waveformChart.getData().add(waveForm1.getSeries());
    waveformChart2.getData().add(waveForm2.getSeries());
    resultChart.getData().add(state.getResultData().getSeries());
    waveForm1.getSeries().nodeProperty().get().setId(waveForm1.getName());
    waveForm2.getSeries().nodeProperty().get().setId(waveForm2.getName());
    state.getResultData().getSeries().setName("Combination Result");
    state.getResultData().getSeries().nodeProperty().get().setId("resultChart");
  }
}

