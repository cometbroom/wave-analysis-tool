/* (C)2024 */
package com.nbmp.waveform.controller;

import javafx.scene.chart.LineChart;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nbmp.waveform.model.generation.ChaosSynthesis;
import com.nbmp.waveform.model.generation.IndependentSynthesis;
import com.nbmp.waveform.model.generation.Synthesis;
import com.nbmp.waveform.model.generation.SynthesisMode;
import com.nbmp.waveform.view.SynthesisViewer;
import com.nbmp.waveform.view.WavesRegister;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Scope("singleton")
@Component
public class ControllersState {
  private WavesRegister waveform1;
  private WavesRegister waveform2;
  private Synthesis synthesis;
  private GraphController graphController;
  private LineChart<Number, Number> waveformChart;
  private LineChart<Number, Number> waveformChart2;
  private SynthesisViewer synthesisViewer;
  private SynthesisMode synthesisMode;

  public static ControllersState createInstance(WavesRegister waveform1, WavesRegister waveform2) {
    var instance = new ControllersState();
    instance.waveform1 = waveform1;
    instance.waveform2 = waveform2;
    instance.synthesis = new IndependentSynthesis(waveform1, waveform2);
    instance.synthesisViewer = new SynthesisViewer(waveform1, waveform2, instance.synthesis);
    return instance;
  }

  public void changeSynthesisMode(SynthesisMode mode) {
    synthesisMode = mode;
    Synthesis synthesisNew =
        switch (mode) {
          case INDEPENDENT -> new IndependentSynthesis(waveform1, waveform2);
          case CHAOS -> new ChaosSynthesis(waveform1, waveform2);
        };
    synthesisViewer.setSynthesis(synthesisNew);
  }

  public void resynthesize() {
    synthesisViewer.synthesizeForPair();
  }
}
