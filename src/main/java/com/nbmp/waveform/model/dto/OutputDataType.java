/* (C)2024 */
package com.nbmp.waveform.model.dto;

import com.nbmp.waveform.application.AppConstants;
import com.nbmp.waveform.model.utils.GenConstants;

public class OutputDataType {
  public static final int BUFFER_SIZE =
      GenConstants.boundValueTo(AppConstants.getSampleCount(), 0, 1024);
  public static final int CHANNELS = 16;

  private double[][] channels = new double[CHANNELS][BUFFER_SIZE];
}
