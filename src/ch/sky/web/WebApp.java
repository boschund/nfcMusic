package ch.sky.web;

import ch.sky.web.nfc.NdefUltralightTagScanner;
import ch.sky.web.rfid.RfidListener;
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
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;
import org.nfctools.mf.MfException;

import javax.smartcardio.CardException;
import java.util.Optional;

public class WebApp extends Application {
    private static final Logger log = LogManager.getLogger(WebApp.class.getName());


    private static final String PROTOCOL = "http://";
//    private static String URL = "localhost";
    private static String URL = "192.168.0.113";
    private static final String PATH = "/sky/sky_manifest_gui/";
    private static final String INDEX = "index.php";
    private final ProgressBar loadProgress = new ProgressBar();
    private WebEngine webEngine;
    private boolean manifestPresent = false;
    NdefUltralightTagScanner ndefUltralightTagScanner;
    private Stage primaryStage;
    RadioButton r1;
    private boolean newPsAlreadyIn;
    private RfidListener rfidListener;
    private boolean alreadyShow;

    public static void main(String[] args) {
        launch(args);
    }

    // Stage ist nur Final, weil ich es fuer das Btn-Event brauche!
    @Override
    public void start(final Stage primaryStage) {
        new Util_NfcSession(this);
        alreadyShow = false;

        this.primaryStage = primaryStage;

//        this.primaryStage.initStyle(StageStyle.UNDECORATED);
//        this.primaryStage.setMaximized(true);

        try {
            startGui();
        } catch (Exception e){
            log.error("Error {}", e);
            stopGui();
            startGui();
        }
    }

    @Override
    public void stop() throws Exception {
        rfidListener.stop();
        stopGui();
    }

    private void startGui() {
        //Initialize Cards Scanner

        newPsAlreadyIn = false;

        new Util_NfcSession(this);
        rfidListener = new RfidListener();
//        ndefUltralightTagScanner = new NdefUltralightTagScanner(this);

        //Initialize WebView
        createGUI(this.primaryStage);

        //Properties Listener
        //If index.php again no more group Manifesting
        webEngine.getLoadWorker().stateProperty().addListener((ChangeListener<State>) (ov, oldState, newState) -> {
            if (newState == State.SUCCEEDED) {
                log.info("{}",webEngine.getLocation());

                if (webEngine.getLocation().contains(INDEX)) {
                    manifestPresent = false;
                    newPsAlreadyIn = false;
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


        ToggleGroup toggleGroup = new ToggleGroup();

        r1 = new RadioButton("U");
        r1.setToggleGroup(toggleGroup);
        RadioButton r2 = new RadioButton("JS");
        r2.setToggleGroup(toggleGroup);
        toggleGroup.selectToggle(r1);

        //no more visible at bottom
        Button set = new Button("URL");
        set.setOnAction(e -> urlField.setVisible(!urlField.isVisible()));

        Button home = new Button("HOME");
        home.setOnAction(e -> webEngine.load(getFullURL()));

        //no more visible at bottom
        Button options = new Button("Full Screen");
        options.setOnAction(e -> primaryStage.setFullScreen(!primaryStage.isFullScreen()));

        HBox hbox = new HBox(home, set, loadProgress,  urlField);
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
                showNotification("Ausloggen", "Bitte zuerst ausloggen bevor sich eine neue Person Manifestiert");
            } else {
                log.info("New Person: {}", cardId);
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

    public void showRFIDError(Throwable e) {
        if (!alreadyShow) {
            alreadyShow = true;
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fehler");
                alert.setHeaderText("Fehler beim Starten");
                alert.setContentText(e.getMessage());
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    alreadyShow = false;
                }
            });
        }
    }


    private void restart() {
        log.error("Restart GUI");
        if (rfidListener != null) {
            rfidListener.stop();
            rfidListener = null;
        }
        rfidListener = new RfidListener();
    }

    public void showNotification() {
        showNotification("Karte konnte nicht gelesen werden", "Bitte melden sie Sich beim Sekretariat");
    }

    public void showNotification(String title, String text) {
        Platform.runLater(() -> {
            log.info("Show Notification: {}, {}", title, text);
            Notifications.create()
                    .title(title)
                    .text(text)
                    .hideAfter(Duration.seconds(10))
                    .show();
        });
    }

    public void showError(Exception e) {
        if (!alreadyShow) {
            alreadyShow = true;
            log.error(e);
            if (e instanceof MfException) {
                showNotification();
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                alreadyShow = false;
                            }
                        },
                        5000
                );
            } else if (e instanceof CardException) {
                Platform.runLater(() -> {
                    showNoDeviceAlert();
                    //FIXME restart hard
                    restart(); //does not work with reader
                });
            } else {
                //FIXME do what?
                restart();
            }
        }
    }

    public void showNoDeviceAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText("Lesegerät konnte nicht gefunden werden");
        alert.setContentText("Bitte Lesegerät verbinden");
        alert.showAndWait();
    }

    public void loadCard(String cardString) {
        Platform.runLater(() -> {
            log.info("Found card: {}", cardString);
            callURL(cardString);
        });
    }
}