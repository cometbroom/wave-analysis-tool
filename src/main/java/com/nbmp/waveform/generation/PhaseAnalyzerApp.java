/* (C)2024 */
package com.nbmp.waveform.generation;

import javafx.stage.Stage;

import com.nbmp.waveform.graph.GraphDashboard;
import com.nbmp.waveform.graph.SliderBox;
import com.nbmp.waveform.guides.PhaseDifferenceGuide;
import com.nbmp.waveform.guides.SineWaveGuide;
import com.nbmp.waveform.guides.WaveOptions;
import com.nbmp.waveform.models.SliderTarget;
import com.nbmp.waveform.ui_elements.WaveSlider;

public class PhaseAnalyzerApp {
  public static void analyzePhaseRelationships(
      Stage stage, double frequencyWave1, double frequencyWave2) {
    var graph = GraphDashboard.builder().totalTime(10).build();
    var sine1 = new SineWaveGuide(frequencyWave1, WaveOptions.REGENERATION);
    var sine2 = new SineWaveGuide(frequencyWave2, WaveOptions.REGENERATION);
    var phaseWave = new PhaseDifferenceGuide(sine1, sine2, WaveOptions.REGENERATION);

    sine2.addFrequencyChange(frequencyWave1, 0.5);

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
