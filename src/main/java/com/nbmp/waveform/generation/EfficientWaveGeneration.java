/* (C)2024 */
package com.nbmp.waveform.generation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.chart.XYChart;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EfficientWaveGeneration {
  List<Generator> generators = new ArrayList<>();
  private final double timeStep, totalTime;

  public void addGenerator(Generator generator) {
    generators.add(generator);
  }

  public void addGenerators(Generator... generators) {
    this.generators.addAll(Arrays.asList(generators));
  }

  public List<XYChart.Series<Number, Number>> generate() {
    for (double t = 0; t < totalTime; t += timeStep) {
      for (Generator generator : generators) {
        generator.getSeries().setName(generator.getName());
        generator
            .getSeries()
            .getData()
            .add(new XYChart.Data<>(t, generator.getComputeFunction().apply(t)));
        for (var eventRun : generator.getEventRuns()) {
          eventRun.accept(t);
        }
      }
    }
    return this.generators.stream().map(Generator::getSeries).toList();
  }
}
