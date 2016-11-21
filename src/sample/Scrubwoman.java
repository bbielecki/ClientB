package sample;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by Bartłomiej on 21.11.2016.
 */
public class Scrubwoman {

    public Scrubwoman(){
        try(Stream<Path> paths = Files.walk(Paths.get("D:\\Client"))) {
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

                    try {
                        if(f.length()!=BackupClient.server.getFileSize(fileName,version))
                            f.delete();
                        else
                            System.out.println("jest ok");

                        System.out.println(filePath);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
