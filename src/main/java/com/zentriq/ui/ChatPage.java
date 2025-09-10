package com.zentriq.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;

import com.zentriq.ai.CustomDocumentLoader;
import com.zentriq.ai.OllamaService;

/**
 * Handles the main chat window UI and its functionality.
 * This class is responsible for creating the chat interface, managing chat messages,
 * and handling user input and bot responses.
 */
public class ChatPage {

    private final DesktopChatApp app;
    private VBox chatPane;
    private ListView<String> chatListView;
    private ObservableList<String> chatMessages = FXCollections.observableArrayList();
    private TextField messageInput;
    private Button sendButton;
    private Button attachButton;
    
    // Assume RagService is autowired or passed into ChatPage
    private final OllamaService ollamaService;
    
    public ChatPage(DesktopChatApp app, OllamaService ollamaService) {
        this.app = app;
        this.ollamaService = ollamaService;
        createChatPane(); // Build the UI on initialization
    }

    public VBox getChatPane() {
        return chatPane;
    }
    
    public ObservableList<String> getChatMessages() {
        return chatMessages;
    }
    
    public void clearChatMessages() {
        chatMessages.clear();
    }
    
    public void addMessage(String message) {
        chatMessages.add(message);
        chatListView.scrollTo(chatMessages.size() - 1);
    }

