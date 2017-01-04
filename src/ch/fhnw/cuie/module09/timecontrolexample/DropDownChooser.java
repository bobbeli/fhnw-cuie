package ch.fhnw.cuie.module09.timecontrolexample;

import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

/**
 * @author Dieter Holz
 */
public class DropDownChooser extends VBox {
    private static final String FONTS_CSS = "fonts.css";
    private static final String STYLE_CSS = "dropDownChooser.css";
    private final TimeControl timeControl;

    private Slider hourSlider;
    private Slider minuteSlider;

    public DropDownChooser(TimeControl timeControl) {
        this.timeControl = timeControl;
        initializeSelf();
        initializeParts();
        layoutParts();
        setupBindings();
    }

    private void initializeSelf() {
        getStyleClass().add("dropDownChooser");

        String fonts = getClass().getResource(FONTS_CSS).toExternalForm();
        getStylesheets().add(fonts);

        String stylesheet = getClass().getResource(STYLE_CSS).toExternalForm();
        getStylesheets().add(stylesheet);
    }

    private void initializeParts() {
        hourSlider = new Slider(0, 23, 0);
        minuteSlider = new Slider(0, 59, 0);
    }

    private void layoutParts() {
        getChildren().addAll(hourSlider, minuteSlider);
    }

    private void setupBindings() {
        hourSlider.valueProperty().bindBidirectional(timeControl.hourProperty());
        minuteSlider.valueProperty().bindBidirectional(timeControl.minuteProperty());
    }
}
