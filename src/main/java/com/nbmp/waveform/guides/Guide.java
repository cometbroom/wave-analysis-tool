/* (C)2024 */
package com.nbmp.waveform.guides;

import com.nbmp.waveform.generation.BufferedSeries;
import com.nbmp.waveform.generation.Generator;
import javafx.scene.chart.XYChart;

import com.nbmp.waveform.generation.EfficientWaveGeneration;

import lombok.Getter;
import lombok.Setter;
import org.jfree.data.xy.XYSeries;

@Getter
@Setter
public abstract class Guide {
  protected EfficientWaveGeneration generator;
  protected XYSeries series;
  protected boolean isInteractive = false;
  protected double currentValue = Double.NEGATIVE_INFINITY;

  public Guide() {
    this("Guide");
  }

  public Guide(String name) {
    //200 ms of buffer
    this(name, (int) Generator.SAMPLE_RATE / 5);
  }

  public Guide(String name, int bufferSize) {
    series = new BufferedSeries(name, bufferSize);
  }


  abstract Double compute(Double t, Double timeStep);

  public void addPoint(double t, double timeStep) {
    currentValue = compute(t, timeStep);
    series.add(t, currentValue);
  }

  public void bindForRegeneration(EfficientWaveGeneration generator) {
    this.generator = generator;
    this.isInteractive = true;
  }

  public void regenerateSeries() {
    if (isInteractive) {
      this.series.clear();
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
