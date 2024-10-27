/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nbmp.waveform.model.dto.SynthesisMode;
import com.nbmp.waveform.model.generation.synth.BaseSynthesis;

@Configuration
public class GenConfig {
  @Bean
  public Map<SynthesisMode, BaseSynthesis> processorMap(ApplicationContext context) {
    var beans = context.getBeansOfType(BaseSynthesis.class);
    Map<SynthesisMode, BaseSynthesis> returnMap = new HashMap<>();
    beans.forEach((k, v) -> returnMap.put(SynthesisMode.valueOf(k), v));
    return returnMap;
  }
}
