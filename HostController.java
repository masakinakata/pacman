import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;


public class HostController {
    @FXML
    private TextField entry_name;

    @FXML
    private TextField entry_ip;

    @FXML
    private ListView<String> list_map;

    @FXML
    private Button button_connect;

    @FXML
    private Button button_return;

    @FXML
    private ChoiceBox<String> choice_count;

    @FXML
    private void CreateServer () {
        System.out.println("CreateServer()");
        MyServer server = new MyServer();
        server.start();

        String s = entry_ip.getText();
        Main.ipAddress = (s.equals(""))? "localhost" : s; //IPアドレスのテキストフィールドになにも入力しなければlocalhostをいれる
        System.out.println("connect to Server IP ADDRESS: "+Main.ipAddress);
        s = entry_name.getText();
        Main.playerName = (s.equals(""))? "NO NAME" : s;
        System.out.println("PLAYER NAME: "+Main.playerName);

        Main.stageChoice = list_map.getSelectionModel().getSelectedItem().toString();
        System.out.println(list_map.getSelectionModel().getSelectedItem().toString());

        Main.changeScene(Main.sceneType.Game);
    }

    @FXML
    private void BackToTitle () {
        System.out.println("BackToTitile()");
        Main.changeScene(Main.sceneType.Title);
    }

    @FXML
    void initialize () {
        entry_ip.setText("localhost");

        for (int i = 1; i < 10; i++) choice_count.getItems().add(String.valueOf(i));
        choice_count.getSelectionModel().select(0);

        try{
          File file = new File("maplist.dat");
          BufferedReader br = new BufferedReader(new FileReader(file));
          System.out.println("File loaded.");
        
          String str;
          while((str = br.readLine()) != null){
            list_map.getItems().add(str);
          }
        
          br.close();
        }catch(FileNotFoundException e){
          System.out.println(e);
        }catch(IOException e){
          System.out.println(e);
        }
        list_map.getSelectionModel().select(0);
    }

}
