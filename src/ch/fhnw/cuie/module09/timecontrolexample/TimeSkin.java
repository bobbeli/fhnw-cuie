package ch.fhnw.cuie.module09.timecontrolexample;

import java.util.Arrays;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.util.Duration;

/**
 * @author Dieter Holz
 */
public class TimeSkin extends SkinBase<TimeControl> {
    private static final int IMG_SIZE   = 12;
    private static final int IMG_OFFSET = 4;

    private static final String ANGLE_DOWN = "\uf107";
    private static final String ANGLE_UP   = "\uf106";

    private enum State {
        VALID("Valid",      "valid.png"),
        INVALID("Invalid",  "invalid.png");
//        INFO("Information", "info.png"),
//        OPTIONAL("Option",   "optional.png");

        public final String    text;
        public final ImageView imageView;

        State(final String text, final String file) {
            this.text = text;
            String url = TimeSkin.class.getResource("icons/" + file).toExternalForm();
            this.imageView = new ImageView(new Image(url,
                                                     IMG_SIZE, IMG_SIZE,
                                                     true, false));
        }
    }

    private static final String FONTS_CSS = "fonts.css";
    private static final String STYLE_CSS = "style.css";

    // all parts
    private TextField editableNode;
    private Label     readOnlyNode;
    private Popup     popup;
    private Pane      dropDownChooser;
    private Button    chooserButton;

    private StackPane drawingPane;

    private Animation      invalidInputAnimation;
    private FadeTransition fadeOutValidIconAnimation;

    public TimeSkin(TimeControl control) {
        super(control);
        initializeSelf();
        initializeParts();
        layoutParts();
        initializeAnimations();
        addEventHandlers();
        addValueChangedListeners();
        setupBindings();
    }

    private void initializeSelf() {
        addStyleSheets();
    }

    private void initializeParts() {
        editableNode = new TextField();
        editableNode.getStyleClass().add("editableNode");

        readOnlyNode = new Label();
        readOnlyNode.getStyleClass().add("readOnlyNode");

        State.VALID.imageView.setOpacity(0.0);

        chooserButton = new Button(ANGLE_DOWN);
        chooserButton.getStyleClass().add("chooserButton");

        dropDownChooser = new DropDownChooser(getSkinnable());

        popup = new Popup();
        popup.getContent().addAll(dropDownChooser);

        drawingPane = new StackPane();
        drawingPane.getStyleClass().add("drawingPane");
    }

    private void layoutParts() {
        StackPane.setAlignment(chooserButton, Pos.CENTER_RIGHT);
        drawingPane.getChildren().addAll(editableNode, chooserButton, readOnlyNode);

        Arrays.stream(State.values())
              .map(state -> state.imageView)
              .forEach(imageView -> {
                  imageView.setManaged(false);
                  drawingPane.getChildren().add(imageView);
              });

        StackPane.setAlignment(editableNode, Pos.CENTER_LEFT);
        StackPane.setAlignment(readOnlyNode, Pos.CENTER_LEFT);

        getChildren().add(drawingPane);
    }

    private void initializeAnimations() {
        int      delta    = 5;
        Duration duration = Duration.millis(30);

        TranslateTransition moveRight = new TranslateTransition(duration, editableNode);
        moveRight.setFromX(0.0);
        moveRight.setByX(delta);
        moveRight.setAutoReverse(true);
        moveRight.setCycleCount(2);
        moveRight.setInterpolator(Interpolator.LINEAR);

        TranslateTransition moveLeft = new TranslateTransition(duration, editableNode);
        moveLeft.setFromX(0.0);
        moveLeft.setByX(-delta);
        moveLeft.setAutoReverse(true);
        moveLeft.setCycleCount(2);
        moveLeft.setInterpolator(Interpolator.LINEAR);

        invalidInputAnimation = new SequentialTransition(moveRight, moveLeft);
        invalidInputAnimation.setCycleCount(3);

        fadeOutValidIconAnimation = new FadeTransition(Duration.millis(500), State.VALID.imageView);
        fadeOutValidIconAnimation.setDelay(Duration.seconds(1));
        fadeOutValidIconAnimation.setFromValue(1.0);
        fadeOutValidIconAnimation.setToValue(0.0);
    }

