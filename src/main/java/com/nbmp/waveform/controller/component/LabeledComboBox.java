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

/**
 * A custom component that combines a ComboBox with a Label within an HBox layout.
 * This component is useful for providing a labeled drop-down menu in the UI.
 */
@Getter
@Setter
@Slf4j
public class LabeledComboBox extends HBox implements Initializable {

  @FXML private HBox root;
  private String text;
  @FXML private Label label;
  @FXML private ComboBox<String> comboBox;
  private boolean load;
  private ChangeListener<String> listener;

  /**
   * Constructor that loads the LabeledComboBox component from the FXML file.
   */
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
   * Adds a listener that listens for selection changes in the ComboBox.
   *
   * @param listener the listener to be notified when the selected item changes
   */
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

  /**
   * Sets the load state and updates the label accordingly.
   *
   * @param load the load state of the component
   */
  public void setLoad(boolean load) {
    this.load = load;
    label.setText(text);
  }
}
