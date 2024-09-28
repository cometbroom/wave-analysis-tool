/* (C)2024 */
package com.nbmp.waveform.application;

import com.nbmp.waveform.model.generation.EfficientWaveGeneration;
import javafx.stage.Stage;

import com.nbmp.waveform.view.GraphDashboard;
import com.nbmp.waveform.controller.SliderBox;
import com.nbmp.waveform.model.guides.GuideOptions;
import com.nbmp.waveform.model.guides.PhaseDifferenceGuide;
import com.nbmp.waveform.model.guides.SineWaveGuide;
import com.nbmp.waveform.controller.SliderTarget;
import com.nbmp.waveform.controller.WaveSlider;
import org.springframework.stereotype.Component;

@Component
public class PhaseAnalyzerApp {
  public static void analyzePhaseRelationships(
      Stage stage, double frequencyWave1, double frequencyWave2) {
    var graph = GraphDashboard.builder().totalTime(10).build();
    var sine1 = new SineWaveGuide(frequencyWave1, "Sine 1", GuideOptions.REGENERATION);
    var sine2 = new SineWaveGuide(frequencyWave2, "Sine 2", GuideOptions.REGENERATION);
    var phaseWave = new PhaseDifferenceGuide("Phase Sum", sine1, sine2, GuideOptions.REGENERATION);
    var gen = new EfficientWaveGeneration(sine1, sine2, phaseWave);
    var slider = new WaveSlider("sine wave", sine1, SliderTarget.FREQUENCY, gen::regenerate);
    SliderBox.addSlider(slider.getLabel(), slider);
    //    phaseWave.makeReactive();
    gen.generateAndBindToGraph(graph);
    graph.viewVBox(stage);
  }
}
