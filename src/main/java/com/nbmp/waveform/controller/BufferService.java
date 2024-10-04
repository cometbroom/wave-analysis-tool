/* (C)2024 */
package com.nbmp.waveform.controller;

public class BufferService {
  private final int bufferSize;
  private final double[] buffer;
  private int index;

  public BufferService(int bufferSize) {
    this.bufferSize = bufferSize;
    this.buffer = new double[bufferSize];
    this.index = 0;
  }

  public void add(double value) {
    buffer[index] = value;
    index = (index + 1) % bufferSize;
  }

  public double get(int i) {
    return buffer[(index + i) % bufferSize];
  }

  public double[] getBuffer() {
    return buffer;
  }
}
