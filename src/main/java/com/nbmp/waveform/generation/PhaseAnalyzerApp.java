/* (C)2024 */
package com.nbmp.waveform.generation;

import java.util.List;
import javafx.stage.Stage;

import com.nbmp.waveform.graph.GraphDashboard;
import com.nbmp.waveform.graph.SliderBox;
import com.nbmp.waveform.guides.SineWaveGuide;
import com.nbmp.waveform.models.SliderTarget;

public class PhaseAnalyzerApp {

  public static void analyzePhaseRelationships(Stage stage) {
    analyzePhaseRelationships(stage, 1, 0.9);
  }

  public static void analyzePhaseRelationships(
      Stage stage, double frequencyWave1, double frequencyWave2) {
    var graph = GraphDashboard.builder().totalTime(10).build();
    var sine1 = new SineWaveGuide(2);
    var sine2 = new SineWaveGuide(5);
    //    var phaseWave = new PhaseDifferenceGuide(sine1, sine2);

    sine2.addFrequencyChange(4, 0.5);

    var gen = EfficientWaveGeneration.generatorOf(graph, sine1, sine2);
    SliderBox.addSlider("sine %s".formatted(frequencyWave2), SliderTarget.FREQUENCY, sine1);
    sine1.setInteractive(true);
    //    phaseWave.makeReactive();
    var seriesList = gen.generateWithWrappers(List.of(sine1, sine2));
    graph.addSeries(seriesList).viewVBox(stage);
  }
}
