package sample;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


public class progressBarController implements Initializable{
    @FXML private ProgressBar prg;
    @FXML private Label info_label;
    @FXML private Label sent_label;
    @FXML private Button ok_button;

    /*
    public static long[] fsize = new long[3];
    public static int order = 0;
    public static int counter = 0;
    public static int amountOfFiles = 0;
    public static String[] filename = {"1", "1"};
    */

    public static int SendOrGet = 0;
    public static int order = 0;
    public static int counter = 0;
    public static int amountOfFiles = 0;
    public static ArrayList<Long> fsize = new ArrayList<>();
    public static ArrayList<String> filename = new ArrayList<>();



    public void controlProgress(){
        final int realAmount = amountOfFiles;

        Runnable task = () -> {
            while (amountOfFiles!=0){
                if(SendOrGet == 0){
                    long progressSize = 0L;
                    order++;
                    info_label.setText("Sending files " + filename.get(counter) + "... (" + order + " of " + realAmount+ ")");
                    while (progressSize<fsize.get(counter)){
                        try {
                            progressSize=(BackupClient.getServer().getChunk()*4096);
                            double coeff = (progressSize*1.0/fsize.get(counter)*1.0);
                            prg.setProgress(coeff);
                            Thread.sleep(10);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    sent_label.setVisible(true);
                    ok_button.setVisible(true);
                    try{
                        BackupClient.getServer().resetChunks();
                        Thread.sleep(3000);
                        Platform.runLater(() -> {
                            sent_label.setVisible(false);
                        });
                    }
                    catch (Exception e){
                        e.getMessage();
                    }
                    //Platform.runLater(() -> ok_button.fire());
                    decreaseFiles();
                    counter++;

                }
                else{
                    long progressSize = 0L;
                    System.out.println("ROZMIAR PLIKU: " + fsize.get(counter));
                    order++;
                    info_label.setText("Downloading file " + filename.get(counter) + "... (" + order + " of " + realAmount+ ")");
                    while (progressSize<fsize.get(counter)){
                        try {
                            progressSize=(BackupClient.clientChunks*4096);
                            double coeff = (progressSize*1.0/fsize.get(counter)*1.0);
                            prg.setProgress(coeff);
                            Thread.sleep(10);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    sent_label.setVisible(true);
                    ok_button.setVisible(true);
                    try{
                        BackupClient.resetClientChunks();
                        Thread.sleep(3000);
                        Platform.runLater(() -> {
                            sent_label.setVisible(false);
                        });
                    }
                    catch (Exception e){
                        e.getMessage();
                    }
                    //Platform.runLater(() -> ok_button.fire());
                    decreaseFiles();
                    counter++;

                }
            }

        };

        Thread t = new Thread(task);
        t.start();

    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ok_button.setOnAction(e -> {
            Stage stage = (Stage) ok_button.getScene().getWindow();
            stage.close();
        });
        controlProgress();
    }

    public void decreaseFiles(){
        amountOfFiles--;
    }
}