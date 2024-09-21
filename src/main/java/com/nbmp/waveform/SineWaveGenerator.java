package com.nbmp.waveform;

import com.nbmp.waveform.models.WaveformData;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class SineWaveGenerator {
    private XYChart.Series<Number, Number> series;
    private final LineChart<Number, Number> lineChart;
    private String SINE_WAVE_LABEL = "Sine Wave";


    public SineWaveGenerator(NumberAxis xAxix, NumberAxis yAxix) {
        lineChart = new LineChart<>(xAxix, yAxix);
        lineChart.setTitle(SINE_WAVE_LABEL);

    }


    public LineChart<Number, Number> generate(WaveformData wd) {
        // Defining a series to display the sine wave
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(SINE_WAVE_LABEL);

        for (double t = 0; t < wd.getTotalTime(); t += wd.getTimeStep()) {
            series.getData().add(makeChartData(t, wd));
        }
        lineChart.getData().add(series);
        return lineChart;
    }

    private XYChart.Data<Number, Number> makeChartData(double t, WaveformData wd) {
        return new XYChart.Data<>(t, computeSineWaveValue(wd, t));
    }


    private double computeSineWaveValue(WaveformData wd, double t) {
        return wd.getAmplitude() * Math.sin(2 * Math.PI * wd.getFrequency() * t + wd.getPhase());
    }
}