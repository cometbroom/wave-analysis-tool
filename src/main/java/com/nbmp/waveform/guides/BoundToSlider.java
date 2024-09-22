package com.nbmp.waveform.guides;

import com.nbmp.waveform.generation.Generator;
import com.nbmp.waveform.models.SliderTarget;

import java.util.function.Consumer;

public interface BoundToSlider {
    Consumer<Double> recompute(SliderTarget target);
    void addSlider(String name, SliderTarget target, Generator generator);
}
