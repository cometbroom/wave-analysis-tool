/* (C)2024 */
package com.nbmp.waveform.graph;

import java.util.LinkedHashMap;
import java.util.Map;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import com.nbmp.waveform.extras.Sliderable;
import com.nbmp.waveform.models.SliderTarget;
import com.nbmp.waveform.ui_elements.WaveSlider;

import lombok.Getter;

@Getter
public class SliderBox {
  public static Map<Label, Slider> sliders = new LinkedHashMap<>();

  public static void addSlider(Label label, Slider slider) {
    sliders.put(label, slider);
  }

  public static void addSlider(String name, SliderTarget targetParam, Sliderable targetObject) {
    var slider = new WaveSlider(targetObject, targetParam);
    slider.setName("%s for %s".formatted(name, targetParam.name()));
    addSlider(slider.getLabel(), slider);
  }
}
