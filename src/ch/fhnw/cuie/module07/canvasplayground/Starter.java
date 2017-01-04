package ch.fhnw.cuie.module07.canvasplayground;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import ch.fhnw.cuie.module06.keyframeplayground.KeyframePlayground;

/**
 * @author Dieter Holz
 */
public class Starter extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent rootPanel = new CanvasPlayground();

		Scene scene = new Scene(rootPanel);

		primaryStage.setTitle("KeyFrame Playground");
		primaryStage.setScene(scene);

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
