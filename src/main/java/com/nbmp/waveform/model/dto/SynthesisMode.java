/* (C)2024 */
package com.nbmp.waveform.model.dto;

import java.util.Arrays;
import java.util.List;

import com.nbmp.waveform.model.generation.synth.ChaosSelfMod;
import com.nbmp.waveform.model.generation.synth.ChaosSynthesis;
import com.nbmp.waveform.model.generation.synth.FMSynthesis;
import com.nbmp.waveform.model.generation.synth.IndependentSynthesis;
import com.nbmp.waveform.model.generation.synth.Synthesis;

import lombok.Getter;

@Getter
public enum SynthesisMode {
  IndependentSynthesis("Independent", 0.0, IndependentSynthesis.class),
  ChaosSynthesis("Chaos Two-Way FM", 0.3, ChaosSynthesis.class),
  ChaosSelfMod("Chaos Independent Self-Modulation", 0.2, ChaosSelfMod.class),
  FMSynthesis("FM Modulation", 2, FMSynthesis.class);
  private final double modIndex;
  private final String title;
  private final Class<? extends Synthesis> className;

  SynthesisMode(String title, double modIndex, Class<? extends Synthesis> className) {
    this.modIndex = modIndex;
    this.title = title;
    this.className = className;
  }

  public static List<String> getNames() {
    return Arrays.stream(SynthesisMode.class.getEnumConstants()).map(Enum::name).toList();
  }
}
