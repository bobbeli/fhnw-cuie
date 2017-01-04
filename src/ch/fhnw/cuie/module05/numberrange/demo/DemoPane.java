package ch.fhnw.cuie.module05.numberrange.demo;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import ch.fhnw.cuie.module05.numberrange.NumberRange;

/**
 * @author Dieter Holz
 */
public class DemoPane extends BorderPane {

    // declare the custom control
    private NumberRange range;

    // all controls
    private Slider slider;

    public DemoPane() {
        initializeControls();
        layoutControls();
        setupBindings();
    }

    private void initializeControls() {
        setPadding(new Insets(10));

        range = new NumberRange();

        slider = new Slider();
        slider.setShowTickLabels(true);
    }

    private void layoutControls() {
        VBox controlPane = new VBox(new Label("Range Properties"), slider);
        controlPane.setPadding(new Insets(0, 50, 0, 50));
        controlPane.setSpacing(10);

        setCenter(range);
        setRight(controlPane);
    }

    private void setupBindings() {
    }

}
