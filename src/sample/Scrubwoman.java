package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by Bartłomiej on 21.11.2016.
 */

public class Scrubwoman {

    public Scrubwoman(){
        try(Stream<Path> paths = Files.walk(Paths.get("D:\\Client"))) {
            List<String> deletedFilesNames = null;
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {

                    String fPath = filePath.toString();
                    String[] fileNameParts = fPath.split("\\.");
                    String fileNames[] = fileNameParts[0].split(Pattern.quote("\\"));
                    String fileName = fileNames[2];
                    String[]versionParts = fileNameParts[1].split("-");
                    String extension = versionParts[0];
                    String version = versionParts[1].substring(versionParts[1].lastIndexOf("v")+1);
                    fileName = fileName + "." + extension;

                    File f = filePath.toFile();
                    Date dt = new Date(f.lastModified());

                    try {
                        if(BackupClient.server.checkFileOnServer(fileName, dt)) {
                            if (f.length() != BackupClient.server.getFileSize(fileName, version)) {
                                f.delete();
                                deletedFilesNames.add(fileName);
                            }
                        }

                        System.out.println(filePath);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                if(deletedFilesNames!=null) {
                    Alert dialog = new Alert(Alert.AlertType.ERROR, "Confirm", ButtonType.OK, ButtonType.CANCEL);
                    dialog.setHeaderText("Files downloading error");
                    dialog.setContentText("Files not properly dowloaded:");

                    for (String n : deletedFilesNames)
                        dialog.setContentText(n);

                    dialog.setResizable(true);
                    dialog.getDialogPane().setPrefSize(250, 100);
                    dialog.showAndWait();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}