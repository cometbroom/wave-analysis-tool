/* (C)2024 */
package com.nbmp.waveform.model;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

import com.nbmp.waveform.model.pipeline.StreamReactor;

public class ConfigIT {
  @Bean
  public StreamReactor streamReactor() {
    return Mockito.mock(StreamReactor.class);
  }
}
