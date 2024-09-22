package com.nbmp.waveform.generation;

import com.nbmp.waveform.guides.Guide;
import com.nbmp.waveform.guides.SmartGuide;
import javafx.scene.chart.XYChart;

import java.util.List;

public interface Generator {
    default List<XYChart.Series<Number, Number>> generate(List<Guide> guides) {
        return List.of(new XYChart.Series<>());
    }
    default List<XYChart.Series<Number, Number>> generate(Guide guide) {
        return List.of(new XYChart.Series<>());
    }

    default void regenerate(SmartGuide guide) {}

}
