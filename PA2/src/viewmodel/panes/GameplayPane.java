package viewmodel.panes;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Exceptions.InvalidMapException;
import model.LevelManager;
import viewmodel.AudioManager;
import viewmodel.MapRenderer;
import viewmodel.SceneManager;
import viewmodel.customNodes.GameplayInfoPane;

import java.util.Optional;

/**
 * Represents the gameplay pane in the game
 */
public class GameplayPane extends BorderPane {

    private final GameplayInfoPane info;
    private VBox canvasContainer;
    private Canvas gamePlayCanvas;
    private HBox buttonBar;
    private Button restartButton;
    private Button quitToMenuButton;

    /**
     * Instantiate the member components and connect and style them. Also set the callbacks.
     * Use 20 for the VBox spacing
     */
    public GameplayPane() {
        //TODO
        info=new GameplayInfoPane(LevelManager.getInstance().currentLevelNameProperty(),
                LevelManager.getInstance().curGameLevelExistedDurationProperty(),
                LevelManager.getInstance().getGameLevel().numPushesProperty(),
                LevelManager.getInstance().curGameLevelNumRestartsProperty());
        canvasContainer=new VBox(20);
        gamePlayCanvas=new Canvas();
        renderCanvas();
        buttonBar=new HBox();
        restartButton=new Button("Restart");
        quitToMenuButton=new Button("Quit to menu");
        connectComponents();
        styleComponents();
        setCallbacks();
    }

    /**
     * Connects the components together (think adding them into another, setting their positions, etc).
     */
    private void connectComponents() {
        //TODO
        canvasContainer.getChildren().add(gamePlayCanvas);
        buttonBar.getChildren().addAll(info, restartButton, quitToMenuButton);
        this.setCenter(canvasContainer);
        this.setBottom(buttonBar);
    }

    /**
     * Apply CSS styling to components.
     */
    private void styleComponents() {
        //TODO applycss?setstyle?
        canvasContainer.getStyleClass().add("big-vbox");
        canvasContainer.getStyleClass().add("-fx-background-color: #bbb");
        buttonBar.getStyleClass().add("big-hbox");
        buttonBar.getStyleClass().add("bottom-menu");
        buttonBar.getStyleClass().add("-fx-background-color: #eee");
        restartButton.getStyleClass().add("big-button");
        quitToMenuButton.getStyleClass().add("big-button");

    }

    /**
     * Set the event handlers for the 2 buttons.
     * <p>
     * Also listens for key presses (w, a, s, d), which move the character.
     * <p>
     * Hint: {@link GameplayPane#setOnKeyPressed(EventHandler)}  is needed.
     * You will need to make the move, rerender the canvas, play the sound (if the move was made), and detect
     * for win and deadlock conditions. If win, play the win sound, and do the appropriate action regarding the timers
     * and generating the popups. If deadlock, play the deadlock sound, and do the appropriate action regarding the timers
     * and generating the popups.
     */
    private void setCallbacks() {
        //TODO
        restartButton.setOnAction(event -> {
            doRestartAction();
        });
        quitToMenuButton.setOnAction(event -> {
            doQuitToMenuAction();
        });
        this.setOnKeyPressed(event->{
                boolean isMove = LevelManager.getInstance().getGameLevel().makeMove(event.getCode().getChar().toLowerCase().charAt(0));
                renderCanvas();
               if(isMove && AudioManager.getInstance().isEnabled())
                    AudioManager.getInstance().playMoveSound();
                if(LevelManager.getInstance().getGameLevel().isWin()) {
                    if(AudioManager.getInstance().isEnabled())
                        AudioManager.getInstance().playWinSound();
                    LevelManager.getInstance().resetLevelTimer();
                    createLevelClearPopup();
                }
                else if(LevelManager.getInstance().getGameLevel().isDeadlocked()) {
                    if(AudioManager.getInstance().isEnabled())
                        AudioManager.getInstance().playDeadlockSound();
                    LevelManager.getInstance().resetLevelTimer();
                    createDeadlockedPopup();
                }
            });
    }

