package com.nbmp.waveform.guides;

public interface Guide {
    default Double compute(Double t) {
        return 0.0;
    }
    Double compute(Double t, Double timeStep);
}
