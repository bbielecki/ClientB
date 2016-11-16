package sample;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javax.swing.*;
import javafx.event.*;
import java.io.IOException;

public class LoginScreenController{

    @FXML
    private TextField ip_textfield;

    @FXML
    private TextField port_textfield;

    @FXML
    private Label error_connection;

    @FXML
    private CheckBox default_cbox;



    @FXML
    private void connectButtonAction(ActionEvent event) throws IOException{
        System.out.println("Witam!");
        Parent clientScreen = FXMLLoader.load(getClass().getResource("clientScreen.fxml"));
        Scene clientScene = new Scene(clientScreen);
        Stage clientStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        //TODO poprawna obsługa nawiązywania połączenia z serwerem
        if (port_textfield.getText().equals("1099")){
            try{
                BackupClient client = new BackupClient(ip_textfield.getText());
                clientStage.setTitle("Client");
                clientStage.setScene(clientScene);
                clientStage.show();
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

        else{
            ip_textfield.clear();
            port_textfield.clear();
            error_connection.setText("Invalid IP or port! Try again.");
        }


    }

    @FXML
    private void exitButtonAction(ActionEvent event){
        System.out.println("Zamykam program...");
        System.exit(0);
    }

    @FXML
    private void handleCheckBox(ActionEvent event){
        if(default_cbox.isSelected()){
            ip_textfield.setText("127.0.0.1");
            port_textfield.setText("1099");
            ip_textfield.setEditable(false);
            port_textfield.setEditable(false);
        }
        else{
            ip_textfield.clear();
            port_textfield.clear();
            ip_textfield.setEditable(true);
            port_textfield.setEditable(true);
        }
    }

}