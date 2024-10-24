/* (C)2024 */
package com.nbmp.waveform.model.dto;

import java.util.Arrays;
import java.util.List;

import com.nbmp.waveform.model.generation.*;

import lombok.Getter;

@Getter
public enum SynthesisMode {
  INDEPENDENT("Independent", 0.0, IndependentSynthesis.class),
  CHAOS_TWO_WAY_FM("Chaos Two-Way FM", 0.3, ChaosSynthesis.class),
  CHAOS_INDEPENDENT_SELF_MOD_FM("Chaos Independent Self-Modulation", 0.2, ChaosSelfMod.class),
  FM_WAVE1MOD_WAVE2CARRIER("FM Modulation", 2, FMSynthesis.class);
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
