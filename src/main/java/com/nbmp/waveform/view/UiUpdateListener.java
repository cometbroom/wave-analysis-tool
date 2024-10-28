/* (C)2024 */
package com.nbmp.waveform.view;

import java.util.List;

import com.nbmp.waveform.model.generation.output.OutputStream;

public interface UiUpdateListener {
  void onDataChunk(List<OutputStream.Output> dataChunk);

  void onStreamEnd();
}