    private void addEventHandlers() {
        chooserButton.setOnAction(event -> {
            if (popup.isShowing()) {
                popup.hide();
            } else {
                popup.show(editableNode.getScene().getWindow());
            }
        });

        popup.setOnHidden(event -> chooserButton.setText(ANGLE_DOWN));

        popup.setOnShown(event -> {
            chooserButton.setText(ANGLE_UP);
            Point2D location = editableNode.localToScreen(editableNode.getWidth() - dropDownChooser.getPrefWidth() - 3, editableNode.getHeight() -3);

            popup.setX(location.getX());
            popup.setY(location.getY());
        });

        editableNode.setOnAction(event -> {
            getSkinnable().convertUserInput();
            if (getSkinnable().isInvalid()) {
                startInvalidInputAnimation(true);
            }
        });

        editableNode.setOnKeyPressed(event -> {
            int caretPos = editableNode.getCaretPosition();

            switch (event.getCode()) {
                case ESCAPE:
                    getSkinnable().reset();
                    event.consume();
                    break;
                case UP:
                    if (getSkinnable().isInTimeFormat(editableNode.getText())) {
                        if (caretPos < 3) {
                            getSkinnable().increaseHour();
                            selectHour();
                        } else {
                            getSkinnable().increaseMinute();
                            selectMinute();
                        }
                    }
                    event.consume();
                    break;
                case DOWN:
                    if (getSkinnable().isInTimeFormat(editableNode.getText())) {
                        if (caretPos < 3) {
                            getSkinnable().decreaseHour();
                            selectHour();
                        } else {
                            getSkinnable().decreaseMinute();
                            selectMinute();
                        }
                    }
                    event.consume();
                    break;
                case RIGHT:
                    if (caretPos < 2 || caretPos == editableNode.getLength()) {
                        selectHour();
                    } else {
                        selectMinute();
                    }
                    event.consume();
                    break;
                case LEFT:
                    int anchor = editableNode.getAnchor();
                    if ((caretPos < 3 && anchor != 0) || anchor == 3) {
                        selectHour();
                    } else {
                        selectMinute();
                    }
                    event.consume();
            }
        });
    }

    private void selectMinute() {
        editableNode.selectRange(3, editableNode.getLength());
    }

    private void selectHour() {
        editableNode.selectRange(0, Math.min(2, editableNode.getLength()));
    }

    private void addValueChangedListeners() {
        getSkinnable().invalidProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                State.VALID.imageView.setOpacity(1.0);
                startFadeOutValidIconTransition();
            }
            startInvalidInputAnimation(newValue);
        });
    }

    private void startFadeOutValidIconTransition() {
        if (fadeOutValidIconAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            return;
        }
        fadeOutValidIconAnimation.play();
    }

    private void startInvalidInputAnimation(boolean isInvalid) {
        if (invalidInputAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            invalidInputAnimation.stop();
        }
        if (isInvalid) {
            invalidInputAnimation.play();
        }
    }

    private void setupBindings() {
        readOnlyNode.textProperty().bind(getSkinnable().timeProperty().asString("%tR"));
        editableNode.textProperty().bindBidirectional(getSkinnable().userFacingTextProperty());

        editableNode.promptTextProperty().bind(getSkinnable().labelProperty());

        editableNode.visibleProperty().bind(getSkinnable().readOnlyProperty().not());
        chooserButton.visibleProperty().bind(getSkinnable().readOnlyProperty().not());
        readOnlyNode.visibleProperty().bind(getSkinnable().readOnlyProperty());

        State.INVALID.imageView.visibleProperty().bind(getSkinnable().invalidProperty());

        State.INVALID.imageView.xProperty().bind(editableNode.translateXProperty().add(editableNode.layoutXProperty()).subtract(IMG_OFFSET));
        State.INVALID.imageView.yProperty().bind(editableNode.translateYProperty().add(editableNode.layoutYProperty()).subtract(IMG_OFFSET));
        State.VALID.imageView.xProperty().bind(editableNode.layoutXProperty().subtract(IMG_OFFSET));
        State.VALID.imageView.yProperty().bind(editableNode.layoutYProperty().subtract(IMG_OFFSET));
    }

    private void addStyleSheets() {
        String fonts = getClass().getResource(FONTS_CSS).toExternalForm();
        getSkinnable().getStylesheets().add(fonts);

        String stylesheet = getClass().getResource(STYLE_CSS).toExternalForm();
        getSkinnable().getStylesheets().add(stylesheet);
    }

}
