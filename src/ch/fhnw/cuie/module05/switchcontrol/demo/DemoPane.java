package ch.fhnw.cuie.module05.switchcontrol.demo;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import ch.fhnw.cuie.module05.switchcontrol.Switch;

/**
 * @author Dieter Holz
 */
public class DemoPane extends BorderPane {
	private Switch switchControl;

	private CheckBox checkBox;

	public DemoPane() {
		initializeControls();
		layoutControls();
		addBindings();
	}

	private void initializeControls() {
		setPadding(new Insets(10));

        switchControl = new Switch();

		checkBox = new CheckBox();
        checkBox.setSelected(true);
	}

	private void layoutControls() {
		setCenter(switchControl);

		VBox box = new VBox(10, new Label("Switch Properties"), checkBox);
		box.setPadding(new Insets(10));
		setRight(box);
	}

	private void addBindings() {
		switchControl.onProperty().bindBidirectional(checkBox.selectedProperty());
	}

}
