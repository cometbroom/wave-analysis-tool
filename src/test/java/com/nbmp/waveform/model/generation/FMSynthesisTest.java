/* (C)2024 */
package com.nbmp.waveform.model.generation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.nbmp.waveform.application.AppConstants;
import com.nbmp.waveform.controller.SmartObservable;
import com.nbmp.waveform.model.generation.synth.FMSynthesis;
import com.nbmp.waveform.model.utils.TestUtils;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FMSynthesisTest extends SynthesisTest {
  @InjectMocks FMSynthesis fmSynthesis;

  @BeforeEach
  public void setUp() {
    TestUtils.mockGenerationProps(stateMock, wave1, wave2, waveProps, MOD_INDEX, 0.0, 1.0);
    TestUtils.mockReactor(reactor, streamReactor, outStream, persistentInitObservers, clockStream);
  }

  @Test
  public void computeReturnsNonNullBiTimeSeries() {
    ArgumentCaptor<SmartObservable.Observer<Integer>> captor =
        ArgumentCaptor.forClass(SmartObservable.Observer.class);
    fmSynthesis.compute(DURATION);
    verify(streamReactor, atLeastOnce()).addObserver(captor.capture());
    captor.getValue().onUpdate(0);
    verifyStreamReactorAddOutputs(outputStream, times(1));
  }

  @Test
  public void computeReturnsCorrectBiTimeSeries() {
    int duration = 1;
    ArgumentCaptor<SmartObservable.Observer<Integer>> captor =
        ArgumentCaptor.forClass(SmartObservable.Observer.class);
    fmSynthesis.compute(duration);
    verify(streamReactor, atLeastOnce()).addObserver(captor.capture());
    captor.getValue().onUpdate(0);
    verify(outputStream, times(1)).addOutputs3Channel(eq(0), eq(0.0), eq(1.0), eq(1.0));
  }

  @Test
  public void computeHandlesZeroDuration() throws Exception {
    int duration = 0;
    try (MockedStatic<AppConstants> appConstants = mockStatic(AppConstants.class)) {
      appConstants.when(AppConstants::getSampleCount).thenReturn(duration);
      fmSynthesis.compute(duration);
      verify(streamReactor, never()).addOutputs(anyInt(), anyDouble(), anyDouble(), anyDouble());
    }
  }
}
