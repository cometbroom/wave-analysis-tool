package com.nbmp.waveform.model.generation;

import com.nbmp.waveform.model.dto.BiTimeSeries;

public interface Synthesis {
    BiTimeSeries compute(int duration);
}
