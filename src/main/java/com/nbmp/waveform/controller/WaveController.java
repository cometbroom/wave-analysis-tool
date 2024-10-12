/* (C)2024 */
package com.nbmp.waveform.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import com.nbmp.waveform.view.WavesRegister;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.util.Duration;

import org.springframework.stereotype.Component;

import com.nbmp.waveform.model.dto.BiTimeSeries;
import com.nbmp.waveform.model.generation.ChaosSynthesis;

@Component
public class WaveController {
  public Label statusLabel;
  public LineChart<Number, Number> resultWaveformChart;
  @FXML private Slider frequencySlider;
  @FXML public Slider frequencySlider2;
  @FXML private Label sliderLabel;
  @FXML private Label sliderLabel2;
  @FXML private LineChart<Number, Number> waveformChart;

  private List<WavesRegister> waves = new LinkedList<>();

  public enum WaveType {
    SINE,
    SQUARE,
    TRIANGLE,
    SAWTOOTH
  }

  public void createButton() {}

  @FXML
  public void initialize() {
    var sineWave =
        WavesRegister.createWaveform("sine1", WaveType.SINE, 5, 1).addToChart(waveformChart);
    var sineWave2 =
        WavesRegister.createWaveform("sine2", WaveType.SINE, 10, 1).addToChart(resultWaveformChart);

    int duration = 1;

    var chaosSytnthesis = new ChaosSynthesis(sineWave, sineWave2);
    BiTimeSeries data = chaosSytnthesis.compute(duration);

    sineWave.addData(data.timeAmplitude1());
    sineWave2.addData(data.timeAmplitude2());

    addListenerToSlider(
        frequencySlider,
        sliderLabel,
        (newValue) -> {
          sineWave.getWaveform().getProps().setFrequency(newValue);
          var newData = chaosSytnthesis.compute(duration);
          sineWave.refreshData(newData.timeAmplitude1());
          sineWave2.refreshData(newData.timeAmplitude2());
        });
    addListenerToSlider(
        frequencySlider2,
        sliderLabel2,
        (newValue) -> {
          sineWave2.getWaveform().getProps().setFrequency(newValue);
          var newData = chaosSytnthesis.compute(duration);
          sineWave.refreshData(newData.timeAmplitude1());
          sineWave2.refreshData(newData.timeAmplitude2());
        });
  }

  public void addListenerToSlider(
      Slider controlSlider, Label labelOfAffectedSlider, Consumer<Double> updateFunction) {
    PauseTransition pause = new PauseTransition(Duration.millis(50));
    controlSlider
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              labelOfAffectedSlider.setText("Frequency: %.2f Hz".formatted(newValue.doubleValue()));
              pause.setOnFinished(
                  event -> {
                    updateFunction.accept(newValue.doubleValue());
                  });
              pause.playFromStart();
            });
  }
}
