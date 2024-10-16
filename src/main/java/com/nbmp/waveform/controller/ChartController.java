/* (C)2024 */
package com.nbmp.waveform.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nbmp.waveform.application.AppConfig;

import lombok.Getter;
import lombok.Setter;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Getter
@Setter
public class ChartController {
  @FXML public LineChart<Number, Number> waveformChart;
  @FXML public LineChart<Number, Number> waveformChart2;
  @FXML public LineChart<Number, Number> resultChart;
  @Autowired private ControllersState state;

  private XYChart.Series<Number, Number> seriesTemp;
  @Autowired private AppConfig appConfig;

  @FXML
  public void initialize() {
    var waveForm1 = state.getWaveform1();
    var waveForm2 = state.getWaveform2();

    waveformChart.getData().add(waveForm1.getSeries());
    waveformChart2.getData().add(waveForm2.getSeries());
    resultChart.getData().add(state.getResultData().getSeries());
    waveForm1.getSeries().nodeProperty().get().setId(waveForm1.getName());
    waveForm2.getSeries().nodeProperty().get().setId(waveForm2.getName());
  }
}
