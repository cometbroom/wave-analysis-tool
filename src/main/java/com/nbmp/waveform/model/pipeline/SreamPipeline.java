/* (C)2024 */
package com.nbmp.waveform.model.pipeline;

import java.util.concurrent.CopyOnWriteArrayList;

import com.nbmp.waveform.controller.SmartObservable;

import lombok.experimental.Delegate;

public class SreamPipeline {
  @Delegate private SmartObservable<Integer> stream = new SmartObservable<>(0);
  private CopyOnWriteArrayList<Observer> completionObservers = new CopyOnWriteArrayList<>();
  private final Integer range;

  public SreamPipeline(int range) {
    this.range = range;
  }

  public void run() {
    for (int i = 0; i < range; i++) {
      stream.setValue(i);
    }
    complete();
  }

  private void complete() {
    completionObservers.reversed().forEach(Observer::onComplete);
  }

  public void onComplete(Runnable runnable) {
    completionObservers.add(runnable::run);
  }

  public interface Observer {
    void onComplete();
  }
}
