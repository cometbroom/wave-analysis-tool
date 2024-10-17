/* (C)2024 */
package com.nbmp.waveform.controller;

import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Getter;

/**
 * Class representing a generic observable that notifies its observers whenever its value changes.
 *
 * @param <T> the type of the value being observed
 */
@Getter
public class SmartObservable<T> {
  private T value;
  private final CopyOnWriteArrayList<Observer<T>> observers = new CopyOnWriteArrayList<>();

  /**
   * Constructor to create a SmartObservable with an initial value.
   *
   * @param value the initial value of the observable
   */
  public SmartObservable(T value) {
    this.value = value;
  }

  /**
   * Default constructor to create a SmartObservable with a null value.
   */
  public SmartObservable() {
    this(null);
  }

  /**
   * Sets the value of the observable and notifies all registered observers.
   *
   * @param value the new value to be set
   */
  public synchronized void setValue(T value) {
    this.value = value;
    observers.forEach(observer -> observer.onUpdate(value));
  }

  /**
   * Adds an observer to the observable.
   *
   * @param observer the observer to be added
   */
  public void addObserver(Observer<T> observer) {
    observers.add(observer);
  }

  /**
   * Interface representing an observer that listens for value updates.
   *
   * @param <T> the type of the value being observed
   */
  public interface Observer<T> {
    void onUpdate(T value);
  }
}
