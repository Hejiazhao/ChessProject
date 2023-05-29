package view;

import controller.GameController;
import model.ChessboardPoint;
import model.PlayerColor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;


public class MainFrame extends JFrame {

    //private JPanel jPanel;
    private void setBackground() {
        ImageIcon icon = new ImageIcon("resource/背景最终版.gif"); // 创建一个图标对象，使用缩放后的图片
        JLabel background = new JLabel(icon); // 创建一个标签对象，使用图标对象
        background.setLayout(null); // 给标签对象设置一个布局管理器
        background.setSize(WIDTH, HEIGHT);
// 将JFrame的内容面板设置为标签对象，并继续正常工作，向JFrame添加组件
        setContentPane(background);
    }
    public boolean isNotNumeric(String string) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return !pattern.matcher(string).matches();
    }

    public void Read(GameController gameController) {
        File ReadFile = chooseFile();
        if (ReadFile != null) {
            try {
                FileReader fileReader = new FileReader(ReadFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                int Round = Integer.parseInt(bufferedReader.readLine());
                boolean judge = true;
                gameController.restartGame();
                for (int i = 0; i < Round - 1; i++) {

                    String[] Read = bufferedReader.readLine().split(",", 4);

                    if (Read.length != 4) {
                        if (Read.length == 1 && (Read[0].equals("Blue") || Read[0].equals("Red")))
                            JOptionPane.showMessageDialog(this, "输入轮数错误");
                        else JOptionPane.showMessageDialog(this, "坐标不够");
                        break;
                    } else if (isNotNumeric(Read[0]) || isNotNumeric(Read[1])) {
                        JOptionPane.showMessageDialog(this, "坐标输入错误");
                        judge = false;
                        break;
                    } else if (Integer.parseInt(Read[0]) > 8 || Integer.parseInt(Read[0]) < 0 || Integer.parseInt(Read[1]) > 6 || Integer.parseInt(Read[1]) < 0) {
                        JOptionPane.showMessageDialog(this, "坐标输入错误");
                        judge = false;
                        break;
                    } else if (isNotNumeric(Read[2]) || isNotNumeric(Read[3])) {
                        JOptionPane.showMessageDialog(this, "坐标输入错误");
                        judge = false;
                        break;
                    } else if (Integer.parseInt(Read[2]) > 8 || Integer.parseInt(Read[2]) < 0 || Integer.parseInt(Read[3]) > 6 || Integer.parseInt(Read[3]) < 0) {
                        JOptionPane.showMessageDialog(this, "坐标输入错误");
                        judge = false;
                        break;
                    }
                    ChessboardPoint src = new ChessboardPoint(Integer.parseInt(Read[0]), Integer.parseInt(Read[1]));
                    ChessboardPoint dest = new ChessboardPoint(Integer.parseInt(Read[2]), Integer.parseInt(Read[3]));
                    if (gameController.getModel().getChessPieceAt(src).getOwner().equals(gameController.getCurrentPlayer())) {
                        if (gameController.getModel().isValidMove(src, dest) && gameController.getModel().getChessPieceAt(dest) == null) {


                            gameController.beforeMove(src, dest);
                            gameController.setSelectedPoint(src);
                            gameController.getModel().setChessPiece(dest, gameController.getModel().getChessPieceAt(src));
                            gameController.getModel().removeChessPiece(src);
                            gameController.getView().initiateChessComponent(gameController.getModel());
                            gameController.setSelectedPoint(null);
                            gameController.swapColor();

                        } else if (gameController.getModel().getChessPieceAt(dest) != null && gameController.getModel().isValidMove(src, dest) && gameController.getModel().isValidMove(src, dest)) {
                            gameController.setSelectedPoint(src);
                            AnimalChessComponent component1 = gameController.getView().removeChessComponentAtGrid(dest);
                            gameController.getView().setChessComponentAtGrid(dest, component1);
                            gameController.onPlayerClickChessPiece(dest, component1);
                            gameController.getView().initiateChessComponent(gameController.getModel());
                            gameController.setSelectedPoint(null);
                        } else {
                            JOptionPane.showMessageDialog(this, "输入了非法移动");
                            break;
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "本回合行动棋子设置错误");
                        break;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    gameController.getView().paintImmediately(0, 0, gameController.getView().getWidth(), gameController.getView().getHeight());
                }

                if (judge) {
                    String color = bufferedReader.readLine();
                    if (color.equals("Red")) gameController.setCurrentPlayer(PlayerColor.RED);
                    else if (color.equals("Blue")) gameController.setCurrentPlayer(PlayerColor.BLUE);
                    else {
                        JOptionPane.showMessageDialog(this, "行动方未指定");
                        judge = false;
                    }
                    bufferedReader.close();
                }
                if (judge) JOptionPane.showMessageDialog(null, "读档成功！");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else JOptionPane.showMessageDialog(null, "读档取消");
    }

    public File chooseFile() {
        // 创建一个JFileChooser对象
        JFileChooser fileChooser = new JFileChooser();
        // 设置文件选择器的标题
        fileChooser.setDialogTitle("请选择一个文件");
        // 设置文件选择器的当前目录，可以根据需要更改
        fileChooser.setCurrentDirectory(new File("Save/"));
        // 设置文件选择器的选择模式，只能选择文件
        fileChooser.setAcceptAllFileFilterUsed(false);
        //设置不能全选
        fileChooser.setMultiSelectionEnabled(false);
        //禁止多选
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // 弹出文件选择器对话框，并获取用户的操作结果
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
        //设置可选文件
        int result = fileChooser.showOpenDialog(null);
        // 如果用户点击了确定按钮
        if (result == JFileChooser.APPROVE_OPTION) {
            // 获取用户选择的文件，并返回
            return fileChooser.getSelectedFile();
        }
        // 否则，返回null
        else {
            return null;
        }
    }


    public void addReadButton(GameController gameController) {
        JButton button = new JButton("读档");
        button.addActionListener((e) -> {
            int Confirm = JOptionPane.showConfirmDialog(this, "读档后将丢失当前进度，是否读档？");
            switch (Confirm) {
                case JOptionPane.YES_OPTION -> {this.dispose();Read(gameController);}
                case JOptionPane.CLOSED_OPTION, JOptionPane.NO_OPTION, JOptionPane.CANCEL_OPTION ->
                        JOptionPane.showMessageDialog(this, "读档取消");
            }
        });

        button.setSize(200, 60);
        button.setFont(new Font("宋体", Font.PLAIN, 20));
        add(button);
        button.setLocation(450,400);
    }




    public MainFrame(int width, int height) {
        this.setTitle("斗兽棋Demo"); //设置标题
        this.setSize(width,height);

        this.setLocationRelativeTo(null); // Center the window.
        this.setLayout(null);
        this.setResizable(false);
        this.setVisible(true);
        setBackground();
    }



    public void addStartButton(GameController ignoredGameController) {
        JButton startButton = new JButton("开始游戏");
        startButton.addActionListener(e -> {
            //if(e.getSource()==startButton){
             //this.setContentPane(chessGameFrame);
             //this.setFocusable(true);
             //this.setVisible(true);
            //}
            dispose();//关闭主界面

        });
        setVisible(true);
        startButton.setLocation(450,320);
        startButton.setSize(200, 60);
        startButton.setFont(new Font("宋体", Font.BOLD, 20));
        add(startButton);
        //pack();


    }

}