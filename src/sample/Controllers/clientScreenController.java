package sample.Controllers;

import com.healthmarketscience.rmiio.RemoteInputStream;
import javafx.application.Platform;
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
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;
import org.apache.commons.io.FileUtils;
import sample.BackupClient;
import sample.ServerTable;

public class clientScreenController implements Initializable {
    //region COMPONENTS
    @FXML
    private TableView<ServerTable> table;

    @FXML
    private TableColumn<ServerTable, String> filenameColumn;

    @FXML
    private TableColumn<ServerTable, String> lastModifiedColumn;

    @FXML
    private TableColumn<ServerTable, String> versionColumn;

    @FXML
    private TableColumn<ServerTable, String> pathColumn;

    @FXML
    private MenuItem upload_menuitem;

    @FXML
    private MenuItem help_menuitem;
    @FXML
    private MenuItem periodic;
    @FXML
    private Button show_button;
    @FXML
    private Button get_button;
    @FXML
    private Button delete_button;

    @FXML
    private ObservableList<ServerTable> data;
    @FXML
    private Menu file_menu;
    @FXML
    private Menu about_menu;
    @FXML
    private Menu info_menu;
    private List<File> chosenFiles;
    //endregion


    public void handleUpload(ActionEvent event) throws Exception {
        Stage selectingFilesStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file(s)...");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );


        chosenFiles = fileChooser.showOpenMultipleDialog(selectingFilesStage);
        System.out.println(chosenFiles);

        progressBarController.SendOrGet = 0;
        progressBarController.fsize.clear();
        progressBarController.filename.clear();
        progressBarController.order = 0;
        progressBarController.counter = 0;

        for (int i = 0; i < chosenFiles.size(); i++) {
            progressBarController.fsize.add(chosenFiles.get(i).length());
            progressBarController.amountOfFiles = chosenFiles.size();
            progressBarController.filename.add(chosenFiles.get(i).getName());
        }


        if (chosenFiles != null) {

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (File f : chosenFiles) {

                        Date dt = new Date(f.lastModified());
                        try {
                            if (!(BackupClient.getServer().checkFileOnServer(f.getName(), dt))) {
                                BackupClient.send(BackupClient.getServer(), f.getPath(), f.getName(), BackupClient.getFileExtension(f), f.lastModified());
                            } else {
                                Alert dialog = new Alert(Alert.AlertType.INFORMATION, "Confirm", ButtonType.OK, ButtonType.CANCEL);
                                dialog.setHeaderText("File exists on the server");
                                dialog.setContentText("File " + f.getName() + " is already on the server!");
                                dialog.setResizable(true);
                                dialog.getDialogPane().setPrefSize(250, 100);
                                dialog.showAndWait();
                            }

                        } catch (Exception e) {
                            e.getMessage();
                        }

                    }
                }
            });
            t.start();
            createProgressBarWindow();
        }
    }

    public void showButtonAction(ActionEvent event) {
        try {
            RemoteInputStream ris = BackupClient.getServer().tableStream();
            data = BackupClient.getTable(ris);
            table.getItems().clear();
            table.getItems().addAll(data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getButtonAction(ActionEvent event) throws RemoteException, IOException {
        if (table.getSelectionModel().getSelectedItem() != null) {
            String pathToGet = table.getSelectionModel().getSelectedItem().getPath();
            System.out.println(pathToGet);
            String filenameb = table.getSelectionModel().getSelectedItem().getFileName();
            String filename = table.getSelectionModel().getSelectedItem().getFileName() + "-v" +
                    table.getSelectionModel().getSelectedItem().getVersion();
            System.out.println(filename);
            try {
                if (!checkFile(filename)) {
                    progressBarController.amountOfFiles = 1;
                    progressBarController.fsize.clear();
                    progressBarController.filename.clear();
                    progressBarController.order = 0;
                    progressBarController.counter = 0;
                    progressBarController.SendOrGet = 1;


                    progressBarController.filename.add(filenameb);
                    progressBarController.fsize.add(BackupClient.getServer().getFileSize(filenameb,table.getSelectionModel().getSelectedItem().getVersion()));


                    Thread t2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                BackupClient.getFile(BackupClient.getServer().passAStream(pathToGet), filename);
                                Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, "Confirm", ButtonType.OK, ButtonType.CANCEL);
                                dialog.setHeaderText("File has been downloaded!");
                                dialog.setContentText("Your file has just been downloaded!");
                                dialog.setResizable(true);
                                dialog.getDialogPane().setPrefSize(250, 100);
                                dialog.showAndWait();
                            }
                            catch(Exception e){
                                e.getMessage();
                            }
                        }
                    });
                    t2.start();
                    createProgressBarWindow();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            Alert dialog = new Alert(Alert.AlertType.INFORMATION, "Confirm", ButtonType.OK, ButtonType.CANCEL);
            dialog.setHeaderText("No file is selected!");
            dialog.setContentText("Please select the file first and then press GET");
            dialog.setResizable(true);
            dialog.getDialogPane().setPrefSize(250, 200);
            dialog.showAndWait();
        }
    }

    public boolean checkFile(String fileName) {
        File file = new File("D:\\Client");
        boolean isInClient = false;
        Collection<File> files = FileUtils.listFiles(file, null, false);
        for (File file2 : files) {
            if (file2.getName().substring(0, file2.getName().lastIndexOf(".")).equals(fileName)) {
                isInClient = true;
                Alert dialog = new Alert(Alert.AlertType.ERROR, "Confirm", ButtonType.OK, ButtonType.CANCEL);
                dialog.setHeaderText("Retrieving file error");
                dialog.setContentText("You have the file already!");
                dialog.setResizable(true);
                dialog.getDialogPane().setPrefSize(250, 100);
                dialog.showAndWait();
                break;
            }
        }
        return isInClient;
    }

    public void periodicAction(ActionEvent event) throws IOException {

        try {
            Parent timeScreen = FXMLLoader.load(getClass().getResource("timeScreen.fxml"));
            Scene timeScene = new Scene(timeScreen);
            Stage timeStage = new Stage();
            timeStage.setTitle("Period Chooser");
            timeStage.setScene(timeScene);
            timeStage.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }


    public void createProgressBarWindow() {
        try {
            Parent timeScreen = FXMLLoader.load(getClass().getResource("../fxmlFiles/progressBar.fxml"));
            Scene timeScene = new Scene(timeScreen);
            Stage timeStage = new Stage();
            timeStage.setTitle("Upload progress");
            timeStage.setScene(timeScene);
            timeStage.show();

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void deleteButtonAction(ActionEvent event){
        if (table.getSelectionModel().getSelectedItem() != null) {
            try{
                BackupClient.getServer().deleteFile(table.getSelectionModel().getSelectedItem().getFileName(),table.getSelectionModel().getSelectedItem()
                        .getVersion(),table.getSelectionModel().getSelectedItem().getPath());

                Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, "Confirm", ButtonType.OK, ButtonType.CANCEL);
                dialog.setHeaderText("File has been deleted!");
                dialog.setContentText("Your file has just been deleted!");
                dialog.setResizable(true);
                dialog.getDialogPane().setPrefSize(250, 100);
                dialog.showAndWait();
            }
            catch (RemoteException re){
                re.getMessage();
            }
        } else {
            Alert dialog = new Alert(Alert.AlertType.INFORMATION, "Confirm", ButtonType.OK, ButtonType.CANCEL);
            dialog.setHeaderText("No file is selected!");
            dialog.setContentText("Please select the file first and then press DELETE");
            dialog.setResizable(true);
            dialog.getDialogPane().setPrefSize(250, 200);
            dialog.showAndWait();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        upload_menuitem.setAccelerator(new KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN));
        periodic.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
        filenameColumn.setCellValueFactory(new PropertyValueFactory<>("FileName"));
        lastModifiedColumn.setCellValueFactory(new PropertyValueFactory<>("lastModified"));
        versionColumn.setCellValueFactory(new PropertyValueFactory<>("version"));
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("path"));


        Platform.runLater(() -> {
            try {
                RemoteInputStream ris = BackupClient.getServer().tableStream();
                data = BackupClient.getTable(ris);
                table.getItems().clear();
                table.getItems().addAll(data);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });

    }

    public void howToAction(ActionEvent event){
        try {
            Parent howToScreen = FXMLLoader.load(getClass().getResource("../fxmlFiles/howTo.fxml"));
            Scene howToScene = new Scene(howToScreen);
            Stage howToStage = new Stage();
            howToStage.setTitle("User Instruction");
            howToStage.setScene(howToScene);
            howToStage.show();

        } catch (Exception e) {
            e.getMessage();
        }

    }

    public void aboutAction(ActionEvent event){
        try {
            Parent creditsScreen = FXMLLoader.load(getClass().getResource("../fxmlFiles/credits.fxml"));
            Scene creditsScene = new Scene(creditsScreen);
            Stage creditsStage = new Stage();
            creditsStage.setTitle("Authors");
            creditsStage.setScene(creditsScene);
            creditsStage.show();

        } catch (Exception e) {
            e.getMessage();
        }
    }


}
