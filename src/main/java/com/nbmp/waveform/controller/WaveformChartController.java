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
import com.nbmp.waveform.model.generation.GenerationState;

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
  @Autowired private GenerationState genState;
  private XYChart.Series<Number, Number> seriesTemp;

  /**
   * Initializes the waveform chart controller.
   */
  @FXML
  public void initialize() {
    waveformChart.getData().add(state.getView1().getSeries());
    waveformChart2.getData().add(state.getView2().getSeries());
    resultChart.getData().add(state.getResultView().getSeries());
    state
        .getView1()
        .getData()
        .addListener(
            (ListChangeListener<XYChart.Data<Number, Number>>)
                c -> {
                  if (c.next() && c.wasRemoved()) {
                    GenerationScope.refreshScope();
                  }
                });
    state.getView1().nodeProperty().get().setId(state.getView1().getName());
    state.getView2().nodeProperty().get().setId(state.getView2().getName());
    state.getResultView().nodeProperty().get().setId("resultChart");
  }
}
