import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class GuestController {

    @FXML
    private TextField entry_name;

    @FXML
    private TextField entry_ip;

    @FXML
    private ListView<String> list_history;

    @FXML
    private ListView<String> list_map;

    @FXML
    private Button button_connect;

    @FXML
    private Button button_del_history;

    @FXML
    private Button button_return;

    @FXML
    private void ConnectServer(MouseEvent event) {
        String s = entry_ip.getText();
        Main.ipAddress = (s.equals(""))? "localhost" : s; //IPアドレスのテキストフィールドになにも入力しなければlocalhostをいれる
        System.out.println("connect to Server IP ADDRESS: "+Main.ipAddress);
        s = entry_name.getText();
        Main.playerName = (s.equals(""))? "NO NAME" : s;
        System.out.println("PLAYER NAME: "+Main.playerName);

        Main.stageChoice = list_map.getSelectionModel().getSelectedItem().toString();
        System.out.println(list_map.getSelectionModel().getSelectedItem().toString());

        try{
            FileWriter file = new FileWriter("history.dat", true);
            PrintWriter pw = new PrintWriter(new BufferedWriter(file));
            System.out.println("File loaded.");
          
            pw.println(entry_ip.getText());
          
            pw.close();
          }catch(FileNotFoundException e){
            System.out.println(e);
          }catch(IOException e){
            System.out.println(e);
        }

        Main.changeScene(Main.sceneType.Game);
    }

    @FXML
    private void DeleteHistory(MouseEvent event) {
        System.out.println("DeleteHistory()");
        list_history.getItems().clear();

        try{
            FileWriter file = new FileWriter("history.dat");
            PrintWriter pw = new PrintWriter(new BufferedWriter(file));
            System.out.println("File loaded.");
          
            pw.println("localhost");
          
            pw.close();
          }catch(FileNotFoundException e){
            System.out.println(e);
          }catch(IOException e){
            System.out.println(e);
        }
    }

    @FXML
    private void SetHistory() {
        entry_ip.setText(list_history.getSelectionModel().getSelectedItem().toString());
    }

    @FXML
    private void BackToTitle (MouseEvent event) {
        System.out.println("BackToTitle()");
        Main.changeScene(Main.sceneType.Title);
    }

    @FXML
    void initialize () {
        entry_ip.setText("localhost");

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

        try{
            File file = new File("history.dat");
            BufferedReader br = new BufferedReader(new FileReader(file));
            System.out.println("File loaded.");
          
            String str;
            while((str = br.readLine()) != null){
              list_history.getItems().add(str);
            }
          
            br.close();
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }
    }
}
