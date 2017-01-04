/*
 *
 * Copyright (c) 2015 by FHNW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ch.fhnw.cuie.module05.animationplayground;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.StrokeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * @author Dieter Holz
 */
public class AnimationPlayground extends BorderPane {

    private Circle    circle;
    private Text      text;
    private Rectangle rectangle;
    private Line      line;
    private Path      path;
    private Rectangle pathFinder;
    private Ellipse   ellipse;

    private TranslateTransition translateTransition;
    private ScaleTransition     scaleTransition;
    private RotateTransition    rotateTransition;
    private FadeTransition      fadeTransition;
    private FillTransition      fillTransition;
    private StrokeTransition    strokeTransition;
    private PathTransition      pathTransition;

    public AnimationPlayground() {
        initializeControls();
        layoutControls();
        addEventHandlers();
        addValueChangedListeners();
        addBindings();
    }

    private void initializeControls() {
        rectangle = new Rectangle(100, 100, Color.CORNFLOWERBLUE);
        rectangle.relocate(50, 50);

        circle = new Circle(250, 200, 30,
                            new RadialGradient(0, 0, 0.2, 0.3, 1, true, CycleMethod.NO_CYCLE,
                                               new Stop(0, Color.rgb(250, 250, 255)),
                                               new Stop(1, Color.CORNFLOWERBLUE)));
        //add a shadow effect
        circle.setEffect(new InnerShadow(7, Color.CORNFLOWERBLUE.darker().darker()));
        //change a cursor when it is over circle
        circle.setCursor(Cursor.HAND);

        text = new Text(50, 300, "Hi!");
        text.setFont(new Font(48));

        line = new Line(50, 400, 300, 400);
        line.setStrokeWidth(3);

        path = new Path(new MoveTo(220, 20),
                        new CubicCurveTo(580, 0, 420, 120, 320, 80),
                        new CubicCurveTo(200, 40, 200, 240, 420, 120));
        path.setStroke(Color.DODGERBLUE);
        path.getStrokeDashArray().setAll(5d, 5d);

        pathFinder = new Rectangle(30, 10, Color.CORNFLOWERBLUE);
        pathFinder.setTranslateX(205);
        pathFinder.setTranslateY(15);

        ellipse = new Ellipse(400, 400, 50, 30);
        ellipse.setStrokeWidth(3);

    }

    private void layoutControls() {
        setPadding(new Insets(10));

        Pane drawingPane = new Pane();
        drawingPane.setPrefHeight(500);
        drawingPane.setPrefWidth(500);
        drawingPane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        drawingPane.setBorder(new Border(new BorderStroke(Color.web("#0E53A7"), BorderStrokeStyle.SOLID, null, null)));

        drawingPane.getChildren().addAll(circle, text, rectangle, line, path, pathFinder, ellipse);

        setCenter(drawingPane);
    }

    private void addEventHandlers() {
        rectangle.setOnMouseClicked(event -> {
            if (translateTransition == null) {
                translateTransition = new TranslateTransition(Duration.seconds(1), rectangle);
                translateTransition.setInterpolator(Interpolator.EASE_BOTH);
                translateTransition.setFromX(0);
                translateTransition.setByX(200);
                translateTransition.setCycleCount(2);
                translateTransition.setAutoReverse(true);
            }
            if (!translateTransition.getStatus().equals(Animation.Status.RUNNING)) {
                translateTransition.play();
            }
        });

        circle.setOnMouseClicked(event -> {
            if (scaleTransition == null) {
                scaleTransition = new ScaleTransition(Duration.seconds(1), circle);
                scaleTransition.setFromX(1.0);
                scaleTransition.setFromY(1.0);
                scaleTransition.setByX(3.0);
                scaleTransition.setByY(3.0);
                scaleTransition.setCycleCount(2);
                scaleTransition.setAutoReverse(true);
            }
            if (!scaleTransition.getStatus().equals(Animation.Status.RUNNING)) {
                scaleTransition.play();
            }
        });

        text.setOnMouseClicked(event -> {
            if (rotateTransition == null) {
                rotateTransition = new RotateTransition(Duration.seconds(1), text);
                rotateTransition.setFromAngle(0.0);
                rotateTransition.setByAngle(180);
                rotateTransition.setCycleCount(2);
                rotateTransition.setAutoReverse(true);
            }
            if (!rotateTransition.getStatus().equals(Animation.Status.RUNNING)) {
                rotateTransition.play();
            }

        });

        line.setOnMouseClicked(event -> {
            if (fadeTransition == null) {
                fadeTransition = new FadeTransition(Duration.seconds(1), line);
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.2);
                fadeTransition.setCycleCount(2);
                fadeTransition.setAutoReverse(true);
            }
            if (!fadeTransition.getStatus().equals(Animation.Status.RUNNING)) {
                fadeTransition.play();
            }
        });

        pathFinder.setOnMouseClicked(event -> {
            if (pathTransition == null) {
                pathTransition = new PathTransition(Duration.seconds(2), path, pathFinder);
                pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                pathTransition.setCycleCount(2);
                pathTransition.setAutoReverse(true);
            }

            if (!pathTransition.getStatus().equals(Animation.Status.RUNNING)) {
                pathTransition.play();
            }
        });

        ellipse.setOnMouseClicked(event -> {
            if (fillTransition == null) {
                fillTransition = new FillTransition(Duration.seconds(2), ellipse);
                fillTransition.setFromValue(Color.RED);
                fillTransition.setToValue(Color.BLUE);
                fillTransition.setCycleCount(2);
                fillTransition.setAutoReverse(true);
            }
            if (strokeTransition == null) {
                strokeTransition = new StrokeTransition(Duration.seconds(2), ellipse);
                strokeTransition.setFromValue(Color.BLUE);
                strokeTransition.setToValue(Color.RED);
                strokeTransition.setCycleCount(2);
                strokeTransition.setAutoReverse(true);
            }
            if (!fillTransition.getStatus().equals(Animation.Status.RUNNING)) {
                new ParallelTransition(fillTransition, strokeTransition).play();
            }
        });
    }

    private void addValueChangedListeners() {
    }

    private void addBindings() {
    }

}
