package ch.fhnw.cuie.module05.ledonoff.demo;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import ch.fhnw.cuie.module05.ledonoff.LED;

/**
 * @author Dieter Holz
 */
public class DemoPane extends BorderPane {

    // declare the custom control
    private LED led;

    // all controls for
    private CheckBox onBox;

    public DemoPane() {
        initializeControls();
        layoutControls();
        setupBindings();
    }

    private void initializeControls() {
        setPadding(new Insets(10));

        led = new LED();

        onBox = new CheckBox("on");
    }

    private void layoutControls() {
        setMargin(onBox, new Insets(0, 50, 10, 50));

        VBox controlPane = new VBox(new Label("LED Properties"), onBox);
        controlPane.setPadding(new Insets(0, 50, 0, 50));
        controlPane.setSpacing(10);

        setCenter(led);
        setRight(controlPane);
    }

    private void setupBindings() {
    }

}
