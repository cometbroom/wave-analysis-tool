package com.nbmp.waveform.generation;


import javafx.scene.chart.XYChart;
import lombok.Builder;

public interface WaveGenerator {

    public XYChart.Series<Number, Number> generate();


}
