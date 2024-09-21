/* (C)2024 */
package com.nbmp.waveform.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.atomic.AtomicReference;

public class SmartData<T> extends AtomicReference<T> {
  private PropertyChangeSupport support = new PropertyChangeSupport(this);

  public SmartData(T value) {
    super(value);
  }

  public T getValue() {
    return this.get();
  }

  public void setMaxValue(T maxValue) {
    T oldValue = this.get();
    this.set(maxValue);
    support.firePropertyChange("maxValue", oldValue, maxValue);
  }

  public void addPropertyChangeListener(PropertyChangeListener pcl) {
    support.addPropertyChangeListener(pcl);
  }

  public void removePropertyChangeListener(PropertyChangeListener pcl) {
    support.removePropertyChangeListener(pcl);
  }
}
