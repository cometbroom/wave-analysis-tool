/* (C)2024 */
package com.nbmp.waveform.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nbmp.waveform.model.generation.SynthesisMode;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class GraphController {
  @FXML public LineChart<Number, Number> waveformChart;
  @FXML public LineChart<Number, Number> waveformChart2;
  @Autowired private ControllersState state;

  @FXML
  public void initialize() {
    state.getWaveform1().addToChart(waveformChart);
    state.getWaveform2().addToChart(waveformChart2);
  }

  public void syntheisModeChanged(SynthesisMode mode) {

    state.setSynthesisMode(mode);
  }
}
