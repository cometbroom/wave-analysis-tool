/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.function.BiConsumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import org.mockito.verification.VerificationMode;

import com.nbmp.waveform.application.AppConstants;
import com.nbmp.waveform.model.dto.ModulationActiveWaveProps;
import com.nbmp.waveform.model.dto.TimeSeries;
import com.nbmp.waveform.model.pipeline.GenerationListeners;
import com.nbmp.waveform.model.pipeline.OutStream;
import com.nbmp.waveform.model.pipeline.StreamReactor;
import com.nbmp.waveform.model.utils.TestUtils;
import com.nbmp.waveform.model.waveform.Waveform;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.doubleThat;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.Mockito.*;

public class SynthesisTest {
  static final int DURATION = 1;
  static final double MOD_INDEX = 0.0;
  static final int SAMPLE_COUNT = AppConstants.getSampleCount(DURATION);
  @InjectMocks StreamReactor streamReactor = new StreamReactor(SAMPLE_COUNT);
  @Mock Waveform waveform1;
  @Mock Waveform waveform2;
  @Mock ModulationActiveWaveProps waveProps;
  @Mock TimeSeries resultSerise;
  @Mock GenerationState stateMock;
  @Mock BiConsumer<Waveform, Waveform> modulationFunctionMock;
  @Mock GenerationListeners generationListenersMock;
  @Mock OutStream outStreamMock;
  @InjectMocks ChaosSynthesis chaosSynthesis;
  AutoCloseable openedMocks;

  @BeforeEach
  public void setUp() {
    streamReactor = Mockito.spy(new StreamReactor(SAMPLE_COUNT));
    openedMocks = MockitoAnnotations.openMocks(this);
    TestUtils.mockGenerationProps(stateMock, waveform1, waveform2, waveProps, MOD_INDEX, 0.0, 1.0);
    TestUtils.mockReactor(stateMock, streamReactor);
    chaosSynthesis.setModulationFunction(modulationFunctionMock);
  }

  @AfterEach
  public void tearDown() throws Exception {
    openedMocks.close();
  }

  protected void verifyStreamReactorAddOutputs(
      StreamReactor streamReactor, VerificationMode verificationMode) {
    verify(streamReactor, verificationMode)
        .addOutputs(
            intThat((x) -> both(greaterThanOrEqualTo(0)).and(lessThan(SAMPLE_COUNT)).matches(x)),
            doubleThat(
                (x) -> both(greaterThanOrEqualTo(-1.0)).and(lessThanOrEqualTo(1.0)).matches(x)),
            doubleThat(
                (x) -> both(greaterThanOrEqualTo(-1d)).and(lessThanOrEqualTo(1d)).matches(x)),
            doubleThat(
                (x) -> both(greaterThanOrEqualTo(-1d)).and(lessThanOrEqualTo(1d)).matches(x)));
  }
}
