package sample;

import com.sun.javafx.scene.layout.region.Margins;

import java.io.Console;
import java.io.File;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

/**
 * Created by Bartłomiej on 14.11.2016.
 */

//Cala klasa
   // metoda send przyjmuje inne argumenty niz u ciebie!
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
                        BackupClient.send(BackupClient.server,f.getPath(),f.getPath(),BackupClient.getFileExtension(f),f.lastModified());
                    }
                    catch (RemoteException re){
                        re.getMessage();
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
