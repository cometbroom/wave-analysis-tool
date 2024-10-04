/* (C)2024 */
package com.nbmp.waveform.controller;

import javafx.scene.chart.XYChart;

import com.nbmp.waveform.model.guides.WaveGuide;

public record WavesRegister(WaveGuide guide, XYChart.Series<Number, Number> series) {}
