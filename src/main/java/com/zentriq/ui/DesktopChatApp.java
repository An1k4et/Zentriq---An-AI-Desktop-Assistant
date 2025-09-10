package com.zentriq.ui;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.zentriq.ZentriqApplication;
import com.zentriq.ai.OllamaService;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Main application class to launch the desktop chat application.
 * This class manages the primary stage and initializes the floating logo and main UI stages.
 * It also handles the overall application flow and state (e.g., login status).
 */

public class DesktopChatApp extends Application {

    private Stage floatingStage;
    private Stage mainStage;
    private FloatingLogo floatingLogo;
    private LoginPage loginPage;
    private ChatPage chatPage;
    
    // Application state
    private boolean isLoggedIn = false;
    private String currentUser = "";
    
    @Autowired
    private OllamaService ollamaService;
    public static ConfigurableApplicationContext springContext;
    
    @Override
    public void init() {
        this.ollamaService = springContext.getBean(OllamaService.class);
    }
    
    @Override
    public void start(Stage primaryStage) {
        // --- Original 'start' method logic ---
        this.mainStage = primaryStage;
        mainStage.setTitle("Zentriq");
        mainStage.initStyle(StageStyle.TRANSPARENT);
        mainStage.setAlwaysOnTop(true);

        // Initialize and create UI components
        this.floatingLogo = new FloatingLogo(this);
        this.loginPage = new LoginPage(this);
        if(ollamaService == null) {
        	System.out.println("Yes it is null ");
        }
        this.chatPage = new ChatPage(this, ollamaService);
        
        // Create the floating logo window
        this.floatingStage = floatingLogo.create();
        
        // Setup main window (hidden initially)
        setupMainWindow();

        // Show only the floating logo initially
        floatingStage.show();

        // Handle application closing
        Platform.setImplicitExit(false);
        floatingStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * Initializes the main window, which will display either the login or chat pane.
     */
    private void setupMainWindow() {
        // --- Original 'setupMainWindow' method logic ---
        StackPane mainContainer = new StackPane();
        mainContainer.setStyle("-fx-background-color: transparent;");

        Scene mainScene = new Scene(mainContainer, 800, 600);
        mainScene.setFill(Color.TRANSPARENT);
        mainStage.setScene(mainScene);

        // Handle main window closing - hide instead of exit
        mainStage.setOnCloseRequest(e -> {
            e.consume();
            mainStage.hide();
        });
    }

    // --- Original 'handleLogoClick' logic, now delegated from FloatingLogo ---
    public void handleLogoClick() {
        if (mainStage.isShowing()) {
            mainStage.hide();
        } else {
            if (isLoggedIn) {
                showChatInterface();
            } else {
                showLoginInterface();
            }
        }
    }

    // --- Original 'showLoginInterface' method logic ---
    public void showLoginInterface() {
        mainStage.getScene().setRoot(loginPage.getLoginPane());
        mainStage.setWidth(650);
        mainStage.setHeight(700);
        
        double[] position = loginPage.calculateSmartPosition(650, 700, floatingStage);
        mainStage.setX(position[0]);
        mainStage.setY(position[1]);
        
        mainStage.show();
        mainStage.toFront();
    }

    // --- Original 'showChatInterface' method logic ---
    public void showChatInterface() {
        if (chatPage.getChatMessages().isEmpty()) {
            String welcomeMessage = chatPage.generateWelcomeMessage(currentUser);
            chatPage.addMessage(welcomeMessage);
        }
        
        mainStage.getScene().setRoot(chatPage.getChatPane());
        mainStage.setWidth(430);
        mainStage.setHeight(600);
        
        double[] position = chatPage.calculateSmartPosition(430, 600, floatingStage);
        mainStage.setX(position[0]);
        mainStage.setY(position[1]);
        
        mainStage.show();
        mainStage.toFront();
    }

    // --- Original 'logout' method logic ---
    public void logout() {
        this.isLoggedIn = false;
        this.currentUser = "";
        chatPage.clearChatMessages();
        mainStage.hide();
    }
    
    // --- Getters and setters for app state ---
    public void setLoggedIn(boolean isLoggedIn, String username) {
        this.isLoggedIn = isLoggedIn;
        this.currentUser = username;
    }
    
    public String getCurrentUser() {
        return currentUser;
    }

    public static void main(String[] args) {
        launch(args);
    }
}