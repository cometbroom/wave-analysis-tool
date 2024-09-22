package com.nbmp.waveform.ui_elements;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

@Getter
@Setter
public class WaveSlider extends Slider {
    private String name = "WaveSlider";
    private Label label = new Label("%s: ".formatted(name));
    private final Consumer<Double> sliderChanged;

    public WaveSlider(Consumer<Double> sliderChanged) {
        this.setMin(0);
        this.setMax(20);
        this.sliderChanged = sliderChanged;
        setSliderGeneralProps(1);
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
        valueProperty().addListener((observable, oldValue, newValue) -> {
            double newValueDouble = newValue.doubleValue();
            label.setText(String.format("%s: %.2f Hz", name, newValueDouble));
            sliderChanged.accept(newValueDouble);
        });
    }

    private void addListener(ChangeListener<Number> changeListener) {
        valueProperty().addListener(changeListener);
    }
}
