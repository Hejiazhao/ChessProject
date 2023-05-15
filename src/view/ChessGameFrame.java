package view;

import controller.GameController;
import model.Cell;
import model.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGHT;

    private final int ONE_CHESS_SIZE;
    private String Name;

    private ChessboardComponent chessboardComponent;
    public ChessGameFrame(int width, int height) {
        setTitle("2023 CS109 Project Demo"); //设置标题
        this.WIDTH = width;
        this.HEIGHT = height;
        this.ONE_CHESS_SIZE = (HEIGHT * 4 / 5) / 9;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        addChessboard();
        addLabel();
        addHelloButton();


    }
    public String getName(){
        return Name;
    }

    public ChessboardComponent getChessboardComponent() {
        return chessboardComponent;
    }

    public void setChessboardComponent(ChessboardComponent chessboardComponent) {
        this.chessboardComponent = chessboardComponent;
    }

    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        chessboardComponent = new ChessboardComponent(ONE_CHESS_SIZE);
        chessboardComponent.setLocation(HEIGHT / 5, HEIGHT / 10);
        add(chessboardComponent);
    }

    /**
     * 在游戏面板中添加标签
     */
    private void addLabel() {
        JLabel statusLabel = new JLabel("Function button");
        statusLabel.setLocation(HEIGHT, HEIGHT / 10);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(statusLabel);
    }

    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */

    private void addHelloButton() {
        JButton button = new JButton("Show Project Here");
        button.addActionListener((e) -> JOptionPane.showMessageDialog(this, "new project"));
        button.setLocation(HEIGHT, HEIGHT / 10 + 120);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

   public void addSaveButton(GameController gameController) {
        JButton button = new JButton("存档");
        button.addActionListener((e) -> {
            JOptionPane.showMessageDialog(this, "游戏已暂停");
            this.Name=JOptionPane.showInputDialog("请输入文件名");



            try {

                File newFile = new File("D:\\JAVA\\Demo\\ChessProject\\Save\\"+Name+".txt");
                if (newFile.createNewFile()){
                    FileWriter fileWriter = new FileWriter(newFile);
                    BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
                if (newFile.exists()) {
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 7; j++) {
                            Cell cell = gameController.getModel().getGrid()[i][j];
                            if (cell.getPiece() !=null) {
                                bufferedWriter.write(i + "\t" + j + "\t" + (cell.getPiece().getOwner().equals(PlayerColor.BLUE) ?  "Blue":"Red" ) + "\t" + cell.getPiece().getName() + "\t" + cell.getPiece().getRank() + "\n");
                            }
                        }
                    }
                    bufferedWriter.write(gameController.getCurrentPlayer().equals(PlayerColor.BLUE)?"Blue":"Red");
                    bufferedWriter.close();
                    JOptionPane.showMessageDialog(null,"存档成功");
                }
            }
                else JOptionPane.showMessageDialog(this,"文件名已存在");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


        });
        button.setLocation(HEIGHT, HEIGHT / 50 + 120);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }


//    private void addLoadButton() {
//        JButton button = new JButton("Load");
//        button.setLocation(HEIGHT, HEIGHT / 10 + 240);
//        button.setSize(200, 60);
//        button.setFont(new Font("Rockwell", Font.BOLD, 20));
//        add(button);
//
//        button.addActionListener(e -> {
//            System.out.println("Click load");
//            String path = JOptionPane.showInputDialog(this,"Input Path here");
//            gameController.loadGameFromFile(path);
//        });
//    }


}
