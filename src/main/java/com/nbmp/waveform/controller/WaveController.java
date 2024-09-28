package com.nbmp.waveform.controller;

import com.nbmp.waveform.model.WaveformGenerator;
import com.nbmp.waveform.model.guides.SineWaveGuide;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class WaveController {
    public ComboBox waveformTypeComboBox;
    public ColorPicker colorPicker;
    public Label statusLabel;
    @FXML private Label sliderLabel;
    @FXML private LineChart<Number, Number> waveformChart;
    @FXML private Slider frequencySlider;
    @FXML private Button addWaveButton;
    private WaveService waveService = new WaveService();

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
        var guide = waveService.createGuide(type, frequency, amplitude);
        waveformChart.getData().add(guide.series());
        //Only first waveform has slider
        if (waves.isEmpty()) {
            frequencySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                guide.guide().setFrequency(newValue.doubleValue());
                sliderLabel.setText("Frequency: %.2f Hz".formatted(newValue.doubleValue()));
                guide.series().getData().clear();
                waveService.getPointsFor(guide);
            });
        }
        waves.add(guide);

    }
}
