/* (C)2024 */
package com.nbmp.waveform.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import com.nbmp.waveform.model.generation.ChaosSynthesis;
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
  public LineChart<Number, Number> resultWaveformChart;
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
    var sineWave = createWaveform(WaveType.SINE, 5, 1);
    var sineWave2 = createWaveform(WaveType.SINE, 10, 1);

    int duration = 1;

    waveformChart.getData().add(sineWave.series());
    resultWaveformChart.getData().add(sineWave2.series());

    sineWave2.series().nodeProperty().get().setId("sine2");

    var chaosSytnthesis = new ChaosSynthesis(sineWave, sineWave2);
    var data = chaosSytnthesis.compute(duration);

    waveService.addDataToSeries(sineWave, data[0]);
    waveService.addDataToSeries(sineWave2, data[1]);
    addListenerToSlider(frequencySlider, sliderLabel, (newValue) -> {
        sineWave.guide().setFrequency(newValue);
        var newData = chaosSytnthesis.compute(duration);
        sineWave.series().getData().clear();
        sineWave2.series().getData().clear();
        waveService.addDataToSeries(sineWave, newData[0]);
        waveService.addDataToSeries(sineWave2, newData[1]);
    });
    addListenerToSlider(frequencySlider2, sliderLabel2, (newValue) -> {
      sineWave2.guide().setFrequency(newValue);
        var newData = chaosSytnthesis.compute(duration);
        sineWave.series().getData().clear();
        sineWave2.series().getData().clear();
        waveService.addDataToSeries(sineWave, newData[0]);
        waveService.addDataToSeries(sineWave2, newData[1]);
    });
  }

  public WavesRegister createWaveform(WaveType type, double frequency, double amplitude) {
    var guide = waveService.createWaveform(type, frequency, amplitude);
    guide.series().setName("Wave");
    return guide;
  }

  public void addListenerToSlider(Slider controlSlider, Label labelOfAffectedSlider, Consumer<Double> updateFunction) {
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
