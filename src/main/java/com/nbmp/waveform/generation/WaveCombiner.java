/* (C)2024 */
package com.nbmp.waveform.generation;

import java.util.List;
import java.util.function.BiFunction;

import com.nbmp.waveform.guides.SineWaveGuide;
import com.nbmp.waveform.utils.MathConstants;
import javafx.util.Pair;

import com.nbmp.waveform.graph.GraphDashboard;
import com.nbmp.waveform.models.SmartData;

public class WaveCombiner {
  public double frequency1 = 1, frequency2 = 0.9;

  public SmartData<Pair<Double, Double>> differenceMonitor(SmartData<Double> data1, SmartData<Double> data2, BiFunction<Double, Double, Double> comparisonMethod) {
    var differenceRegister = new SmartData<>(new Pair<>(0d, 0d), "delta");
      data1.addListener(
              t -> {
                  if (data2.get() != Double.NEGATIVE_INFINITY) {
                      differenceRegister.setValue(new Pair<>(t, comparisonMethod.apply(data1.get(), data2.get())));
                  }
              });
      data2.addListener(
              t -> {
                  if (data1.get() != Double.NEGATIVE_INFINITY) {
                      differenceRegister.setValue(new Pair<>(t, comparisonMethod.apply(data2.get(), data1.get())));
                  }
              });
      return differenceRegister;
  }

  public GraphDashboard drawOnGraph() {
    var graph = GraphDashboard.builder().build();
    var lineGraph = EventLineGenerator.builder().graph(graph).build();

    var efficientGens = new EfficientWaveGeneration(graph.getTimeStep(), graph.getTotalTime());
    var sine1 =
        SineWaveGuide.builder()
            .frequency(frequency1)
            .frequency(1)
            .build();
    var sine2 =
        SineWaveGuide.builder()
            .frequency(frequency2)
            .frequency(0.9)
            .build();
    var differenceMonitor = differenceMonitor(sine1.peakTime, sine2.peakTime, (a, b) -> b - a);

      differenceMonitor.addListener(
        (t) -> {
          boolean isNegative = t.getValue() < 0;
          double deltaPhiRadians = MathConstants.angularFrequency.apply(frequency1) * Math.abs(t.getValue()),
              deltaPhiDegrees = Math.toDegrees(deltaPhiRadians);
          deltaPhiDegrees = ((deltaPhiDegrees + 180) % 360) - 180;
          double deltaPhiGraphable = deltaPhiDegrees / 360;
          if (isNegative) deltaPhiGraphable = -deltaPhiGraphable;
          lineGraph.addPoint(t.getKey(), deltaPhiGraphable);
        });

    var seriesList = efficientGens.generate(List.of(sine1, sine2));

    graph.addSeries(seriesList).addSeries(lineGraph.getSeries());
    return graph;
  }

}
