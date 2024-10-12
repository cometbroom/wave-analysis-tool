/* (C)2024 */
package com.nbmp.waveform.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import com.nbmp.waveform.model.dto.WavePropsSliders;
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
    var synthesisViewer = new SynthesisViewer(sineWave, sineWave2, chaosSytnthesis, duration);
    var slider1 = new WavePropsSliders(sineWave, frequencySlider, sliderLabel);
    var slider2 = new WavePropsSliders(sineWave2, frequencySlider2, sliderLabel2);

    slider1.addListenerAccordingToTarget(WavePropsSliders.Target.FREQUENCY);
    slider2.addListenerAccordingToTarget(WavePropsSliders.Target.FREQUENCY);
    synthesisViewer.synthesizeForPair(slider1, slider2);
  }
}
