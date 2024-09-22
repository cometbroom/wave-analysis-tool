package com.nbmp.waveform.guides;

import com.nbmp.waveform.models.SmartData;
import com.nbmp.waveform.utils.GlobalUtils;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class WaveGuide implements Guide {
    @Builder.Default protected double amplitude = 1;
    @Builder.Default protected double frequency = 440;
    @Builder.Default protected double phase = 0;
    @Builder.Default protected String WAVE_LABEL = "Wave";

    @Builder.Default private Double maxValue = 0.0;
    @Builder.Default public SmartData<Double> peakTime = new SmartData<>(Double.NEGATIVE_INFINITY, GlobalUtils.makeCountLabel("WaveGuide-peakTime"));

    @Override
    public Double compute(Double t, Double timeStep) {
        double computedValue = computeWaveValue(t);
        if (computedValue > maxValue && computedValue > computeWaveValue(t + timeStep)) {
            maxValue = computedValue;
            peakTime.setValue(t);
        }
        return computedValue;
    }

    protected Double computeWaveValue(Double t) {
        return 0.0;
    }
}
