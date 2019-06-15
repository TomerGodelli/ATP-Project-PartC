package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.Optional;

import static javafx.scene.media.MediaPlayer.INDEFINITE;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        MyModel model = new MyModel();
        model.startServers();
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);
        //--------------
        primaryStage.setTitle("Operation Grandma");


        //create welcome scene
        FXMLLoader fxmlLoader1 = new FXMLLoader();
        Parent rootWelcome = fxmlLoader1.load(getClass().getResource("welcome.fxml").openStream());
        Scene welcomeScene = new Scene(rootWelcome, 560, 330);
        welcomeScene.getStylesheets().add(getClass().getResource("WelcomeStyle.css").toExternalForm());

        //create main display scene
        FXMLLoader fxmlLoader2 = new FXMLLoader();
        Parent rootMaze = fxmlLoader2.load(getClass().getResource("MyView.fxml").openStream());
        Scene mazeScene = new Scene(rootMaze, 800, 900);
        mazeScene.getStylesheets().add(getClass().getResource("mainDisplay.css").toExternalForm());

        primaryStage.setScene(welcomeScene);


        startMusic();


        //--------------
        MyViewController view = fxmlLoader2.getController();
        WelcomeView yaaraView = fxmlLoader1.getController();
        yaaraView.setMazeScene(mazeScene);
        yaaraView.setMazeView(view);
        //--------------------------
        view.setResizeEvent(mazeScene);
        view.setViewModel(viewModel);
        viewModel.addObserver(view);
        //--------------
        SetStageCloseEvent(primaryStage, model);
        view.SetScrollEvent(mazeScene);
        view.setDragEvent(mazeScene);

        primaryStage.getIcons().add(new Image("file:resources/Images/krembo_icon.png"));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void SetStageCloseEvent(Stage primaryStage, MyModel model) {
        primaryStage.setOnCloseRequest(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to exit?");
            alert.initStyle(StageStyle.UTILITY);

            Optional<ButtonType> result = alert.showAndWait();
            // ... user chose CANCEL or closed the dialog
            if (result.get() == ButtonType.OK) {
                // ... user chose OK
                model.stopServers();
                // Close program
            } else e.consume();
        });


    }

    private void startMusic() {
        String musicFile = "resources/Music/opening.mp3";     // For example
        Media sound = new Media(new File(musicFile).toURI().toString());
        WelcomeView.mediaPlayer = new MediaPlayer(sound);
        WelcomeView.mediaPlayer.setCycleCount(INDEFINITE);
        WelcomeView.mediaPlayer.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
