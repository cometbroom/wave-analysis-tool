/* (C)2024 */
package com.nbmp.waveform.generation;

import com.nbmp.waveform.graph.JfreeGraph;
import com.nbmp.waveform.graph.UiView;
import com.nbmp.waveform.graph.UiViewable;
import javafx.stage.Stage;

import com.nbmp.waveform.graph.SliderBox;
import com.nbmp.waveform.guides.GuideOptions;
import com.nbmp.waveform.guides.PhaseDifferenceGuide;
import com.nbmp.waveform.guides.SineWaveGuide;
import com.nbmp.waveform.models.SliderTarget;
import com.nbmp.waveform.ui_elements.WaveSlider;

public class PhaseAnalyzerApp {
  public static void analyzePhaseRelationships(
      Stage stage, double frequencyWave1, double frequencyWave2) {
    UiViewable graph = new JfreeGraph();
    var sine1 = new SineWaveGuide(frequencyWave1, "Sine 1", GuideOptions.REGENERATION);
    var sine2 = new SineWaveGuide(frequencyWave2, "Sine 2", GuideOptions.REGENERATION);
    var phaseWave = new PhaseDifferenceGuide("Phase Sum", sine1, sine2, GuideOptions.REGENERATION);
    var gen = new EfficientWaveGeneration(sine1, sine2, phaseWave);
    var slider =
        new WaveSlider(
            "sine wave", sine1, SliderTarget.FREQUENCY, sine2, phaseWave);
    SliderBox.addSlider(slider.getLabel(), slider);
    //    phaseWave.makeReactive();
    gen.generateAndBindToGraph(graph);
    graph.start(stage);
  }
}
