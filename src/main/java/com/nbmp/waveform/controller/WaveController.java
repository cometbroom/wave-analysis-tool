package com.nbmp.waveform.controller;

import com.nbmp.waveform.model.WaveformGenerator;
import com.nbmp.waveform.model.guides.SineWaveGuide;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class WaveController {
    @FXML private LineChart<Number, Number> waveformChart;
    @FXML private Slider frequencySlider;
    @FXML private Button addWaveButton;

    private WaveformGenerator generator = new WaveformGenerator();
    private List<WavesRegister> waves = new LinkedList<>();


    public enum WaveType {
        SINE, SQUARE, TRIANGLE, SAWTOOTH
    }

    public void createButton() {
    }

    @FXML public void initialize() {
        createGuide(WaveType.SINE, 5, 1);
    }

    public void createGuide(WaveType type, double frequency, double amplitude) {
        var series = new XYChart.Series<Number, Number>();
        series.setName("guide");
        waveformChart.getData().add(series);
        WavesRegister guide = switch (type) {
      case SINE -> new WavesRegister(new SineWaveGuide(frequency, amplitude), series);
            case SQUARE, TRIANGLE, SAWTOOTH ->
                throw new IllegalArgumentException("Wave type not supported");
        };
        //Only first waveform has slider
        if (waves.isEmpty()) {
            frequencySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                guide.guide().setFrequency(newValue.doubleValue());
                guide.series().getData().clear();
                getPointsFor(guide);
            });
        }
        waves.add(guide);

    }

    private void getPointsFor(WavesRegister wave) {
        var data = generator.generate(wave.guide(), 0, 1);
            for (double[] point : data) {
                wave.series().getData().add(new XYChart.Data<>(point[0], point[1]));
            }
    }

    private void getPoints() {
        for (var wave : waves) {
            var data = generator.generate(wave.guide(), 0, 1);
            for (double[] point : data) {
                wave.series().getData().add(new XYChart.Data<>(point[0], point[1]));
            }
        }
    }
}
