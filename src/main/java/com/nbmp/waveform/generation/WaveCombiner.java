/* (C)2024 */
package com.nbmp.waveform.generation;

import java.util.function.Function;
import javafx.util.Pair;

import com.nbmp.waveform.graph.GraphDashboard;
import com.nbmp.waveform.models.SmartData;

public class WaveCombiner {

  public Function<Double, Double> angularFrequency = (f) -> 2 * Math.PI * f;
  public SmartData<Double> peak1 = new SmartData<>(Double.NEGATIVE_INFINITY, "peakTime1");
  public SmartData<Double> peak2 = new SmartData<>(Double.NEGATIVE_INFINITY, "peakTime2");
  public SmartData<Pair<Double, Double>> deltaTime =
      new SmartData<>(new Pair<>(0d, 0d), "deltaTime");

  public double frequency1 = 1, frequency2 = 0.9;

  public GraphDashboard drawOnGraph() {
    var graph = GraphDashboard.builder().build();
    var lineGraph = EventLineGenerator.builder().graph(graph).build();

    var efficientGens = new EfficientWaveGenerator(graph.getTimeStep(), graph.getTotalTime());
    var sine1 =
        SineWaveGenerator.builder()
            .peakTime(peak1)
            .frequency(frequency1)
            .peakTime(peak1)
            .graph(graph)
            .frequency(1)
            .build();
    var sine2 =
        SineWaveGenerator.builder()
            .peakTime(peak2)
            .frequency(frequency2)
            .peakTime(peak2)
            .graph(graph)
            .frequency(0.9)
            .build();
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
    peak1 = attachEventToPeakTime(peak1, peak2);
    peak2 = attachEventToPeakTime(peak2, peak1);

    var seriesList = efficientGens.generate();

    graph.addSeries(seriesList).addSeries(lineGraph);
    return graph;
  }

  private SmartData<Double> attachEventToPeakTime(
      SmartData<Double> peakTime, SmartData<Double> peakTimeOther) {
    peakTime.addListener(
        t -> {
          if (peakTimeOther.get() != Double.NEGATIVE_INFINITY) {
            deltaTime.setValue(new Pair<>(t, peakTimeOther.get() - peakTime.get()));
          }
        });
    return peak1;
  }
}
