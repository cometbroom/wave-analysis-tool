package com.nbmp.waveform.graph;

import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.jfree.data.xy.XYSeries;

public interface UiViewable {
    void start(Stage stage);
    default UiViewable addSeries(XYChart.Series<Number, Number> series) {
        return this;
    }
    default UiViewable addSeries(XYSeries series) {
        return this;
    }


}
