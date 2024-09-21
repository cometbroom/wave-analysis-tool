package com.nbmp.waveform.application;

import com.nbmp.waveform.SineWaveGenerator;
import com.nbmp.waveform.models.WaveformData;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;
import javafx.util.Pair;

public class WaveformPlotter extends Application {

    private final String STAGE_TITLE = "Sine Wave";

    @Override
    public void start(Stage stage) {
        stage.setTitle(STAGE_TITLE);
        var xyPair = getXYAxis("Time (s)", "Amplitude");

        var wd = new WaveformData(1.0, 5.0, 0.0, 0.01, 2.0);
        SineWaveGenerator sineWaveGenerator = new SineWaveGenerator(xyPair.getKey(), xyPair.getValue());
        var lineChart = sineWaveGenerator.generate(wd);
        // Setting up the scene
        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Pair<NumberAxis, NumberAxis> getXYAxis(String xLabel, String yLabel) {
        final NumberAxis xAxis = new NumberAxis(), yAxis = new NumberAxis();
        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);

        return new Pair<>(xAxis, yAxis);
    }
}