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
import com.nbmp.waveform.model.dto.*;
import com.nbmp.waveform.model.generation.synth.ChaosSynthesis;
import com.nbmp.waveform.model.utils.TestUtils;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ChaosSynthesisTest extends SynthesisTest {

  @InjectMocks ChaosSynthesis chaosSynthesis;

  @BeforeEach
  public void setUp() {
    chaosSynthesis.setModulationFunction(modulationFunctionMock);
    TestUtils.mockGenerationProps(stateMock, wave1, wave2, waveProps, MOD_INDEX, 0.0, 1.0);
    TestUtils.mockReactor(reactor, streamReactor, outStream, persistentInitObservers, clockStream);
  }

  @Test
  public void computeReturnsCorrectValues_extensive() {
    chaosSynthesis.compute(DURATION);
    // x2 for fx processing run TODO: remove x2 when Rod is implemented
    ArgumentCaptor<SmartObservable.Observer<Integer>> captor =
        ArgumentCaptor.forClass(SmartObservable.Observer.class);
    verify(streamReactor, atLeastOnce()).addObserver(captor.capture());
    var computeFunc = captor.getValue();

    computeFunc.onUpdate(0);
    computeFunc.onUpdate(1);
    verifyStreamReactorAddOutputs(outputStream, times(2));
  }

  @Test
  public void computeHandlesZeroDuration() throws Exception {
    try (MockedStatic<AppConstants> appConstants = mockStatic(AppConstants.class)) {
      appConstants.when(AppConstants::getSampleCount).thenReturn(0);
      chaosSynthesis.compute(0);
      verify(streamReactor, atLeastOnce()).addObserver(any());
      verify(streamReactor, times(1)).run(eq(0), eq(0));
      verify(streamReactor, never()).addOutputs(anyInt(), anyDouble(), anyDouble(), anyDouble());
    }
  }

  @Test
  public void computeHandlesNegativeDuration() {
    int duration = -10;
    try (MockedStatic<AppConstants> appConstants = mockStatic(AppConstants.class)) {
      appConstants.when(AppConstants::getSampleCount).thenReturn(duration);
      chaosSynthesis.compute(duration);
      verify(streamReactor, never()).addOutputs(anyInt(), anyDouble(), anyDouble(), anyDouble());
    }
  }
}
