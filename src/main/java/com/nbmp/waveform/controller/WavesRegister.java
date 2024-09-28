package com.nbmp.waveform.controller;

import com.nbmp.waveform.model.guides.WaveGuide;
import javafx.scene.chart.XYChart;

public record WavesRegister(WaveGuide guide, XYChart.Series<Number, Number> series) { }
