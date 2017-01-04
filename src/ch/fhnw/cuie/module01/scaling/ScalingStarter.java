package ch.fhnw.cuie.module01.scaling;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScalingStarter extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent rootPanel = new Scaling();

		Scene scene = new Scene(rootPanel);

		primaryStage.setTitle("Scaling");
		primaryStage.setScene(scene);

		primaryStage.centerOnScreen();

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
