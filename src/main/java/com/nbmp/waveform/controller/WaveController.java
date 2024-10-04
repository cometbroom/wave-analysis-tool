/* (C)2024 */
package com.nbmp.waveform.controller;

import java.util.LinkedList;
import java.util.List;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.util.Duration;

import org.springframework.stereotype.Component;

@Component
public class WaveController {
  public ComboBox waveformTypeComboBox;
  public ColorPicker colorPicker;
  public Label statusLabel;
  public LineChart resultWaveformChart;
  @FXML private Slider frequencySlider;
  @FXML public Slider frequencySlider2;
  @FXML private Label sliderLabel;
  @FXML private Label sliderLabel2;
  @FXML private LineChart<Number, Number> waveformChart;
  @FXML private Button addWaveButton;
  private WaveService waveService = new WaveService();

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
    createGuide(WaveType.SINE, frequencySlider, waveformChart, sliderLabel, 5, 1);
    createGuide("sine2", WaveType.SINE, frequencySlider2, waveformChart, sliderLabel2, 10, 1);
  }

  public void createGuide(
      WaveType type,
      Slider controlSlider,
      LineChart<Number, Number> chartToAddTo,
      Label labelOfAffectedSlider,
      double frequency,
      double amplitude) {
    createGuide("", type, controlSlider, chartToAddTo, labelOfAffectedSlider, frequency, amplitude);
  }

  public void createGuide(
      String id,
      WaveType type,
      Slider controlSlider,
      LineChart<Number, Number> chartToAddTo,
      Label labelOfAffectedSlider,
      double frequency,
      double amplitude) {
    var guide = waveService.createGuide(type, frequency, amplitude);
    chartToAddTo.getData().add(guide.series());

    PauseTransition pause = new PauseTransition(Duration.millis(50));
    controlSlider
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              labelOfAffectedSlider.setText("Frequency: %.2f Hz".formatted(newValue.doubleValue()));
              pause.setOnFinished(
                  event -> {
                    guide.guide().setFrequency(newValue.doubleValue());
                    guide.series().getData().clear();
                    waveService.getPointsFor(guide);
                  });
              pause.playFromStart();
            });
    guide.series().nodeProperty().get().setId(id);
    guide.series().setName("Wave" + id);
    waves.add(guide);
  }
}
