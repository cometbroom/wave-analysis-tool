package com.nbmp.waveform.guides;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class SineWaveGuide extends WaveGuide {

    @Override
    protected Double computeWaveValue(Double t) {
        return amplitude * Math.sin(2 * Math.PI * frequency * t + phase);
    }
}
