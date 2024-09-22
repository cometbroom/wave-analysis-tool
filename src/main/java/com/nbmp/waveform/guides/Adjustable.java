package com.nbmp.waveform.guides;

public interface Adjustable {
    void addPoint(Double t, Double timeStep);
    void regenerateSeries();
}
