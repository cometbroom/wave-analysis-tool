/* (C)2024 */
package com.nbmp.waveform.generation;

import java.util.function.Function;
import javafx.util.Pair;

import com.nbmp.waveform.graph.GraphDashboard;
import com.nbmp.waveform.models.SmartData;

public class WaveCombiner {

  public Function<Double, Double> angularFrequency = (f) -> 2 * Math.PI * f;
  public SmartData<Double> peak1, peak2;
  public SmartData<Pair<Double, Double>> deltaTime =
      new SmartData<>(new Pair<>(0d, 0d), "deltaTime");

  public double frequency1 = 1, frequency2 = 0.9;

  public GraphDashboard drawOnGraph() {
    var graph = GraphDashboard.builder().build();
    var lineGraph = EventLineGenerator.builder().graph(graph).build();

    var efficientGens = new EfficientWaveGeneration(graph.getTimeStep(), graph.getTotalTime());
    var sine1 =
        SineWaveGenerator.builder()
            .frequency(frequency1)
            .graph(graph)
            .frequency(1)
            .build();
    var sine2 =
        SineWaveGenerator.builder()
            .frequency(frequency2)
            .graph(graph)
            .frequency(0.9)
            .build();
    peak1 = sine1.peakTime;
    peak2 = sine2.peakTime;
    efficientGens.addGenerators(sine1, sine2);

    deltaTime.addListener(
        (t) -> {
          boolean isNegative = t.getValue() < 0;
          double deltaPhiRadians = angularFrequency.apply(frequency1) * Math.abs(t.getValue()),
              deltaPhiDegrees = Math.toDegrees(deltaPhiRadians);
          deltaPhiDegrees = ((deltaPhiDegrees + 180) % 360) - 180;
          double deltaPhiGraphable = deltaPhiDegrees / 360;
          if (isNegative) deltaPhiGraphable = -deltaPhiGraphable;
          lineGraph.addPoint(t.getKey(), deltaPhiGraphable);
        });
    attachEventToPeakTime(peak1, peak2);
    attachEventToPeakTime(peak2, peak1);

    var seriesList = efficientGens.generate();

    graph.addSeries(seriesList).addSeries(lineGraph.getSeries());
    return graph;
  }

  private void attachEventToPeakTime(
      SmartData<Double> peakTime, SmartData<Double> peakTimeOther) {
    peakTime.addListener(
        t -> {
          if (peakTimeOther.get() != Double.NEGATIVE_INFINITY) {
            deltaTime.setValue(new Pair<>(t, peakTimeOther.get() - peakTime.get()));
          }
        });
  }
}
