import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.scene.transform.Translate;
import javafx.animation.AnimationTimer;
import java.util.*;

public class Game {
    public int gameover=0;
    private Pane root;
    private Scene scene;
    private Character pacman, enemy, img;
    private Character player[];
    private int myNumber;
    public static boolean isPacman;
    private boolean isLeft, isRight, isUp, isDown;
    private PrintWriter out;
    private String myName = "player1";
    private Socket socket = null;
    public static Stage stage1 =null;
    private static boolean flag = true;
    private static boolean check = false;

	private static boolean isRotatable;

    Game(Pane p, Scene s, boolean b)
    {
        root     = p;
        scene    = s;
        isPacman = b;
        scene.setOnKeyPressed(e->keyPressed(e)); // キーが押されたときの挙動を示す
        // scene.setOnKeyReleased(e -> keyReleased(e));//キーが離されたときの挙動を示す
        stageSet();

        // サーバに接続する
        try {
            // "localhost"は，自分内部への接続．localhostを接続先のIP Address（"133.42.155.201"形式）に設定すると他のPCのサーバと通信できる
            // 10000はポート番号．IP Addressで接続するPCを決めて，ポート番号でそのPC上動作するプログラムを特定する
            socket = new Socket(Main.ipAddress, 10000);
        } catch (UnknownHostException e) {
            System.err.println("ホストの IP アドレスが判定できません: " + e);
        } catch (IOException e) {
            System.err.println("エラーが発生しました: " + e);
        }

        MesgRecvThread mrt = new MesgRecvThread(socket, myName); // 受信用のスレッドを作成する
        mrt.start(); // スレッドを動かす（Runが動く）
        AnimationTimer timer = new AnimationTimer(){

            @Override
            public void handle(long now){
                if(gameover==1) {
                    gameover=2;
                    if(myNumber != 1) Main.changeScene(Main.sceneType.Gameover);
                    else Main.changeScene(Main.sceneType.Gamewin);
                }else if(gameover==3){
                    gameover=2;
                    if(isPacman) Main.changeScene(Main.sceneType.Gamewin);
                    else Main.changeScene(Main.sceneType.Gameover);
                }
            }
        };

        timer.start();

    }

    public void setStage()
    {
        // setting pane
        root.setPrefSize(Main.sceneLength, Main.sceneLength);
        root.setStyle("-fx-background-color: darkgray;");
        // set blocks
        for (int y = 0; y < Main.blocks; y++)
        {
            for (int x = 0; x < Main.blocks; x++)
            {
                if (Main.isBlock[x][y]) {
                    Rectangle r = new Rectangle();
                    r.setWidth(Main.blockSize);
                    r.setHeight(Main.blockSize);
                    r.setStyle("-fx-background-color: BLACK; -fx-border-color: blue;");
                    r.getTransforms().add(new Translate(x * Main.blockSize, y * Main.blockSize));
                    root.getChildren().add(r);
                }
            }
        }

        player = new Character[4];
        player[0] = new Character(new Image("pacman.png"), 1, 1);
        player[1] = new Character(new Image("enemy.png"), 1, Main.blocks-2);
        player[2] = new Character(new Image("enemy.png"), Main.blocks-2, 1);
        player[3] = new Character(new Image("enemy.png"), Main.blocks-2, Main.blocks-2);

        for (int i = 0; i < 4; i++) {
            player[i].setX(Main.blockSize * player[i].x);
            player[i].setY(Main.blockSize * player[i].y);
            player[i].setFitHeight(Main.blockSize);
            player[i].setFitWidth(Main.blockSize);
            //player[i].setVisible(false);
            root.getChildren().add(player[i]);
        }

        // set icons
        // pacman = new Character(new Image("pacman.png"), 1, 1);
        // pacman.setX(Main.blockSize);
        // pacman.setY(Main.blockSize);
        // pacman.setFitHeight(Main.blockSize);
        // pacman.setFitWidth(Main.blockSize);
        // root.getChildren().add(pacman);
        // enemy  = new Character(new Image("enemy.png"), Main.blocks - 2, Main.blocks - 2);
        // enemy.setX(Main.blockSize * enemy.x);
        // enemy.setY(Main.blockSize * enemy.y);
        // enemy.setFitHeight(Main.blockSize);
        // enemy.setFitWidth(Main.blockSize);
        // root.getChildren().add(enemy);
    }

    public class MesgRecvThread extends Thread {
        Socket socket;
        String myName;

        public MesgRecvThread(Socket s, String n)
        {
            socket = s;
            myName = n;
        }

