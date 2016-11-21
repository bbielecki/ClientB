package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

//cala klasa
public class TimerController implements Initializable{

    @FXML
    private ComboBox timeComboBox;

    List<File> periodicChosenFiles, chosenFiles;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<String> periods = FXCollections.observableArrayList("half an hour","hour","2 hour","3 hour","6 hour","12 hour","1 day");
        timeComboBox.setItems(periods);

    }


    private long getPeriodTime(){
        long time = 0L;
        switch (timeComboBox.getSelectionModel().getSelectedItem().toString()){
            case "half an hour":{
                time = (long)(0.5*60*60*100);
                break;
            }
            case "hour":{
                time = (long)(60*60*100);
                break;
            }
            case "2 hours":{
                time = (long)(2*60*60*100);
                break;
            }
            case "3 hours":{
                time = (long)(3*60*60*100);
                break;
            }
            case "6 hours":{
                time = (long)(6*60*60*100);
                break;
            }
            case "12 hours":{
                time = (long)(12*60*60*100);
                break;
            }
            case "1 day":{
                time = (long)(24*60*60*100);
                break;
            }

        }
        return time;

    }

    public void getChosenPeriod(ActionEvent event) throws IOException {

        Date date = new Date();
        long period = getPeriodTime();
        selectFiles(2);

        BackupTimer bT = new BackupTimer(date, period, periodicChosenFiles);
        Thread timer = new Thread(bT);
        timer.start();
    }

    private void selectFiles(int a){
        Stage selectingFilesStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Choose file(s)...");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        //fileChooser.showOpenDialog(selectingFilesStage);

        if(a==1)
            chosenFiles = fileChooser.showOpenMultipleDialog(selectingFilesStage);
        else
            periodicChosenFiles = fileChooser.showOpenMultipleDialog(selectingFilesStage);

    }
}