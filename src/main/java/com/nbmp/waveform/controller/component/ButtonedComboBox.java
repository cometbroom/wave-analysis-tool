/* (C)2024 */
package com.nbmp.waveform.controller.component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class ButtonedComboBox extends HBox implements Initializable {

  @FXML private HBox root;
  @FXML public ComboBox<String> comboBox;
  @FXML public Button button;
  public String buttonText;
  private boolean load;

  public ButtonedComboBox() {
    String resourceName = "/components/ButtonedComboBox.fxml";
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resourceName));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
    log.debug("ButtonedComboBox from {} bound to controller {}", resourceName, this);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  public void setBoxValues(List<String> values) {
    comboBox.getItems().addAll(values);
    if (!values.isEmpty()) {
      comboBox.getSelectionModel().select(0);
    }
  }

  public void addListener(Consumer<String> listener) {
    button.setOnAction((e) -> listener.accept(comboBox.getSelectionModel().getSelectedItem()));
  }

  public HBox getRoot() {
    return root;
  }

  public void setLoad(boolean load) {
    this.load = load;
    this.button.setText(this.buttonText);
  }
}
