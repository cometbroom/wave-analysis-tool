package com.nbmp.waveform.graph;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class SliderBox {
    public static Map<Label, Slider> sliders = new LinkedHashMap<>();

    public static void addSlider(Label label, Slider slider) {
        sliders.put(label, slider);
    }
}
