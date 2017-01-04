package ch.fhnw.cuie.module02.path;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * @author Dieter Holz
 */
public class PathPane extends StackPane {
	private Region regionA;

	public PathPane() {
        initializeSelf();
		initializeParts();
		layoutParts();
	}

    private void initializeSelf() {
        String fonts = getClass().getResource("fonts.css").toExternalForm();
        getStylesheets().add(fonts);

        String stylesheet = getClass().getResource("style.css").toExternalForm();
        getStylesheets().add(stylesheet);
    }

	private void initializeParts() {
		regionA = new Region();
		regionA.getStyleClass().addAll("pathPlayground");
	}

	private void layoutParts() {
		getChildren().addAll(regionA);
	}
}
