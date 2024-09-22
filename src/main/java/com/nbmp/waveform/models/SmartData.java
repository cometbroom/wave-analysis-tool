/* (C)2024 */
package com.nbmp.waveform.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import com.nbmp.waveform.utils.GlobalUtils;

public class SmartData<T> extends AtomicReference<T> {
  private PropertyChangeSupport support = new PropertyChangeSupport(this);

  private String propertyName;

  public SmartData(T value) {
    super(value);
    String className = "SmartData";
    propertyName = className + GlobalUtils.getCounter(className).getAndIncrement();
  }

  public SmartData(T value, String propertyName) {
    super(value);
    this.propertyName = propertyName;
  }

  public void setValue(T newValue) {
    T oldValue = this.get();
    this.set(newValue);
    support.firePropertyChange(propertyName, oldValue, newValue);
  }

  public void addPropertyChangeListener(PropertyChangeListener pcl) {
    support.addPropertyChangeListener(pcl);
  }

  public void addListener(Consumer<T> consumer) {
    addPropertyChangeListener(
        evt -> {
          if (evt.getNewValue() != null) {
            try {
              consumer.accept((T) evt.getNewValue());
            } catch (ClassCastException ex) {
              System.out.println("ClassCastException occurred: " + ex.getMessage());
            }
          }
        });
  }

  public void removePropertyChangeListener(PropertyChangeListener pcl) {
    support.removePropertyChangeListener(pcl);
  }
}
