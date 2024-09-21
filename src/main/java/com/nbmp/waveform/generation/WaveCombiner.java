/* (C)2024 */
package com.nbmp.waveform.generation;

import com.nbmp.waveform.graph.GraphDashboard;

public class WaveCombiner {

  public void init() {
    var graph = GraphDashboard.builder().build();

    var sine1 = SineWaveGenerator.builder().graph(graph).frequency(1).build();
    var sine2 = SineWaveGenerator.builder().graph(graph).frequency(0.9).build();
  }
}
