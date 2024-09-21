package com.nbmp.waveform.generation;

import com.nbmp.waveform.models.WaveformData;
import javafx.scene.chart.XYChart;
import lombok.Builder;

@Builder
public class SineWaveGenerator implements WaveGenerator {
    @Builder.Default
    private double amplitude = 1;
    @Builder.Default
    private double frequency = 440;
    @Builder.Default
    private double phase = 0;
    @Builder.Default
    private double timeStep = 0.01;
    @Builder.Default
    private double totalTime = 2;

    private final String SINE_WAVE_LABEL = "Sine Wave";

    public XYChart.Series<Number, Number> generate() {
        // Defining a series to display the sine wave
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(SINE_WAVE_LABEL);
        for (double t = 0; t < totalTime; t += timeStep) {
            series.getData().add(new XYChart.Data<>(t, computeSineWaveValue(t)));
        }
        return series;
    }


    private double computeSineWaveValue(double t) {
        return amplitude * Math.sin(2 * Math.PI * frequency * t + phase);
    }
}