/* (C)2024 */
package com.nbmp.waveform.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.nbmp.waveform.model.generation.output.BufferedOutputStream;
import com.nbmp.waveform.model.generation.output.OutputStream;
import com.nbmp.waveform.model.pipeline.StreamReactor;
import com.nbmp.waveform.view.ChartView;

import lombok.Getter;
import lombok.Setter;

/**
 * Class representing the state of waveform UI controllers such as {@link WaveController}
 */
@Getter
@Setter
@Service
@Scope("singleton")
public class ControllersState {
  @Autowired private ObjectFactory<StreamReactor> reactor;
  @Autowired private ChartView view1;
  @Autowired private ChartView view2;
  @Autowired private ChartView resultView;
  @Autowired private BufferedOutputStream outputStream;

  @PostConstruct
  public void init() {
    view1.init("sine1");
    view2.init("sine2");
    resultView.init("Combination Result");
    reactor.getObject().onStart(view1::onStart);
    reactor.getObject().onStart(view2::onStart);
    reactor.getObject().onStart(resultView::onStart);
    outputStream.getBufferedChannelFlux(OutputStream.Channel.CH1).subscribe(view1::onDataChunk);
    outputStream.getBufferedChannelFlux(OutputStream.Channel.CH2).subscribe(view2::onDataChunk);
    outputStream
        .getBufferedChannelFlux(OutputStream.Channel.CH3)
        .subscribe(resultView::onDataChunk);
  }
}
