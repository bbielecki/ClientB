package sample;

import com.sun.javafx.scene.SceneUtils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;


public class progressBarController implements Initializable{
    @FXML private ProgressBar prg;
    @FXML private Label info_label;

    public static long fsize = 0;

    public void controlProgress(long fsize){
        Runnable task = () -> {
            long progressSize = 0L;
            System.out.println("ROZMIAR PLIKU: " + fsize);
            while (progressSize<fsize){
                try {
                    progressSize=(BackupClient.getNumberOfChunks(BackupClient.getServer().chunkStream())*4096);
                    //progressSize=(BackupClient.getServer().getChunk()*4096);
                    double coeff = (progressSize*1.0/fsize*1.0);
                    System.out.println("Stosunek: " + coeff);
                    prg.setProgress(coeff);
                    Thread.sleep(250);

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t = new Thread(task);
        t.start();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controlProgress(fsize);
    }

}