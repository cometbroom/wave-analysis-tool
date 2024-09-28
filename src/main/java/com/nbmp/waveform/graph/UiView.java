package com.nbmp.waveform.graph;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;

public abstract class UiView {
    protected int width = 800, height = 600;
    protected double timeStep = 0.01, totalTime = 5;
   protected String xLabel = "Time (s)", yLabel = "Amplitude";
    private LineChart<Number, Number> lineChart = new LineChart<>(new NumberAxis(), new NumberAxis());

    public void start(Stage stage) {
        Scene scene = new Scene(lineChart, width, height);
        stage.setScene(scene);
        stage.show();
    }

    public void addSeries() {

    }
}
