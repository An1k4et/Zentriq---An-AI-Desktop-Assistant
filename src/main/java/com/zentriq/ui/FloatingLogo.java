package com.zentriq.ui;

import javafx.animation.RotateTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Handles the floating logo UI and its behavior.
 * This class is responsible for creating a small, transparent, draggable window
 * with a button that launches the main application window.
 */
public class FloatingLogo {

    private final DesktopChatApp app;
    private Button logoButton;
    private Stage floatingStage;

    public FloatingLogo(DesktopChatApp app) {
        this.app = app;
    }

    /**
     * Creates and returns the floating logo stage.
     * Corresponds to the original 'createFloatingLogo' method.
     */
    public Stage create() {
        // --- Original 'createFloatingLogo' method logic ---
        floatingStage = new Stage();
        floatingStage.initStyle(StageStyle.TRANSPARENT);
        floatingStage.setAlwaysOnTop(true);
        floatingStage.setResizable(false);
        
        logoButton = new Button();
        
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/images/zentriq-logo.png"));
            ImageView logoImageView = new ImageView(logoImage);
            logoImageView.setFitWidth(60);
            logoImageView.setFitHeight(60);
            logoImageView.setPreserveRatio(true);
            logoButton.setGraphic(logoImageView);
        } catch (Exception  e) {
            logoButton.setText("💬");
            logoButton.setStyle("-fx-font-size: 45px;");
        }
        
        logoButton.setPrefSize(70, 70);
        setTransparentLogoStyle();
        
        logoButton.setOnAction(e -> {
            animateLogoRotation();
            app.handleLogoClick(); // Delegate click handling back to the main app class
        });
        
        makeDraggable(floatingStage, logoButton);
        
        StackPane logoContainer = new StackPane(logoButton);
        logoContainer.setStyle("-fx-background-color: transparent;");
        
        Scene logoScene = new Scene(logoContainer, 70, 70);
        logoScene.setFill(Color.TRANSPARENT);
        floatingStage.setScene(logoScene);
        
        floatingStage.setX(50);
        floatingStage.setY(50);

        return floatingStage;
    }

    /**
     * Corresponds to the original 'animateLogoRotation' method.
     */
    private void animateLogoRotation() {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(300), logoButton);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(180);
        rotateTransition.setCycleCount(1);
        rotateTransition.setAutoReverse(false);
        rotateTransition.play();
        
        rotateTransition.setOnFinished(e -> logoButton.setRotate(0));
    }
    
    /**
     * Corresponds to the original 'setTransparentLogoStyle' method.
     */
    private void setTransparentLogoStyle() {
        logoButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;" +
            "-fx-cursor: hand;"
        );
    }
    
    /**
     * Corresponds to the original 'makeDraggable' method.
     */
    private void makeDraggable(Stage stage, Button button) {
        final double[] xOffset = {0};
        final double[] yOffset = {0};
        
        button.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                xOffset[0] = event.getSceneX();
                yOffset[0] = event.getSceneY();
            }
        });
        
        button.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                stage.setX(event.getScreenX() - xOffset[0]);
                stage.setY(event.getScreenY() - yOffset[0]);
            }
        });
    }
}