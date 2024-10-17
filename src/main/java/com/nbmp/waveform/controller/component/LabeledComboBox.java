/* (C)2024 */
package com.nbmp.waveform.controller.component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class LabeledComboBox extends HBox implements Initializable {

  @FXML private HBox root;
  private String text;
  @FXML private Label label;
  @FXML private ComboBox<String> comboBox;
  private boolean load;
  ChangeListener<String> listener;

  public LabeledComboBox() {
    String resourceName = "/components/LabeledComboBox.fxml";
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resourceName));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
    log.debug("LabeledComboBox from {} bound to controller {}", resourceName, this);
  }

  @FXML
  public void initialize(URL location, ResourceBundle resources) {}

  public void setBoxValues(List<String> values) {
    comboBox.getItems().addAll(values);
    if (!values.isEmpty()) {
      comboBox.getSelectionModel().select(0);
    }
  }

  public void addListener(ChangeListener<String> listener) {
    if (this.listener != null) {
      comboBox.getSelectionModel().selectedItemProperty().removeListener(this.listener);
    }
    this.listener = listener;
    comboBox.getSelectionModel().selectedItemProperty().addListener(this.listener);
  }

  public HBox getRoot() {
    return root;
  }

  public void setLoad(boolean load) {
    this.load = load;
    label.setText(text);
  }
}
