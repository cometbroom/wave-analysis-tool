/* (C)2024 */
package com.nbmp.waveform.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nbmp.waveform.controller.ControllersState;
import com.nbmp.waveform.model.generation.output.BufferedOutputStream;
import com.nbmp.waveform.model.pipeline.GenerationListeners;
import com.nbmp.waveform.model.pipeline.OutStream;
import com.nbmp.waveform.model.pipeline.StreamReactor;
import com.nbmp.waveform.view.ChartView;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@RecordApplicationEvents
@ContextConfiguration(
    classes = {
      ConfigIT.class,
      OutStream.class,
      GenerationListeners.class,
      ControllersState.class,
      ChartView.class,
      BufferedOutputStream.class
    })
public class BaseIT {
  @Autowired private StreamReactor streamReactor;

  @Test
  public void contextLoads(@Autowired ControllersState controllersState) {
    verify(streamReactor, times(3)).onStart(any());
  }

  @Test
  public void reactorRuns(@Autowired StreamReactor streamReactor) {
    streamReactor.subscribe();
    verify(streamReactor, times(1)).subscribe();
  }
}
