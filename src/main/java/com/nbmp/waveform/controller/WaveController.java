/* (C)2024 */
package com.nbmp.waveform.controller;

import java.util.concurrent.atomic.AtomicReference;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.util.Duration;

import org.springframework.stereotype.Component;

import com.nbmp.waveform.model.generation.ChaosSynthesis;
import com.nbmp.waveform.model.generation.IndependentSynthesis;
import com.nbmp.waveform.model.generation.Synthesis;
import com.nbmp.waveform.view.SynthesisViewer;
import com.nbmp.waveform.view.WavesRegister;

@Component
public class WaveController {
  public Label statusLabel;
  public LineChart<Number, Number> resultWaveformChart;
  @FXML public ComboBox<String> synthesisMode;
  @FXML public TextField durationField;
  public static AtomicReference<Integer> duration = new AtomicReference<>(1);
  @FXML private Slider frequencySlider;
  @FXML private Slider frequencySlider2;
  @FXML private Label sliderLabel;
  @FXML private Label sliderLabel2;
  @FXML private LineChart<Number, Number> waveformChart;

  private SynthesisViewer synthesisViewer;
  private WavePropsSliders slider1;
  private WavePropsSliders slider2;

  @FXML
  public void durationEntered(ActionEvent actionEvent) {
    System.out.println("hi");
  }

  public enum WaveType {
    SINE,
    SQUARE,
    TRIANGLE,
    SAWTOOTH
  }

  @FXML
  public void initialize() {
    var sineWave =
        WavesRegister.createWaveform("sine1", WaveType.SINE, 5, 1).addToChart(waveformChart);
    var sineWave2 =
        WavesRegister.createWaveform("sine2", WaveType.SINE, 10, 1).addToChart(resultWaveformChart);
    Synthesis synthesis = new IndependentSynthesis(sineWave, sineWave2);
    setupDurationField();
    synthesisViewer = new SynthesisViewer(sineWave, sineWave2, synthesis);

    synthesisMode.getItems().addAll("Independent", "Chaos");
    synthesisMode.getSelectionModel().select(0);
    synthesisMode
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observableValue, s, t1) -> {
              switch (t1) {
                case "Independent" -> synthesisViewer.setSynthesis(
                    new IndependentSynthesis(sineWave, sineWave2));
                case "Chaos" -> synthesisViewer.setSynthesis(
                    new ChaosSynthesis(sineWave, sineWave2));
                default -> throw new IllegalStateException("Unexpected value: " + t1);
              }
              ;
              synthesisViewer.synthesizeForPair(slider1, slider2);
            });

    slider1 = new WavePropsSliders(sineWave, frequencySlider, sliderLabel);
    slider2 = new WavePropsSliders(sineWave2, frequencySlider2, sliderLabel2);

    slider1.addListenerAccordingToTarget(WavePropsSliders.Target.FREQUENCY);
    slider2.addListenerAccordingToTarget(WavePropsSliders.Target.FREQUENCY);
    synthesisViewer.synthesizeForPair(slider1, slider2);
  }

  private void setupDurationField() {
    durationField.setText(duration + "");

    durationField.setOnMouseClicked(
        (event) -> {
          durationField.selectAll();
        });

    durationField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (!newValue.matches("\\d*")) {
                durationField.setText(newValue.replaceAll("[^\\d]", ""));
              }
              PauseTransition pause = new PauseTransition(Duration.millis(500));
              pause.setOnFinished(
                  event -> {
                    int newDuration = getDuration();
                    if (newDuration > 5) {
                      durationField.setText("5");
                    }
                    if (newDuration < 1) {
                      durationField.setText("1");
                    }
                    duration.set(getDuration());
                    synthesisViewer.synthesizeForPair(slider1, slider2);
                  });
              pause.playFromStart();
            });
  }

  public int getDuration() {
    return Integer.parseInt(durationField.getText());
  }
}