        // 通信状況を監視し，受信データによって動作する
        public void run()
        {
            try {
                InputStreamReader sisr = new InputStreamReader(socket.getInputStream());
                BufferedReader    br   = new BufferedReader(sisr);
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println(myName); // 接続の最初に名前を送る
                boolean isFirst = true; // 最初の通信かどうか
                while (true) {
                    String inputLine = br.readLine(); // データを一行分だけ読み込んでみる
                    if (inputLine != null) { // 読み込んだときにデータが読み込まれたかどうかをチェックする
                        //System.out.println(inputLine); // デバッグ（動作確認用）にコンソールに出力する
                        if(inputLine.equals("4")){
                            String msg1 = "Start";
                            // サーバに情報を送る
                            out.println(msg1);
                            out.flush();
                        }
                        
                        if (isFirst)
                        {
                            myNumber = Integer.parseInt(inputLine);
                            if (inputLine.equals("1")) { isPacman = false; }
                            else { isPacman = true; }
                            isFirst = false;
                        }
                        String[] inputTokens = inputLine.split(" ");    // 入力データを解析するために、スペースで切り分ける
                        String   cmd         = inputTokens[0]; // コマンドの取り出し．１つ目の要素を取り出す
                        if(cmd.equals("Start")){
                            check=true;
                            TimerTask task = new TimerTask(){
                                public void run(){
                                  if(flag){
                                        gameover=3;
                                        flag=false;
                                  }
                                }
                            };
                            Timer timerset = new Timer();
                            timerset.schedule(task,60000);
                        }
                        if (cmd.equals("MOVE")&&check) { // cmdの文字と"MOVE"が同じか調べる．同じ時にtrueとなる
                            // MOVEの時の処理(コマの移動の処理)
                            String    isPacmanStr = inputTokens[1]; // ボタンの名前（番号）の取得
                            // Character c, d;
                            // if (isPacmanStr.equals("p")) {
                            //     c = pacman;
                            //     d = enemy;
							// 									isRotatable = true;
                            // } else {
                            //     c = enemy;
                            //     d = pacman;
							// 									isRotatable = false;
                            // }
                            String command = inputTokens[2];
                            //gameLoop(c, d, command);
                            gameLoop(Integer.parseInt(isPacmanStr)-1, command);
                        }
                        if (cmd.equals("MAP")) {
                            Main.stageChoice = inputTokens[1];
                        }
                    } else {
                        break;
                    }
                }
                try{
                socket.close();
                }catch(IOException e){
                    System.err.println("scene is ....."+e);
                }
            } catch (IOException e) {
                System.err.println("エラーが発生しました: " + e);
            }
        }
    }

    /*
        public void sendMsg() //AnimationTimerでループする
        {
            String direction, isPacmanStr;
            // フラグを元にdirectionを定める
            if(isLeft) direction = "LEFT";
            else if(isRight) direction = "RIGHT";
            else if(isUp) direction = "UP";
            else if(isDown) direction = "DOWN";
            else direction = "NO";

            //　pacmanかenemyか
            if(isPacman)	isPacmanStr = "p";
            else isPacmanStr = "e";

            String msg = "MOVE"+" "+isPacmanStr+" "+direction;

            //サーバに情報を送る
            out.println(msg);
            out.flush();
        }*/
    public void gameLoop(int c, String s)  // 受信したデータをもとに動かす c->動くキャラ　d->動かないキャラ
    {
        img = player[c];
        int mid = (Main.blocks - 1) / 2;
        if (s.equals("LEFT")) {
            if(c == 0) img.setRotate(180);
            if (img.x == 0 && img.y == mid) { // 左のワープゾーン到達
                img.stageNum = 1 - img.stageNum;
                img.setX((Main.blocks - 1) * Main.blockSize);
                img.x = Main.blocks - 1;
                changeVisible(c);
            } else if (!Main.isBlock[img.x - 1][img.y]) { // 左のところが空白かまたはステージ無いかどうかを見る　今回はステージ内であることだけを確認
                img.setX(img.getX() - Main.blockSize); // 左に移動可能なら座標を10マイナスする
                img.x--;
                // x-=1;//これもsetXと同じことをしている. 後述
            }
            isLeft = false; // 長押ししてると高速で移動しちゃうのであえて1回おきに移動するようにしている
        }
        else if (s.equals("RIGHT")) {
            if(c == 0) img.setRotate(0);
            if (img.x == (Main.blocks - 1) && img.y == mid) {
                img.stageNum = 1 - img.stageNum;
                img.setX(0);
                img.x = 0;
                changeVisible(c);
            } else if (!Main.isBlock[img.x + 1][img.y]) {
                img.setX(img.getX() + Main.blockSize);
                img.x++;
                // img.pointx+=1;
            }
            isRight = false;
        }
        else if (s.equals("UP")) {
            if(c == 0) img.setRotate(270);
            if (!Main.isBlock[img.x][img.y - 1]) {
                img.setY(img.getY() - Main.blockSize);
                img.y--;
                // img.pointy-=1;
                isUp = false;
            } else { img.setY(img.getY()); }
        }
        else if (s.equals("DOWN")) {
            if(c == 0) img.setRotate(90);
            if (!Main.isBlock[img.x][img.y + 1]) {
                img.setY(img.getY() + Main.blockSize);
                img.y++;
                // img.pointy+=1;
                isDown = false;
            } else { img.setY(img.getY()); }
        }


				// if(c.x == d.x && c.y == d.y && c.stageNum == d.stageNum) //キャラクターが重なった時
				// {
                //     System.out.println("キャラが重なりました");
                //     gameover=1;
                // }
                for (int i = 0; i < 4; i++) {
                    if (i == c) continue;
                    else if (img.x == player[i].x &&img.y == player[i].y&&img.stageNum == player[i].stageNum) {
                        System.out.println("Collision with "+i+" "+c);
                        if (i==0) img.death();
                        else if (c==0) player[i].death();
                    }
                }

                //GameOver
                if (myNumber != 0) if (player[myNumber-1].isDead) gameover = 1;
                else {
                    boolean winFlag = true;
                    for (int i = 1; i < 4; i++) {
                        if (!player[i].isDead) winFlag = false;
                    }
                    if (winFlag) gameover = 1;
                }
                
    }

