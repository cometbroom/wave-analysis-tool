package com.nbmp.waveform.controller.component;

import com.nbmp.waveform.controller.ControllersState;
import com.nbmp.waveform.view.WavesRegister;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@Getter
@Setter
public class WaveSliders extends HBox implements Initializable {
  @FXML public HBox root;
  @FXML public WaveLabeledSlider amplitudeSlider;
  @FXML public WaveLabeledSlider frequencySlider;
  @FXML public WaveLabeledSlider phaseSlider;
  @FXML public Label titleLabel;
  public String title;

  public WaveSliders() {
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getResource("/components/WaveSliders.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }
  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  public void setupSliders(WavesRegister waveform, Consumer<Double> refreshTask) {
    amplitudeSlider.addListenerForTarget(waveform, WaveLabeledSlider.Target.AMPLITUDE);
    amplitudeSlider.setRefreshTask(refreshTask);
    frequencySlider.addListenerForTarget(waveform, WaveLabeledSlider.Target.FREQUENCY);
    frequencySlider.setRefreshTask(refreshTask);
    phaseSlider.addListenerForTarget(waveform, WaveLabeledSlider.Target.PHASE);
    phaseSlider.setRefreshTask(refreshTask);
  }

  public void setTitle(String title) {
    this.title = title;
    titleLabel.setText(title);
  }

  public void setLoad(boolean load) {

  }



  public HBox getRoot() {
    return root;
  }
}
