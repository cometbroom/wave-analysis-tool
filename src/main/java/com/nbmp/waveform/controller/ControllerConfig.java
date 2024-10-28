/* (C)2024 */
package com.nbmp.waveform.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nbmp.waveform.view.ChartView;

@Configuration
public class ControllerConfig {

  @Bean
  public ChartView view1() {
    var view1 = new ChartView();
    view1.init("sine1");
    return view1;
  }

  @Bean
  public ChartView view2() {
    var view1 = new ChartView();
    view1.init("sine2");
    return view1;
  }

  @Bean
  public ChartView resultView() {
    var view1 = new ChartView();
    view1.init("Combination Result");
    return view1;
  }
}