    /**
     * Corresponds to the original 'createChatPane' method.
     */
    private void createChatPane() {
        chatPane = new VBox(0);
        chatPane.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgba(20, 30, 70, 0.95), rgba(15, 25, 50, 0.98));" +
            "-fx-background-radius: 20px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0), 25, 0, 0, 8);" +
            "-fx-border-color: rgba(0, 188, 255, 0.3);" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 20px;"
        );
        
        // --- Header Section (original code) ---
        HBox header = new HBox();
        header.setPadding(new Insets(25, 25, 20, 25));
        header.setStyle(
            "-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, rgba(0, 188, 255, 0.2) 0%, rgba(0, 128, 255, 0.1) 100%);" +
            "-fx-background-radius: 20px 20px 0 0;"
        );
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox headerContent = new VBox(2);
        Label headerTitle = new Label("Zentriq Assistant");
        headerTitle.setStyle("-fx-text-fill: linear-gradient(from 0% 0% to 100% 100%, #00BCFF 0%, #FFFFFF 100%); -fx-font-size: 22px; -fx-font-weight: bold;");
        Label headerSubtitle = new Label("Connected • Ready to help");
        headerSubtitle.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.7); -fx-font-size: 12px;");
        headerContent.getChildren().addAll(headerTitle, headerSubtitle);
        
        HBox controlButtons = new HBox(10);
        controlButtons.setAlignment(Pos.CENTER_RIGHT);
        
        
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-text-fill: rgba(255,255,255,0.8); -fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 15px; -fx-background-radius: 15px; -fx-padding: 8px 16px; -fx-cursor: hand; -fx-font-size: 12px; -fx-border-width: 1px;");
        logoutButton.setOnAction(e -> app.logout());
        
        controlButtons.getChildren().addAll(logoutButton);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(headerContent, spacer, controlButtons);
        
        // --- Chat Area with Fixed Text Wrapping ---
        chatListView = new ListView<>(chatMessages);
        chatListView.setPrefHeight(420);
        chatListView.setStyle("-fx-background-color: rgba(0, 0, 0, 0.1); -fx-border-color: transparent; -fx-padding: 15px; -fx-background-insets: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
     // Or for a more robust solution, you can use this approach after the ListView is created:
        chatListView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin != null) {
                // Find the vertical scroll bar and make it take no space
                chatListView.lookup(".scroll-bar:vertical").setStyle(
                    "-fx-pref-width: 0;" +
                    "-fx-max-width: 0;" +
                    "-fx-min-width: 0;" +
                    "-fx-opacity: 0;"
                );
                
                // Also handle the corner where scroll bars meet (if horizontal scroll bar appears)
                if (chatListView.lookup(".corner") != null) {
                    chatListView.lookup(".corner").setStyle(
                        "-fx-pref-width: 0;" +
                        "-fx-max-width: 0;" +
                        "-fx-min-width: 0;" +
                        "-fx-pref-height: 0;" +
                        "-fx-max-height: 0;" +
                        "-fx-min-height: 0;"
                    );
                }
            }
        });
        
        // Fixed cell factory with proper text wrapping and spacing
        chatListView.setCellFactory(listView -> new ListCell<String>() {
            private Text text = new Text();
            private VBox container = new VBox();
            
            {
                // Initialize container with padding for spacing between messages
                container.setPadding(new Insets(5, 0, 5, 0)); // Top and bottom padding for spacing
                container.getChildren().add(text);
            }
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    // Use Text node for proper wrapping
                    text.setText(item);
                    text.setWrappingWidth(listView.getWidth() - 80); // Account for padding and scrollbar
                    
                    if (item.contains("You:")) {
                        text.setStyle("-fx-fill: rgba(255, 255, 255, 0.9); -fx-font-size: 13px;");
                        container.setStyle("-fx-background-color: rgba(0, 188, 255, 0.1); -fx-background-radius: 10px; -fx-padding: 8px 12px;");
                        setStyle("-fx-background-color: transparent; -fx-padding: 5px 0;");
                    } else if (item.contains("Assistant:")) {
                        text.setStyle("-fx-fill: rgba(255, 255, 255, 0.8); -fx-font-size: 13px;");
                        container.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 10px; -fx-padding: 8px 12px;");
                        setStyle("-fx-background-color: transparent; -fx-padding: 5px 0;");
                    } else {
                        text.setStyle("-fx-fill: rgba(255, 255, 255, 0.7); -fx-font-size: 13px;");
                        container.setStyle("-fx-background-color: transparent; -fx-padding: 8px 12px;");
                        setStyle("-fx-background-color: transparent; -fx-padding: 5px 0;");
                    }
                    
                    setText(null);
                    setGraphic(container);
                }
            }
        });
        
        // Update text wrapping when ListView width changes
        chatListView.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            chatListView.refresh();
        });
        
        // --- Input Area (original code) ---
        HBox inputArea = new HBox(15);
        inputArea.setPadding(new Insets(5, 25, 5, 25));
        inputArea.setAlignment(Pos.CENTER);
        inputArea.setStyle(
            "-fx-background-color: rgba(0, 0, 0, 0.1);" +
            "-fx-background-radius: 0 0 20px 20px;"
        );
        
        messageInput = new TextField();
        messageInput.setPromptText("Type your message here...");
        messageInput.setPrefHeight(40);
        messageInput.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-padding: 15px 20px;" +
            "-fx-background-color: rgba(255, 255, 255, 0.1);" +
            "-fx-background-radius: 25px;" +
            "-fx-border-color: rgba(0, 188, 255, 0.3);" +
            "-fx-border-radius: 25px;" +
            "-fx-border-width: 2px;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: rgba(255, 255, 255, 0.5);"
        );
        
        // Focus effect for message input
        messageInput.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                messageInput.setStyle(
                    "-fx-font-size: 14px;" +
                    "-fx-padding: 15px 20px;" +
                    "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                    "-fx-background-radius: 25px;" +
                    "-fx-border-color: #00BCFF;" +
                    "-fx-border-radius: 25px;" +
                    "-fx-border-width: 2px;" +
                    "-fx-text-fill: white;" +
                    "-fx-prompt-text-fill: rgba(255, 255, 255, 0.5);" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,188,255,0.3), 10, 0, 0, 0);"
                );
            } else {
                messageInput.setStyle(
                    "-fx-font-size: 14px;" +
                    "-fx-padding: 15px 20px;" +
                    "-fx-background-color: rgba(255, 255, 255, 0.1);" +
                    "-fx-background-radius: 25px;" +
                    "-fx-border-color: rgba(0, 188, 255, 0.3);" +
                    "-fx-border-radius: 25px;" +
                    "-fx-border-width: 2px;" +
                    "-fx-text-fill: white;" +
                    "-fx-prompt-text-fill: rgba(255, 255, 255, 0.5);"
                );
            }
        });
        
        HBox.setHgrow(messageInput, Priority.ALWAYS);

        
        sendButton = new Button();
        try {
            Image sendIcon = new Image(getClass().getResourceAsStream("/images/send-icon.png"));
            ImageView sendImageView = new ImageView(sendIcon);
            sendImageView.setFitWidth(20);
            sendImageView.setFitHeight(20);
            sendImageView.setPreserveRatio(true);
            sendButton.setGraphic(sendImageView);
        } catch (Exception e) {
            sendButton.setText("➤"); // Fallback to unicode arrow
        }
        sendButton.setPrefHeight(40);
        sendButton.setPrefWidth(40);
        sendButton.setStyle(
            "-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #00BCFF 0%, #0080FF 100%);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 25px;" +
            "-fx-cursor: hand;" +
            "-fx-font-size: 14px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,188,255,0.4), 10, 0, 0, 3);"
        );
        sendButton.setOnMouseEntered(e -> applyHoverStyle(true));
        sendButton.setOnMouseExited(e -> applyHoverStyle(false));
        
        Runnable sendAction = this::handleSendMessage;
        sendButton.setOnAction(e -> sendAction.run());
        messageInput.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                sendAction.run();
            }
        });
        
        attachButton = new Button("+");
        attachButton.setPrefHeight(20);
        attachButton.setPrefWidth(20);
        attachButton.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.1);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 50%;" +
            "-fx-cursor: hand;" +
            "-fx-border-width: 2px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,188,255,0.2), 8, 0, 0, 2);"
        );

        
        attachButton.setOnAction(e -> handleFileAttach());
        
        inputArea.getChildren().addAll(messageInput , attachButton, sendButton);
        chatPane.getChildren().addAll(header, chatListView, inputArea);
    }

    /**
     * Corresponds to the original 'generateBotResponse' method and message handling.
     */
    private void handleSendMessage() {
        String message = messageInput.getText().trim();
        if (!message.isEmpty()) {
            String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            chatMessages.add(String.format("🟢 [%s] You: %s", timestamp, message));
            
         // Instead of rule-based response → call RAG
            String botResponse = ollamaService.ask(message);
            chatMessages.add(String.format("🤖 [%s] Zentriq: %s", timestamp, botResponse));
            
            messageInput.clear();
            chatListView.scrollTo(chatMessages.size() - 1);
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

    /**
     * Generates the welcome message for the chat based on the logged-in user.
     * Corresponds to the logic inside the original 'showChatInterface' method.
     */
    public String generateWelcomeMessage(String currentUser) {
        String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        return String.format("🤖 [%s] Assistant: Hello %s! Welcome to Zentriq. How can I help you today?", 
            timestamp, currentUser);
    }
    
    private void applyFocusStyle(boolean isNowFocused) {
        if (isNowFocused) {
        	sendButton.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-padding: 15px 20px;" +
                "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                "-fx-background-radius: 25px;" +
                "-fx-border-color: #00BCFF;" +
                "-fx-border-radius: 25px;" +
                "-fx-border-width: 2px;" +
                "-fx-text-fill: white;" +
                "-fx-prompt-text-fill: rgba(255, 255, 255, 0.5);" +
                "-fx-effect: dropshadow(gaussian, rgba(0,188,255,0.3), 10, 0, 0, 0);"
            );
        } else {
        	sendButton.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-padding: 15px 20px;" +
                "-fx-background-color: rgba(255, 255, 255, 0.1);" +
                "-fx-background-radius: 25px;" +
                "-fx-border-color: rgba(0, 188, 255, 0.3);" +
                "-fx-border-radius: 25px;" +
                "-fx-border-width: 2px;" +
                "-fx-text-fill: white;" +
                "-fx-prompt-text-fill: rgba(255, 255, 255, 0.5);"
            );
        }
    }
    
    private void applyHoverStyle(boolean isHovered) {
        if (isHovered) {
        	sendButton.setStyle(
                "-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #00D4FF 0%, #0090FF 100%);" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25px;" +
                "-fx-cursor: hand;" +
                "-fx-font-size: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,188,255,0.6), 15, 0, 0, 5);" +
                "-fx-scale-x: 1.05;" +
                "-fx-scale-y: 1.05;"
            );
        } else {
        	sendButton.setStyle(
                "-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #00BCFF 0%, #0080FF 100%);" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25px;" +
                "-fx-cursor: hand;" +
                "-fx-font-size: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,188,255,0.4), 10, 0, 0, 3);" +
                "-fx-scale-x: 1.0;" +
                "-fx-scale-y: 1.0;"
            );
        }
    }
    
    private void handleFileAttach() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Text Files", "*.txt"),
            new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
            new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(chatPane.getScene().getWindow());
        if (selectedFile != null) {
            try {
                List<Document> docs = CustomDocumentLoader.loadFiles(selectedFile.toPath());
                ollamaService.indexDocuments(docs);
                addMessage("📂 Loaded file: " + selectedFile.getName() + " (" + docs.size() + " chunks)");
            } catch (IOException ex) {
                ex.printStackTrace();
                addMessage("⚠️ Failed to load file: " + selectedFile.getName());
            }
        }
    }
}