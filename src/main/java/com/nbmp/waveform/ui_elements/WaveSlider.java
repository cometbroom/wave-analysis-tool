/* (C)2024 */
package com.nbmp.waveform.ui_elements;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import com.nbmp.waveform.extras.Reactive;
import com.nbmp.waveform.extras.Sliderable;
import com.nbmp.waveform.models.SliderTarget;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WaveSlider extends Slider {
  private String name = "WaveSlider";
  private Label label = new Label("%s: ".formatted(name));
  private final Sliderable targetToSlider;
  private final SliderTarget targetParam;

  public WaveSlider(String name, Sliderable targetToSlider, SliderTarget targetParam) {
    this.setMin(0);
    this.setMax(20);
    this.targetToSlider = targetToSlider;
    this.targetParam = targetParam;
    this.setName("%s for %s".formatted(name, targetParam.name()));
    setSliderGeneralProps(1);
  }

  public WaveSlider(
      String name, Sliderable targetToSlider, SliderTarget targetParam, Reactive... affects) {
    this(name, targetToSlider, targetParam);
    for (Reactive affect : affects) {
      addListener(affect::regenerateSeries);
    }
  }

  private void setSliderGeneralProps(double defaultValue) {
    // Create the slider for frequency adjustment
    this.setShowTickLabels(true);
    this.setShowTickMarks(true);
    this.setMajorTickUnit(1);
    this.setMinorTickCount(5);
    this.setBlockIncrement(0.1);
    // Create a label to show the current frequency
    this.label = new Label("%s: %s hz".formatted(name, defaultValue));
    addListener();
  }

  private void addListener() {
    valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              double newValueDouble = newValue.doubleValue();
              label.setText(String.format("%s: %.2f Hz", name, newValueDouble));
              targetToSlider.updateValue(targetParam, newValueDouble);
            });
  }

  private void addListener(ChangeListener<Number> changeListener) {
    valueProperty().addListener(changeListener);
  }

  private void addListener(Runnable run) {
    valueProperty().addListener((observable, oldValue, newValue) -> run.run());
  }
}
