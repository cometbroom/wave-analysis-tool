/* (C)2024 */
package com.nbmp.waveform.controller;

import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Getter;

@Getter
public class SmartObservable<T> {
  private T value;
  private final CopyOnWriteArrayList<Observer<T>> observers = new CopyOnWriteArrayList<>();

  public SmartObservable(T value) {
    this.value = value;
  }

  public SmartObservable() {
    this(null);
  }

  public synchronized void setValue(T value) {
    this.value = value;
    observers.forEach(observer -> observer.onUpdate(value));
  }

  public void addObserver(Observer<T> observer) {
    observers.add(observer);
  }

  public interface Observer<T> {
    void onUpdate(T value);
  }
}
