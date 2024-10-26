/* (C)2024 */
package com.nbmp.waveform.model.pipeline;

import javafx.util.Pair;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.MoreObjects;
import com.nbmp.waveform.controller.SmartObservable;

import lombok.Getter;
import lombok.experimental.Delegate;

@Getter
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class OutStream {
  @Delegate private SmartObservable<Pair<Integer, Output>> stream;

  public OutStream() {
    this.stream = new SmartObservable<>(new Pair<>(0, new Output(0.0, 0.0, 0.0)));
  }

  public Output getOutput() {
    // Get stream value and then pair value
    return stream.getValue().getValue();
  }

  public double channel1() {
    return getOutput().channel1();
  }

  public double channel2() {
    return getOutput().channel2();
  }

  public double channel3() {
    return getOutput().channel3();
  }

  public void setValue(int i, double channel1, double channel2, double channel3) {
    stream.setValue(new Pair<>(i, new Output(channel1, channel2, channel3)));
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("stream", stream).toString();
  }

  public record Output(double channel1, double channel2, double channel3) {}
}
