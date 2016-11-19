package sample;

import javafx.scene.control.ProgressBar;

/**
 * Created by Bartłomiej on 19.11.2016.
 */
public class ProgressBarThread implements Runnable {

    long progressSize = 0;
    long fileSize = 0;
    ProgressBar progressBar;

    ProgressBarThread(long fS, ProgressBar pB){

        fileSize = fS;
        progressBar = pB;
    }

    @Override
    public void run() {
        while (progressSize<fileSize){
            progressSize=(BackupClient.numberOfChunks*4096);
            progressBar.setProgress(progressSize/fileSize);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