    private void changeVisible(int c)
    {
    //   if(pacman.stageNum != enemy.stageNum)
    //   {
    //     if(isPacman) enemy.setVisible(false);
    //     else  pacman.setVisible(false);
    //   }
    //   else
    //   {
    //     enemy.setVisible(true);
    //     pacman.setVisible(true);
    //   }
        if (c == myNumber-1) {
            for (int i = 0; i < 4; i++) {
                if (i == myNumber-1) continue;
                else if (player[myNumber-1].stageNum != player[i].stageNum) {
                    player[i].setVisible(false);
                }
                else player[i].setVisible(true);
            }
        }
        else {
            if (player[myNumber-1].stageNum != player[c].stageNum) {
                player[c].setVisible(false);
            }
            else player[c].setVisible(true);
        }
    }

    private void keyPressed(KeyEvent e)
    {
        String direction = "NO", isPacmanStr;
        switch (e.getCode()) {
            case LEFT:
                direction = "LEFT"; // 左が押されたの確認したらフラグを立てる
                break;
            case RIGHT:
                direction = "RIGHT";
                break;
            case UP:
                direction = "UP";
                break;
            case DOWN:
                direction = "DOWN";
                break;
            default:
                break;
        }
        if (isPacman) { isPacmanStr = "p"; }
        else { isPacmanStr = "e"; }
        String msg = "MOVE" + " " + String.valueOf(myNumber) + " " + direction;
        // サーバに情報を送る
        out.println(msg);
        out.flush();
    }

    private void stageSet()
    {
        // System.out.println("here");
        try
        {
            String fileAddress = Main.stageChoice+".txt";
            File file = new File(fileAddress);
            // System.out.println("here----");
            FileReader     fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String         data;
            int            y = 0;
            data = br.readLine();
            Main.blocks = Integer.parseInt(data);
            data = br.readLine();
            Main.blockSize = Integer.parseInt(data);
            System.out.println("(blocks, blockSize) = ("+Main.blocks+", "+Main.blockSize+")");
            Main.isBlock = new boolean[Main.blocks][Main.blocks];
            while ((data = br.readLine()) != null) {
                // System.out.println(data);
                for (int i = 0; i < data.length(); i++) {
                    char c = data.charAt(i);
                    if (c - '0' == 0) { Main.isBlock[i][y] = false; }
                    else { Main.isBlock[i][y] = true; }
                    // System.out.print(isBlock[i][y]+"|");
                }
               // System.out.println();
                y++;
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}


class Character extends ImageView {
    public int x, y, stageNum = 1;
    public boolean isDead;
    // public int pointx, pointy;
    Character(Image i, int x, int y)
    {
        super();
        Image image = i;
        this.setImage(image);
        this.x = x;
        this.y = y;
        // this.pointx = (int) (x/10)+1;
        // this.pointy = (int) (y/10)+1;
        this.setRotate(0);
        // System.out.println(pointx+" "+pointy);// デバック用　消しても何も変わらん
        this.isDead = false;
    }

    public void death () {
        this.isDead = true;
        this.setVisible(false);
    }
}
