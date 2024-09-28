package com.nbmp.waveform.model;

public class SinewaveGenerator implements WaveformGenerator {
    private double frequency;
    private double amplitude;
    private double phase;

    // Constructor and methods implementation

    @Override
    public double[] generate(int sampleRate, double duration) {
        int sampleCount = (int) (sampleRate * duration);
        double[] data = new double[sampleCount];
        for (int i = 0; i < sampleCount; i++) {
            data[i] = amplitude * Math.sin(2 * Math.PI * frequency * i / sampleRate + phase);
        }
        return data;
    }

    @Override
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    @Override
    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    @Override
    public void setPhase(double phase) {
        this.phase = phase;
    }
    // Setters and getters
}
