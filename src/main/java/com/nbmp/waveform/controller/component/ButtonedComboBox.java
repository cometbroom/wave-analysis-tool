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

/**
 * A custom component that combines a ComboBox and a Button within an HBox layout.
 * It allows for easy selection from a list and the triggering of an action via the button.
 */
@Slf4j
@Getter
@Setter
public class ButtonedComboBox extends HBox implements Initializable {

  @FXML private HBox root;
  @FXML public ComboBox<String> comboBox;
  @FXML public Button button;
  public String buttonText;
  private boolean load;

  /**
   * Constructor that loads the ButtonedComboBox component from the FXML file.
   */
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

  /**
   * Sets the values in the ComboBox and selects the first item by default.
   *
   * @param values the list of values to populate the ComboBox
   */
  public void setBoxValues(List<String> values) {
    comboBox.getItems().addAll(values);
    if (!values.isEmpty()) {
      comboBox.getSelectionModel().select(0);
    }
  }

  /**
   * Adds a listener to the button that triggers the provided Consumer when the button is clicked.
   *
   * @param listener the action to perform when the button is clicked, using the selected ComboBox value
   */
  public void addListener(Consumer<String> listener) {
    button.setOnAction((e) -> listener.accept(comboBox.getSelectionModel().getSelectedItem()));
  }

  public HBox getRoot() {
    return root;
  }

  /**
   * Sets the load state and updates the button text accordingly.
   *
   * @param load the load state of the component
   */
  public void setLoad(boolean load) {
    this.load = load;
    this.button.setText(this.buttonText);
  }
}
