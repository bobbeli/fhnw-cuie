package ch.fhnw.cuie.module07.canvasplayground;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;

/**
 * @author Dieter Holz
 */
public class CanvasPlayground extends StackPane {

    private Canvas canvas;

    public CanvasPlayground() {
        initializeControls();
        layoutControls();
    }

    private void initializeControls() {
        canvas = new Canvas(400, 400);

        drawCanvas();
    }

    private void layoutControls() {
        setPadding(new Insets(10));
        getChildren().add(canvas);
    }

    private void drawCanvas() {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // draw a border
        graphicsContext.setStroke(Color.RED);
        graphicsContext.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());


        // draw two circles
        graphicsContext.setStroke(Color.FORESTGREEN.brighter());
        graphicsContext.setLineWidth(5);
        graphicsContext.strokeOval(30, 10, 80, 80);
        graphicsContext.setFill(Color.FORESTGREEN);
        graphicsContext.fillOval(130, 10, 80, 80);


        // use a gradient
        Stop[] stops1 = new Stop[]{
                new Stop(0.2, Color.BLACK),
                new Stop(0.5, Color.RED),
                new Stop(0.8, Color.BLACK)};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true,
                                                CycleMethod.NO_CYCLE, stops1);
        graphicsContext.setFill(lg1);
        graphicsContext.fillRect(30, 120, 80, 80);

        Stop[] stops2 = new Stop[]{
                new Stop(0, Color.RED),
                new Stop(1, Color.BLACK)};
        RadialGradient lg2;
        lg2 = new RadialGradient(0, 0, 0.5, 0.5, 0.8, true,
                                 CycleMethod.NO_CYCLE, stops2);
        graphicsContext.setFill(lg2);
        graphicsContext.fillOval(130, 120, 80, 80);


        // draw an arc
        graphicsContext.setStroke(Color.STEELBLUE);
        graphicsContext.setLineWidth(10);
        graphicsContext.strokeArc(230, 120, 80, 80, 45, 180, ArcType.OPEN);


        // use svg path
        graphicsContext.setLineWidth(10);
        graphicsContext.setFill(Color.DARKSLATEBLUE);
        graphicsContext.beginPath();
        graphicsContext.appendSVGPath("M 150 250 L 250 250 L 200 350 z");
        graphicsContext.stroke();
        graphicsContext.fill();
    }
}
