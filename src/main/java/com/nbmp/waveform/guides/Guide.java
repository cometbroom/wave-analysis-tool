/* (C)2024 */
package com.nbmp.waveform.guides;

import javafx.scene.chart.XYChart;

import com.nbmp.waveform.generation.EfficientWaveGeneration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Guide {
  protected EfficientWaveGeneration generator;
  protected XYChart.Series<Number, Number> series;
  protected boolean isInteractive = false;
  protected String LABEL = "Guide";
  protected double currentValue = Double.NEGATIVE_INFINITY;

  public Guide() {
    series = new XYChart.Series<>();
    series.setName(LABEL);
  }

  abstract Double compute(Double t, Double timeStep);

  public void addPoint(Double t, Double timeStep) {
    currentValue = compute(t, timeStep);
    series.getData().add(new XYChart.Data<>(t, currentValue));
  }

  public void bindForRegeneration(EfficientWaveGeneration generator) {
    this.generator = generator;
    this.isInteractive = true;
  }

  public void regenerateSeries() {
    if (isInteractive) {
      this.series.getData().clear();
      this.generator.regenerate();
    }
  }

  protected void setupOptions(GuideOptions[] options) {
    for (GuideOptions option : options) {
      switch (option) {
        case REGENERATION -> this.isInteractive = true;
      }
    }
  }
}
