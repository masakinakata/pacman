import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class TitleController {

    @FXML
    private Button button_host;

    @FXML
    private Button button_gest;

    @FXML
    private void StartHostGame (MouseEvent event) {
        System.out.println("StartHostGame()");
        Main.changeScene(Main.sceneType.Host);
    }

    @FXML
    private void StartGuestGame (MouseEvent event) {
        System.out.println("StartGuestGame()");
        Main.changeScene(Main.sceneType.Guest);
    }

    @FXML
    void initialize () {}

}
