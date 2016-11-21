package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;


public class BackupTimer implements Runnable{

    private long periodicDate;
    private long period;
    private List<File> periodFiles;

    public BackupTimer(Date d, long pT, List<File> f){
        period = pT;
        periodicDate = d.getTime();
        periodFiles = f;
    }

    @Override
    public void run() {
        while (true) {
            Date date = new Date();

            if (date.getTime() >= periodicDate) {

                for(File f : periodFiles) {
                    try {
                        Date dt = new Date(f.lastModified());
                        if (!(BackupClient.getServer().checkFileOnServer(f.getName(), dt)))
                            BackupClient.send(BackupClient.server,f.getPath(),f.getName(),BackupClient.getFileExtension(f),f.lastModified());
                    }
                    catch (RemoteException re){
                        re.getMessage();
                    }
                    Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, "Confirm", ButtonType.OK, ButtonType.CANCEL);
                    dialog.setHeaderText("Periodic backup succesful");
                    dialog.setContentText("File" + f.getName() + " been sent succesfully!");
                    dialog.setResizable(true);
                    dialog.getDialogPane().setPrefSize(250, 100);
                    dialog.showAndWait();
                    if(dialog.getResult() == ButtonType.OK){
                        dialog.close();
                    }
                }

                periodicDate = date.getTime() + period;
            }

            System.out.println("watek ruszyl");
            try {
                Thread.sleep(period / 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}