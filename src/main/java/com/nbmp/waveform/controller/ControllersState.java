/* (C)2024 */
package com.nbmp.waveform.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.nbmp.waveform.application.AppConstants;
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

  @PostConstruct
  public void init() {
    view1.init("sine1");
    view2.init("sine2");
    resultView.init("Combination Result");
    reactor
        .getObject()
        .onStart(
            () -> {
              view1.getData().clear();
              view2.getData().clear();
              resultView.getData().clear();
            });
    reactor.getObject().addOutStreamObserver(this::addPoints);
  }

  public void addPoints(int i, double value1, double value2, double result) {
    if (i % AppConstants.VIEW_STEP == 0) {
      view1.addPoint(i, value1);
      view2.addPoint(i, value2);
      resultView.addPoint(i, result);
    }
  }
}
