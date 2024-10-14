/* (C)2024 */
package com.nbmp.waveform.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nbmp.waveform.model.generation.GenerationState;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@Service
public class SynthesisViewer {
  // Class is the only class autowiring GenerationState. Removing it will break the application
  // Will be using this class to simplify GenerationState in the future
  @Autowired private GenerationState genState;
}
