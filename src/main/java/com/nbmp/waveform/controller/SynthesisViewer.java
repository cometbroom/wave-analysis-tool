package com.nbmp.waveform.controller;

import com.nbmp.waveform.model.dto.BiTimeSeries;
import com.nbmp.waveform.model.dto.WavePropsSliders;
import com.nbmp.waveform.model.generation.Synthesis;
import com.nbmp.waveform.view.WavesRegister;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class SynthesisViewer {
    private final WavesRegister wave1, wave2;
    private final Synthesis synthesis;
    private final int duration;

    public void synthesizeForPair(WavePropsSliders slider1, WavePropsSliders slider2) {
        regenSeriesData(duration);
        setUpdateTask(slider1);
        setUpdateTask(slider2);
    }

    private void regenSeriesData(int duration) {
        var newData = synthesis.compute(duration);
        wave1.refreshData(newData.timeAmplitude1());
        wave2.refreshData(newData.timeAmplitude2());
    }

    private void setUpdateTask(WavePropsSliders slider) {
        slider.setUpdateTask((newValue) -> {
            var newData = synthesis.compute(duration);
            wave1.refreshData(newData.timeAmplitude1());
            wave2.refreshData(newData.timeAmplitude2());
        });
    }
}
