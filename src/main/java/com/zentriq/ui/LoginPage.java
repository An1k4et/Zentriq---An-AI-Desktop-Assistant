package com.zentriq.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 * Handles the login page UI and its authentication logic.
 * This class is responsible for creating the login form, handling user input,
 * and performing simple validation before notifying the main application.
 */
public class LoginPage {

    private final DesktopChatApp app;
    private VBox loginPane;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label statusLabel;
    
    public LoginPage(DesktopChatApp app) {
        this.app = app;
        createLoginPane(); // Build the UI on initialization
    }

    public VBox getLoginPane() {
        return loginPane;
    }
    
    /**
     * Corresponds to the original 'createLoginPane' method.
     */
    private void createLoginPane() {
        loginPane = new VBox(30);
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setPadding(new Insets(50));
        loginPane.setMaxWidth(480);
        loginPane.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgba(20, 30, 70, 0.95), rgba(15, 25, 50, 0.98));" +
            "-fx-background-radius: 25px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0), 30, 0, 0, 8);" +
            "-fx-border-color: rgba(0, 188, 255, 0.3);" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 25px;"
        );
        
        // --- Brand and Form sections (original code) ---
        VBox brandSection = new VBox(15);
        brandSection.setAlignment(Pos.CENTER);
        Label brandLogo = new Label("Z");
        brandLogo.setStyle(
            "-fx-font-size: 72px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: linear-gradient(from 0% 0% to 100% 100%, #00BCFF 0%, #0080FF 50%, #0066CC 100%);" +
            "-fx-effect: dropshadow(gaussian, rgba(0,188,255,0.6), 15, 0, 0, 0);"
        );
        Label titleLabel = new Label("Welcome to Zentriq");
        titleLabel.setStyle(
            "-fx-font-size: 32px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: linear-gradient(from 0% 0% to 100% 100%, #00BCFF 0%, #FFFFFF 100%);"
        );
        Label subtitleLabel = new Label("Connect. Chat. Collaborate.");
        subtitleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: rgba(255, 255, 255, 0.8);" +
            "-fx-font-weight: 300;"
        );
        brandSection.getChildren().addAll(brandLogo, titleLabel, subtitleLabel);
        
        VBox formSection = new VBox(20);
        formSection.setAlignment(Pos.CENTER);
        formSection.setPadding(new Insets(20, 0, 0, 0));
        
     // Username field with modern styling
        VBox usernameContainer = new VBox(8);
        Label usernameLabel = new Label("Username");
        usernameLabel.setStyle(
            "-fx-text-fill: rgba(255, 255, 255, 0.9);" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 500;"
        );
        
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setPrefHeight(50);
        usernameField.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-padding: 15px 20px;" +
            "-fx-background-color: rgba(255, 255, 255, 0.1);" +
            "-fx-background-radius: 15px;" +
            "-fx-border-color: rgba(0, 188, 255, 0.3);" +
            "-fx-border-radius: 15px;" +
            "-fx-border-width: 2px;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: rgba(255, 255, 255, 0.6);"
        );
        
        // Focus effect for username field
        usernameField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                usernameField.setStyle(
                    "-fx-font-size: 15px;" +
                    "-fx-padding: 15px 20px;" +
                    "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                    "-fx-background-radius: 15px;" +
                    "-fx-border-color: #00BCFF;" +
                    "-fx-border-radius: 15px;" +
                    "-fx-border-width: 2px;" +
                    "-fx-text-fill: white;" +
                    "-fx-prompt-text-fill: rgba(255, 255, 255, 0.6);" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,188,255,0.4), 10, 0, 0, 0);"
                );
            } else {
                usernameField.setStyle(
                    "-fx-font-size: 15px;" +
                    "-fx-padding: 15px 20px;" +
                    "-fx-background-color: rgba(255, 255, 255, 0.1);" +
                    "-fx-background-radius: 15px;" +
                    "-fx-border-color: rgba(0, 188, 255, 0.3);" +
                    "-fx-border-radius: 15px;" +
                    "-fx-border-width: 2px;" +
                    "-fx-text-fill: white;" +
                    "-fx-prompt-text-fill: rgba(255, 255, 255, 0.6);"
                );
            }
        });
        
        usernameContainer.getChildren().addAll(usernameLabel, usernameField);
        
        // Password field with matching styling
        VBox passwordContainer = new VBox(8);
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle(
            "-fx-text-fill: rgba(255, 255, 255, 0.9);" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 500;"
        );
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefHeight(50);
        passwordField.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-padding: 15px 20px;" +
            "-fx-background-color: rgba(255, 255, 255, 0.1);" +
            "-fx-background-radius: 15px;" +
            "-fx-border-color: rgba(0, 188, 255, 0.3);" +
            "-fx-border-radius: 15px;" +
            "-fx-border-width: 2px;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: rgba(255, 255, 255, 0.6);"
        );
        
        // Focus effect for password field
        passwordField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                passwordField.setStyle(
                    "-fx-font-size: 15px;" +
                    "-fx-padding: 15px 20px;" +
                    "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                    "-fx-background-radius: 15px;" +
                    "-fx-border-color: #00BCFF;" +
                    "-fx-border-radius: 15px;" +
                    "-fx-border-width: 2px;" +
                    "-fx-text-fill: white;" +
                    "-fx-prompt-text-fill: rgba(255, 255, 255, 0.6);" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,188,255,0.4), 10, 0, 0, 0);"
                );
            } else {
                passwordField.setStyle(
                    "-fx-font-size: 15px;" +
                    "-fx-padding: 15px 20px;" +
                    "-fx-background-color: rgba(255, 255, 255, 0.1);" +
                    "-fx-background-radius: 15px;" +
                    "-fx-border-color: rgba(0, 188, 255, 0.3);" +
                    "-fx-border-radius: 15px;" +
                    "-fx-border-width: 2px;" +
                    "-fx-text-fill: white;" +
                    "-fx-prompt-text-fill: rgba(255, 255, 255, 0.6);"
                );
            }
        });
        
        passwordContainer.getChildren().addAll(passwordLabel, passwordField);
        Button loginButton = new Button("Sign In to Zentriq");
        loginButton.setPrefHeight(55);
        loginButton.setPrefWidth(300);
        loginButton.setStyle(
            "-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #00BCFF 0%, #0080FF 100%);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 27px;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,188,255,0.4), 15, 0, 0, 5);"
        );
        loginButton.setOnMouseEntered(e -> applyHoverStyle(loginButton, true));
        loginButton.setOnMouseExited(e -> applyHoverStyle(loginButton, false));
        
        statusLabel = new Label();
        statusLabel.setStyle(
            "-fx-text-fill: #FF6B6B;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 500;" +
            "-fx-background-color: rgba(255, 107, 107, 0.1);" +
            "-fx-padding: 8px 15px;" +
            "-fx-background-radius: 10px;"
        );
        statusLabel.setVisible(false);

        Runnable loginAction = this::handleLogin;
        loginButton.setOnAction(e -> loginAction.run());
        passwordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loginAction.run();
            }
        });
        
        formSection.getChildren().addAll(usernameContainer, passwordContainer, loginButton, statusLabel);
        loginPane.getChildren().addAll(brandSection, formSection);
    }

    /**
     * Corresponds to the original authentication logic.
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("⚠ Please fill in all fields");
            statusLabel.setVisible(true);
            return;
        }
        
        if (password.length() >= 3) {
            app.setLoggedIn(true, username);
            app.showChatInterface();
            statusLabel.setVisible(false);
            usernameField.clear();
            passwordField.clear();
        } else {
            statusLabel.setText("⚠ Password must be at least 3 characters");
            statusLabel.setVisible(true);
        }
    }
    
    private void applyFocusStyle(Control control, boolean isFocused) {
        if (isFocused) {
            control.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-padding: 15px 20px;" +
                "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                "-fx-background-radius: 15px;" +
                "-fx-border-color: #00BCFF;" +
                "-fx-border-radius: 15px;" +
                "-fx-border-width: 2px;" +
                "-fx-text-fill: white;" +
                "-fx-prompt-text-fill: rgba(255, 255, 255, 0.6);" +
                "-fx-effect: dropshadow(gaussian, rgba(0,188,255,0.4), 10, 0, 0, 0);"
            );
        } else {
            control.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-padding: 15px 20px;" +
                "-fx-background-color: rgba(255, 255, 255, 0.1);" +
                "-fx-background-radius: 15px;" +
                "-fx-border-color: rgba(0, 188, 255, 0.3);" +
                "-fx-border-radius: 15px;" +
                "-fx-border-width: 2px;" +
                "-fx-text-fill: white;" +
                "-fx-prompt-text-fill: rgba(255, 255, 255, 0.6);"
            );
        }
    }
    
    private void applyHoverStyle(Button button, boolean isHovered) {
        if (isHovered) {
            button.setStyle(
                "-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #00D4FF 0%, #0090FF 100%);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 27px;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,188,255,0.6), 20, 0, 0, 8);" +
                "-fx-scale-x: 1.02;" +
                "-fx-scale-y: 1.02;"
            );
        } else {
            button.setStyle(
                "-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #00BCFF 0%, #0080FF 100%);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 27px;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,188,255,0.4), 15, 0, 0, 5);" +
                "-fx-scale-x: 1.0;" +
                "-fx-scale-y: 1.0;"
            );
        }
    }

    /**
     * Corresponds to the original 'calculateSmartPosition' method.
     */
    public double[] calculateSmartPosition(double windowWidth, double windowHeight, Stage floatingStage) {
        // --- Original 'calculateSmartPosition' logic ---
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        
        double logoX = floatingStage.getX();
        double logoY = floatingStage.getY();
        double logoWidth = 70;
        double logoHeight = 70;
        
        double newX, newY;
        
        if (logoX < screenBounds.getWidth() / 2) {
            newX = logoX + logoWidth + 10;
        } else {
            newX = logoX - windowWidth - 10;
        }
        
        if (logoY < screenBounds.getHeight() / 2) {
            newY = logoY;
        } else {
            newY = logoY + logoHeight - windowHeight;
        }
        
        newX = Math.max(10, Math.min(newX, screenBounds.getMaxX() - windowWidth - 10));
        newY = Math.max(10, Math.min(newY, screenBounds.getMaxY() - windowHeight - 10));
        
        return new double[]{newX, newY};
    }
}