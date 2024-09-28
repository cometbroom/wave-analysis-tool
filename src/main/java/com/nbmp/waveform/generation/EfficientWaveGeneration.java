/* (C)2024 */
package com.nbmp.waveform.generation;

import java.util.LinkedList;
import java.util.List;

import com.nbmp.waveform.graph.UiView;

import com.nbmp.waveform.graph.UiViewable;
import com.nbmp.waveform.guides.Guide;

import lombok.Getter;

@Getter
public class EfficientWaveGeneration extends Generator {
  protected List<Guide> allGuides = new LinkedList<>();
  private List<Guide> dynamicGuides = new LinkedList<>();

  public EfficientWaveGeneration(double timeStep, double totalTime, Guide... guides) {
    super(timeStep, totalTime);
    bindGuides(guides);
  }

  public EfficientWaveGeneration(Guide... guides) {
    bindGuides(guides);
  }

  public void generate() {
    for (double t = 0; t < totalTime; t += timeStep) {
      for (var guide : dynamicGuides) {
        guide.addPoint(t, timeStep);
      }
    }
    dynamicGuides = dynamicGuides.stream().filter(Guide::isInteractive).toList();
  }

  public void generateAndBindToGraph(UiViewable graph) {
    generate();
    allGuides.forEach(guide -> graph.addSeries(guide.getSeries()));
  }

  public void populateSeriesList(Guide guide) {
    dynamicGuides.add(guide);
    allGuides.add(guide);
    guide.bindForRegeneration(this);
  }

  public void regenerate() {
    dynamicGuides.forEach(
        guide -> {
          if (guide.isInteractive()) guide.getSeries().clear();
        });
    generate();
  }

  private void bindGuides(Guide[] guides) {
    for (var guide : guides) {
      populateSeriesList(guide);
    }
  }
}
