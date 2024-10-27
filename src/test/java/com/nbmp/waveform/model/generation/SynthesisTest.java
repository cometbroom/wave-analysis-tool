/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.function.BiConsumer;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.ObjectFactory;

import com.nbmp.waveform.application.AppConstants;
import com.nbmp.waveform.controller.SmartObservable;
import com.nbmp.waveform.model.dto.ModulationActiveWaveProps;
import com.nbmp.waveform.model.pipeline.GenerationListeners;
import com.nbmp.waveform.model.pipeline.OutStream;
import com.nbmp.waveform.model.pipeline.StreamReactor;
import com.nbmp.waveform.model.utils.TestUtils;
import com.nbmp.waveform.model.waveform.Waveform;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.doubleThat;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.Mockito.*;

public abstract class SynthesisTest {
  static final int DURATION = 1;
  static final double MOD_INDEX = 0.0;
  static final int SAMPLE_COUNT = AppConstants.getSampleCount(DURATION);
  @Mock GenerationListeners persistentInitObservers;
  @Mock OutStream outStream;
  @Mock SmartObservable<Integer> clockStream;
  @Mock ObjectFactory<StreamReactor> reactor;
  @Mock StreamReactor streamReactor;
  @Mock Waveform wave1;
  @Mock Waveform wave2;
  @Mock ModulationActiveWaveProps waveProps;
  @Mock GenerationState stateMock;
  @Mock BiConsumer<Waveform, Waveform> modulationFunctionMock;

  //  AutoCloseable openedMocks;

  @BeforeEach
  public void setUp() {
    TestUtils.mockGenerationProps(stateMock, wave1, wave2, waveProps, MOD_INDEX, 0.0, 1.0);
    TestUtils.mockReactor(reactor, streamReactor, outStream, persistentInitObservers, clockStream);
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
