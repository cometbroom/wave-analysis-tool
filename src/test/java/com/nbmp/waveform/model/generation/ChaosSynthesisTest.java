/* (C)2024 */
package com.nbmp.waveform.model.generation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.nbmp.waveform.application.AppConstants;
import com.nbmp.waveform.model.dto.*;
import com.nbmp.waveform.model.pipeline.StreamReactor;
import com.nbmp.waveform.model.utils.TestUtils;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ChaosSynthesisTest extends SynthesisTest {
  @Test
  public void computeReturnsCorrectValues_extensive() {
    chaosSynthesis.compute(DURATION);
    // x2 for fx processing run TODO: remove x2 when Rod is implemented
    verifyStreamReactorAddOutputs(streamReactor, times(SAMPLE_COUNT * 2));
  }

  @Test
  public void computeHandlesZeroDuration() throws Exception {
    openedMocks.close();
    streamReactor = Mockito.spy(new StreamReactor(SAMPLE_COUNT));
    openedMocks = MockitoAnnotations.openMocks(this);
    TestUtils.mockGenerationProps(stateMock, waveform1, waveform2, waveProps, MOD_INDEX, 0.0, 1.0);
    TestUtils.mockReactor(stateMock, streamReactor);
    try (MockedStatic<AppConstants> appConstants = mockStatic(AppConstants.class)) {
      appConstants.when(AppConstants::getSampleCount).thenReturn(0);
      chaosSynthesis.compute(0);
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
