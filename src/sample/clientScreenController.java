package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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


    @FXML
    private TextField port_textfield;

    @FXML private MenuItem upload_menuitem;
    @FXML private MenuItem download_menuitem;

    @FXML private Menu file_menu;
    @FXML private Menu about_menu;
    @FXML private Menu info_menu;
    @FXML private MenuItem periodic;

    private List<File> chosenFiles;
    private List<File> periodicChosenFiles;

    public void handleUpload(ActionEvent event) throws IOException{

        selectFiles(1);

        System.out.println(chosenFiles);

        try {
            if (chosenFiles != null) {
                for (File f : chosenFiles) {
                    System.out.println(f.getName() + " " + f.lastModified());
                  //  if (!(BackupClient.getServer().checkFileOnServer(f.getName(), f.lastModified()))) {
                        BackupClient.send(f.getPath());
                        System.out.println("Przesłano plik: " + f.getName() + " " + "!");
                  //  }

                }
            }
        } catch (Exception e) {
            System.out.println(e.getCause().getMessage());
        }

    }

    public File getChosenFile(int index){
        return chosenFiles.get(index);
    }
    public File getChosenFile(){
        return chosenFiles.remove(0);
    }
    public int getNumberOfChosenFiles(){return chosenFiles.size();}

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

    @Override
    public void initialize(URL location, ResourceBundle resources){

        upload_menuitem.setAccelerator(new KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN));
        download_menuitem.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
        periodic.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        filenameColumn.setCellValueFactory(new PropertyValueFactory<>("FileName"));
        extensionColumn.setCellValueFactory(new PropertyValueFactory<>("Extension"));
        lastModifiedColumn.setCellValueFactory(new PropertyValueFactory<>("lastModified"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        versionColumn.setCellValueFactory(new PropertyValueFactory<>("Version"));

        table.setItems(data);
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
        else
            periodicChosenFiles = fileChooser.showOpenMultipleDialog(selectingFilesStage);

    }



}
