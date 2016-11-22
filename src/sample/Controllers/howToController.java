package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Bartłomiej on 22.11.2016.
 */
public class howToController implements Initializable {

    @FXML
    private TextArea howToText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        howToText.setVisible(true);
        howToText.setText("Opis działania programu:\n" +
                "1.\tWłączanie programu BackupServer\n" +
                "Po włączeniu programu użytkownik ma możliwość ręcznego podania adresu ip serwera oraz portu, na którym ma działać. Jest także preferowana opcja wyboru domyślnych ustawień.\n" +
                "2.\tWłączenie programu BackupClient\n" +
                "W pierwszym oknie dialogowym należy skonfigurować połączenie z serwerem podając ip serwera oraz port. W tym punkcie także preferowane jest wybranie domyślnych ustawień.\n" +
                "3.\tObsługa programu BackupClient\n" +
                "Obsługa programu jest w gruncie rzeczy trywialna! BackupClient pozwala na sprawne zabezpieczanie plików poprzez tworzenie kopii zapasowej na serwerze dedykowanym.\n" +
                "a.\tUpload plików:\n" +
                "W celu backup’u plików należy wybrać z menu rozwijalnego File opcje upload file lub wcisnąć kombinacje klawiszy ctrl+u.\n" +
                "Po pojawieniu się okna wyboru plików należy wybrać dowolne pliki i kliknąć przycisk otwórz.\n" +
                "Po wykonaniu w.w. czynności nastąpi przesyłanie wybranego pliku/plików na server, co użytkownik będzie mógł obserwować dzięki paskowi progresu. Po udanym przesłaniu wyświetli się informacja o sukcesie.\n" +
                "b.\tOkresowy upload plików\n" +
                "Opcja umożliwiająca backup wybranych plików co określony wybrany z rozwijalnej listy okres, po upłynięciu którego nastąpi automatyczny backup wybranych plików. Aby wybrać tę opcję należy wybrać z menu rozwijalnego file opcje periodic backup.\n Po wybraniu plików należy kliknąć otwórz. Nastąpi przesłanie plików chwilę po kliknięciu. Kolejne wysłania będą następowały co stały, wybrany wcześniej okres czasu.\n" +
                "c.\tWyświetlanie plików znajdujących się na serwerze\n" +
                "Klikając przycisk show wyświetlane zostają pliki znajdujące się na serwerze. Są one wyświetlane w tabeli wraz z parametrami takimi jak: nazwa pliku; data modyfikacji; wersja pliku.\n" +
                "d.\tPobieranie pliku z serwera\n" +
                "Aby pobrać plik z serwera najpierw należy wyświetlić jego zawartość (patrz pkt c.). Po wyświetleniu wszystkich plików należy wybrać interesujący nas plik z tabeli poprzez zaznaczenie go kliknięciem po czym kliknąć przycisk Get.\n" +
                "e.\tOpcja usuwania plików z serwera\n" +
                "Po wybraniu pliku poprzez kliknięcie jego odpowiednika w tabeli należy kliknąć przycisk delete. Spowoduje to trwałe usunięcie kopii zapasowej tego pliku z serwera.\n");
    }
}
