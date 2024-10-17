/* (C)2024 */
package com.nbmp.waveform.application;

import com.nbmp.waveform.model.generation.GenerationState;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import atlantafx.base.theme.PrimerLight;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Component
public class WaveformPlotter extends Application {
  @Autowired private AnchorPane mainView;

  private ApplicationContext springContext;

  @Override
  public void init() {
    springContext = new AnnotationConfigApplicationContext(AppConfig.class);
    springContext.getAutowireCapableBeanFactory().autowireBean(this);
  }

  @Override
  public void start(Stage stage) {
    String STAGE_TITLE = "Waveform Analysis Graph";

    Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    stage.setTitle(STAGE_TITLE);
    stage.setScene(new Scene(mainView));
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
