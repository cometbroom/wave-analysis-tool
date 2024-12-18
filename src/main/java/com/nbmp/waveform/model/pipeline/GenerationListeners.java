/* (C)2024 */
package com.nbmp.waveform.model.pipeline;

import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.MoreObjects;

import lombok.experimental.Delegate;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class GenerationListeners {
  @Delegate
  private CopyOnWriteArrayList<StreamReactor.Observer> observers = new CopyOnWriteArrayList<>();

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("observers", observers).toString();
  }
}
