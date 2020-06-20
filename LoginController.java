/*new*/
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;

public class LoginController {

    private boolean isNextScene = false;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button button_start;

    @FXML
    private Button button_connect;

    @FXML
    private TextField textfield_ip;

    @FXML
    private TextField textfield_name;

    @FXML
    private ComboBox<String> stageComboBox;


    /*Startgame ConnectServer*/

    @FXML
    private void ConnectServer()
    {
        String s = textfield_ip.getText();
        Main.ipAddress = (s.equals(""))? "localhost" : s; //IPアドレスのテキストフィールドになにも入力しなければlocalhostをいれる
        System.out.println("connect to Server IP ADDRESS: "+Main.ipAddress);
    }

    @FXML
    private void Startgame()
    {
        String s = textfield_name.getText();
        Main.playerName = (s.equals(""))? "NO NAME" : s;
        System.out.println("PLAYER NAME: "+Main.playerName);
        isNextScene = true;
        textfield_ip.setDisable(true);
        textfield_name.setDisable(true);
        button_start.setDisable(true);
        button_connect.setDisable(true);

        // Manager.connectToServer(serverIp, textfield_name.getText());

        Main.changeScene(Main.sceneType.Game);
    }

    @FXML
    private void selectStage()
    {
      Main.stageChoice = stageComboBox.getValue();
    }



    @FXML
    void initialize()
    {
      stageComboBox.getItems().add("1");
      stageComboBox.getItems().add("2");
      stageComboBox.getItems().add("3");
      stageComboBox.getSelectionModel().select(0);
    }
}
