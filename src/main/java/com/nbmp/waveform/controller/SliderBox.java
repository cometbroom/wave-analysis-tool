/* (C)2024 */
package com.nbmp.waveform.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import lombok.Getter;

@Getter
public class SliderBox {
  public static Map<Label, Slider> sliders = new LinkedHashMap<>();

  public static void addSlider(Label label, Slider slider) {
    sliders.put(label, slider);
  }
}
