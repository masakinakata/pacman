/*new*/
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class GamewinScene {
    private Scene scene = null;
    private Parent root = null;

    public GamewinScene () {
        try {
            System.out.println("Starting GamewinScene");
            root = FXMLLoader.load(getClass().getResource("GamewinScene.fxml"));
            scene = new Scene (root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Scene getScene () {
        return scene;
    }
}
