/* (C)2024 */
package com.nbmp.waveform.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Getter
@Setter
public class ChartController {
  @FXML public LineChart<Number, Number> waveformChart;
  @FXML public LineChart<Number, Number> waveformChart2;
  @Autowired private ControllersState state;

  @FXML
  public void initialize() {
    state.getWaveform1().addToChart(waveformChart);
    state.getWaveform2().addToChart(waveformChart2);
  }
}
