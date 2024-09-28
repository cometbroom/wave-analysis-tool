package com.nbmp.waveform.model;

public interface WaveformGenerator {
    double[] generate(int sampleRate, double duration);
    void setFrequency(double frequency);
    void setAmplitude(double amplitude);
    void setPhase(double phase);
}