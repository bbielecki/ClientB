package sample;
import com.healthmarketscience.rmiio.RemoteRetry;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javax.swing.*;
import javafx.event.*;
import java.io.IOException;
import java.util.Date;

public class ServerTable {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty FileName;
    private final SimpleStringProperty extension;
    private final SimpleStringProperty lastModified;
    private final SimpleLongProperty Size;
    private final SimpleStringProperty version;

    public ServerTable(int id, String filename, String extension, String lastModified, long size, String version){
        this.id = new SimpleIntegerProperty(id);
        this.FileName = new SimpleStringProperty(filename);
        this.extension = new SimpleStringProperty(extension);
        this.lastModified = new SimpleStringProperty(lastModified);
        this.Size = new SimpleLongProperty(size);
        this.version = new SimpleStringProperty(version);

    }

    public Integer getId(){
        return id.get();
    }

    public String getFileName(){
        return FileName.get();
    }

    public String getExtension(){
        return extension.get();
    }

    public Long getSize(){
        return Size.get();
    }

    public String getVersion(){
        return version.get();
    }

    public String getLastModified() {return lastModified.get();}

    public void setFileName(String v){ FileName.set(v);}
    public void setExtension(String v){
        extension.set(v);
    }
    public void setSize(Long v){
        Size.set(v);
    }
    public void setVersion(String v){
        version.set(v);
    }
    public void setLastModified(String v) {lastModified.set(v);}
}