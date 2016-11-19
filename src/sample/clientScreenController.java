package sample;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


//handle upload podzielone (funkcjaselectFIles)
//periodic w metodzie initialize
//metoda periodicAction
//
public class clientScreenController implements Initializable{
    @FXML
    private TableView<ServerTable> table;

    @FXML
    private TableColumn<ServerTable, Integer> idColumn;

    @FXML
    private TableColumn<ServerTable,String> filenameColumn;

    @FXML
    private TableColumn<ServerTable,String> extensionColumn;

    @FXML
    private TableColumn<ServerTable,Long> sizeColumn;

    @FXML
    private TableColumn<ServerTable,String> lastModifiedColumn;

    @FXML
    private TableColumn<ServerTable,String> versionColumn;

    @FXML private TableColumn<ServerTable,String> pathColumn;

    @FXML
    private ObservableList<ServerTable> data;

    @FXML
    private TextField port_textfield;

    @FXML private MenuItem upload_menuitem;
    @FXML private MenuItem download_menuitem;
    @FXML private Button show_button;
    @FXML private Button get_button;

    @FXML private Menu file_menu;
    @FXML private Menu about_menu;
    @FXML private Menu info_menu;
    @FXML private MenuItem periodic;

    @FXML ProgressBar progress;

    private List<File> chosenFiles;

    public void handleUpload(ActionEvent event) throws IOException{

        selectFiles(1);
        createProgressBarWindow();

        System.out.println(chosenFiles);

        try {
            if (chosenFiles != null) {
                for (File f : chosenFiles) {
                    //controlProgress(f.length());
                    System.out.println(f.getName() + " " + f.lastModified());
                    Date dt = new Date(f.lastModified());
                    if (!(BackupClient.server.checkFileOnServer(f.getName(), dt))) {
                        controlProgress(f.length());
                        BackupClient.fileSize=f.length();

                        BackupClient.send(BackupClient.server, f.getPath(), f.getName(), BackupClient.getFileExtension(f), f.lastModified());
                        System.out.println("Przesłano plik: " + f.getName() + " " + "!");
                    }
                    else{
                        //TODO okienko!
                        System.out.println("Niestety plik już jest na serwerze!");
                    }

                }
            }
        } catch (Exception e) {
            System.out.println(e.getCause().getMessage());
        }
        //pc.controlProgress(1);
    }

    public File getChosenFile(int index){
        return chosenFiles.get(index);
    }
    public File getChosenFile(){
        return chosenFiles.remove(0);
    }
    public int getNumberOfChosenFiles(){return chosenFiles.size();}

    public void getButtonAction(ActionEvent event) throws RemoteException {
        //TODO jeśli cokolwiek zaznaczone
        String pathToGet = table.getSelectionModel().getSelectedItem().getPath();
        System.out.println(pathToGet);
        String filename = table.getSelectionModel().getSelectedItem().getFileName() + "-v" +
                table.getSelectionModel().getSelectedItem().getVersion();
        System.out.println(filename);
        try{
            BackupClient.getFile(BackupClient.server.passAStream(pathToGet),filename);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

/*
    private int iNumber = 1;

    private final ObservableList<ServerTable> data = FXCollections.observableArrayList(
            new ServerTable(iNumber++,"lol",".jpg","2015-12-12",1212122,"2.0"),
            new ServerTable(iNumber++,"be",".png","2015-12-12", 20102121, "2.1"),
            new ServerTable(iNumber++,"l",".jpg","2015-12-12",1212122,"2.0"),
            new ServerTable(iNumber++,"b.",".png","2015-12-12", 20102121, "2.1")
    );


    public void addRow(int id, String filename, String extension, String lastModified, long size, String version){
        data.add(new ServerTable(iNumber++,filename,extension,lastModified,size,version));
        table.setItems(data);
    }
*/
    public void showButtonAction(ActionEvent event){
        try {
            RemoteInputStream ris = BackupClient.server.tableStream();
            data = BackupClient.getTable(ris);
            table.getItems().clear();
            table.getItems().addAll(data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){

        upload_menuitem.setAccelerator(new KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN));
        download_menuitem.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
        periodic.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));

        filenameColumn.setCellValueFactory(new PropertyValueFactory<>("FileName"));
        lastModifiedColumn.setCellValueFactory(new PropertyValueFactory<>("lastModified"));
        versionColumn.setCellValueFactory(new PropertyValueFactory<>("version"));
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("path"));

        //table.setItems(data);

        Platform.runLater(() -> {
            try {
                RemoteInputStream ris = BackupClient.server.tableStream();
                data = BackupClient.getTable(ris);
                table.getItems().clear();
                table.getItems().addAll(data);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public void periodicAction(ActionEvent event) throws IOException{

        try {
            Parent timeScreen = FXMLLoader.load(getClass().getResource("timeScreen .fxml"));
            Scene timeScene = new Scene(timeScreen);
            Stage timeStage = new Stage();
            timeStage.setTitle("Period Chooser");
            timeStage.setScene(timeScene);
            timeStage.show();
        }
        catch (Exception e){
            e.getMessage();
        }


    }


    private void selectFiles(int a){
        Stage selectingFilesStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file(s)...");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))
        );
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
    }

    public void createProgressBarWindow(){
        try {
            Parent timeScreen = FXMLLoader.load(getClass().getResource("progressBar.fxml"));
            Scene timeScene = new Scene(timeScreen);
            Stage timeStage = new Stage();
            timeStage.setTitle("Upload progress");
            timeStage.setScene(timeScene);
            timeStage.show();



        }
        catch (Exception e){
            e.getMessage();
        }
    }

    public void controlProgress(long fsize){

        ProgressBarThread PBT = new ProgressBarThread(fsize, progress);

        Thread progressThread = new Thread(PBT);
        progressThread.start();

    }



}
