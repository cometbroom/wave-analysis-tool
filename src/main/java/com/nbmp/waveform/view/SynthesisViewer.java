/* (C)2024 */
package com.nbmp.waveform.view;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nbmp.waveform.controller.WaveController;
import com.nbmp.waveform.model.generation.GenerationState;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@Service
public class SynthesisViewer {
  @Autowired private GenerationState genState;

  public Consumer<Double> recomputeRunner() {
    return (newValue) -> genState.regenSeriesData(WaveController.duration.get());
  }
}
