/* (C)2024 */
package com.nbmp.waveform.model.generation;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component("ChaosSelfMod")
public class ChaosSelfMod extends ChaosSynthesis {

  @PostConstruct
  public void init() {
    this.modulationFunction = this::singleSelfFM;
  }
}
