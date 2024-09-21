package com.nbmp.waveform.models;

import lombok.Getter;

@Getter
public class WaveformData {
    private final double amplitude;
    private final double frequency;
    private final double phase;
    private final double timeStep;
    private final double totalTime;

    public WaveformData(double amplitude, double frequency, double phase, double timeStep, double totalTime) {
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.phase = phase;
        this.timeStep = timeStep;
        this.totalTime = totalTime;
    }
}