    /**
     * Called when the tries to quit to menu. Show a popup (see the documentation). If confirmed,
     * do the appropriate action regarding the level timer, level number of restarts, and go to the
     * main menu scene.
     */
    private void doQuitToMenuAction() {
        //TODO
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION, "Game progress will be lost.");
        alert.setHeaderText("Return to menu?");
        alert.setTitle("Confirm");
        Optional<ButtonType> result=alert.showAndWait();
        if(result.isPresent() && result.get()==ButtonType.OK) {
            LevelManager.getInstance().resetLevelTimer();
            LevelManager.getInstance().resetNumRestarts();
            SceneManager.getInstance().showMainMenuScene();
        }
        else if(result.isPresent() && result.get()==ButtonType.CANCEL)
            alert.close();
    }

    /**
     * Called when the user encounters deadlock. Show a popup (see the documentation).
     * If the user chooses to restart the level, call {@link GameplayPane#doRestartAction()}. Otherwise if they
     * quit to menu, switch to the level select scene, and do the appropriate action regarding
     * the number of restarts.
     */
    private void createDeadlockedPopup() {
        //TODO
        ButtonType restartButton=new ButtonType("Restart");
        ButtonType returnButton = new ButtonType("Return");
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION, "", restartButton, returnButton);
        alert.setHeaderText("Level deadLocked!");
        alert.setTitle("Confirm");
        Optional<ButtonType> result=alert.showAndWait();
        if(result.isPresent() && result.get()==restartButton) {
            doRestartAction();
        }
        else if(result.isPresent() && result.get()==returnButton) {
            SceneManager.getInstance().showLevelSelectMenuScene();
            LevelManager.getInstance().resetNumRestarts();
        }
    }

    /**
     * Called when the user clears the level successfully. Show a popup (see the documentation).
     * If the user chooses to go to the next level, set the new level, rerender, and do the appropriate action
     * regarding the timers and num restarts. If they choose to return, show the level select menu, and do
     * the appropriate action regarding the number of level restarts.
     * <p>
     * Hint:
     * Take care of the edge case for when the user clears the last level. In this case, there shouldn't
     * be an option to go to the next level.
     */
    private void createLevelClearPopup() {
        //TODO ??
        ButtonType nextLvB=new ButtonType("Next level");
        ButtonType returnButton = new ButtonType("Return");
        Alert alert;
        Optional<ButtonType> result;
        if(LevelManager.getInstance().getNextLevelName()!=null) {
            alert = new Alert(Alert.AlertType.CONFIRMATION, "", nextLvB, returnButton);
            alert.setTitle("Confirm");
            alert.setHeaderText("Level cleared!");
            result=alert.showAndWait();
        }
        else {
            alert = new Alert(Alert.AlertType.CONFIRMATION, "", returnButton);
            alert.setTitle("Confirm");
            alert.setHeaderText("Level cleared!");
            result = alert.showAndWait();
        }
        if(result.isPresent() && result.get()==nextLvB) {
            try {
                LevelManager.getInstance().setLevel(LevelManager.getInstance().getNextLevelName());
                LevelManager.getInstance().startLevelTimer();
                LevelManager.getInstance().resetNumRestarts();
                LevelManager.getInstance().getGameLevel().numPushesProperty().set(0);
                renderCanvas();
                SceneManager.getInstance().showGamePlayScene();
            }catch (InvalidMapException e){
                e.printStackTrace();
            }
        }
        else if(result.isPresent() && result.get()==returnButton){
            SceneManager.getInstance().showLevelSelectMenuScene();
            LevelManager.getInstance().resetNumRestarts();
        }
    }

    /**
     * Set the current level to the current level name, rerender the canvas, reset and start the timer, and
     * increment the number of restarts
     */
    private void doRestartAction() {
        //TODO
        try{
            LevelManager.getInstance().setLevel(LevelManager.getInstance().currentLevelNameProperty().getValue());
            renderCanvas();
            LevelManager.getInstance().resetLevelTimer();
            LevelManager.getInstance().startLevelTimer();
            LevelManager.getInstance().getGameLevel().numPushesProperty().set(0);
            LevelManager.getInstance().incrementNumRestarts();
            SceneManager.getInstance().showGamePlayScene();
        }catch(InvalidMapException e){
            e.printStackTrace();
        }
    }

    /**
     * Render the canvas with updated data
     * <p>
     * Hint: {@link MapRenderer}
     */
    private void renderCanvas() {
        //TODO //??
        MapRenderer.render(gamePlayCanvas, LevelManager.getInstance().getGameLevel().getMap().getCells());
    }
}
