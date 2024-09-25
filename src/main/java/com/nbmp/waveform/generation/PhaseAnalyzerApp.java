/* (C)2024 */
package com.nbmp.waveform.generation;

import javafx.stage.Stage;

import com.nbmp.waveform.graph.GraphDashboard;
import com.nbmp.waveform.graph.SliderBox;
import com.nbmp.waveform.guides.GuideOptions;
import com.nbmp.waveform.guides.PhaseDifferenceGuide;
import com.nbmp.waveform.guides.SineWaveGuide;
import com.nbmp.waveform.models.SliderTarget;
import com.nbmp.waveform.ui_elements.WaveSlider;

public class PhaseAnalyzerApp {
  public static void analyzePhaseRelationships(
      Stage stage, double frequencyWave1, double frequencyWave2) {
    var graph = GraphDashboard.builder().totalTime(10).build();
    var sine1 = new SineWaveGuide(frequencyWave1, "Sine 1", GuideOptions.REGENERATION);
    var sine2 = new SineWaveGuide(frequencyWave2, "Sine 2", GuideOptions.REGENERATION);
    var phaseWave = new PhaseDifferenceGuide("Phase Sum", sine1, sine2, GuideOptions.REGENERATION);

    var gen = EfficientWaveGeneration.generatorOf(graph, sine1, sine2, phaseWave);
    var slider =
        new WaveSlider(
            "sine %s".formatted(frequencyWave2), sine1, SliderTarget.FREQUENCY, sine2, phaseWave);
    SliderBox.addSlider(slider.getLabel(), slider);
    //    phaseWave.makeReactive();
    var seriesList = gen.generate();
    graph.addSeries(seriesList).viewVBox(stage);
  }
}
