package ch.sky.web;

import ch.sky.web.nfc.NdefUltralightTagScanner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker.State;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.Optional;

public class WebApp extends Application {

    private static final String PROTOCOL = "http://";
    private static String URL = "localhost";
    private static final String PATH = "/sky/SKY_MANIFEST_GUI/";
    private static final String INDEX = "index.php";
    private final ProgressBar loadProgress = new ProgressBar();
    private WebEngine webEngine;
    private boolean manifestPresent = false;
    NdefUltralightTagScanner ndefUltralightTagScanner;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    // Stage ist nur Final, weil ich es fuer das Btn-Event brauche!
    @Override
    public void start(final Stage primaryStage) {
        this.primaryStage = primaryStage;

        try {
            startGui();

        } catch (Exception e){
            stopGui();
            startGui();
        }
    }

    @Override
    public void stop() throws Exception {
        stopGui();
        startGui();
    }

    private void startGui() {
        //Initialize Cards Scanner

        ndefUltralightTagScanner = new NdefUltralightTagScanner(this);

        //Initialize WebView
        createGUI(this.primaryStage);

        //Properties Listener
        //If index.php again no more group Manifesting
        webEngine.getLoadWorker().stateProperty().addListener((ChangeListener<State>) (ov, oldState, newState) -> {
            if (newState == State.SUCCEEDED) {
                if (webEngine.getLocation().contains(INDEX)) {
                    manifestPresent = false;
                }
            }
        });
    }


    private void stopGui() {
        ndefUltralightTagScanner = null;
        webEngine = null;
    }

    private void createGUI(Stage primaryStage) {
        final WebView browser = new WebView();
        webEngine = browser.getEngine();
        webEngine.load(getFullURL());
        loadProgress.progressProperty().bind(webEngine.getLoadWorker().progressProperty());

        TextField urlField = new TextField();
        urlField.setText(URL);
        urlField.setVisible(false);
        urlField.textProperty().addListener((observable, oldValue, newValue) -> {
            URL = newValue;
        });

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(browser);

        Button set = new Button("URL");
        set.setOnAction(e -> urlField.setVisible(!urlField.isVisible()));

        Button reload = new Button("Reload");
        reload.setOnAction(e -> webEngine.load(getFullURL()));

        Button options = new Button("Full Screen");
        options.setOnAction(e -> primaryStage.setFullScreen(!primaryStage.isFullScreen()));

        HBox hbox = new HBox(set, reload, options, loadProgress,  urlField);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SKY DZM");
        primaryStage.show();
    }

    /**
     * Calls URL with Card ID
     * Either new Manifesting or if already present then group Manifesting
     *
     * @param cardId
     */
    public void callURL(String cardId) {
        Platform.runLater(() -> {
            if (manifestPresent) {
                webEngine.executeScript(String.format("javascript:addGroupMember(%s)", cardId));
            } else {
                webEngine.load(String.format(getFullURL() + "manifest.php?id=%s", cardId));
                manifestPresent = true;
            }
        });
    }

    private String getFullURL() {
        return PROTOCOL + URL + PATH;
    }

    public void showError(Throwable throwable) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText("Fehler beim Starten");
        alert.setContentText("Bitte sicherstellen dass das NFC-Lesegerät richtig eingesteck ist");
        alert.showAndWait();
    }
}