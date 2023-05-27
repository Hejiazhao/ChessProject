package view;

import controller.GameController;
import model.ChessboardPoint;
import model.PlayerColor;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.regex.Pattern;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGHT;

    private final int ONE_CHESS_SIZE;

    private String SaveName;
    private String[] Animal = new String[]{"Elephant", "Cat", "Leopard", "Mouse", "Lion", "Tiger", "Wolf", "Dog"};
    private JPanel jPanel=new JPanel() ;
    private boolean hide=false;

    private ChessboardComponent chessboardComponent;
    private GameController gameController;

    public ChessGameFrame(int width, int height) {
        setTitle("斗兽棋Demo"); //设置标题
        this.WIDTH = width;
        this.HEIGHT = height;
        this.ONE_CHESS_SIZE = (HEIGHT * 4 / 5) / 9;


        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);
        setBackground();

        addJPanel();
        addChessboard();
        addLabel();
    }


    public String getName() {
        return SaveName;
    }

    public ChessboardComponent getChessboardComponent() {
        return chessboardComponent;
    }

    public void setChessboardComponent(ChessboardComponent chessboardComponent) {
        this.chessboardComponent = chessboardComponent;
    }

    private void addJPanel(){
        jPanel.setLayout( new GridLayout(7, 1,0,10));
        jPanel.setLocation(HEIGHT,HEIGHT / 10 + 100);
        jPanel.setOpaque(false);
        jPanel.setSize(200,500);
        System.out.println(jPanel.getSize());
        this.add(jPanel);
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
        JLabel statusLabel = new JLabel();
        statusLabel.setLocation(HEIGHT + 12, HEIGHT / 20);
        statusLabel.setLayout(new GridLayout(1,1));
        JButton button=new JButton("隐藏");
        button.setFont(new Font("宋体", Font.PLAIN, 20));
        button.addActionListener(e -> {
            if (hide){
                jPanel.setVisible(true);
                button.setText("隐藏");
                hide=false;
            }
            else {
                jPanel.setVisible(false);
                button.setText("展开");
                hide=true;
            }
        });
        statusLabel.add(button);
        ImageIcon image = new ImageIcon("resource/原神图标.jpg");
        statusLabel.setSize(image.getIconWidth(), image.getIconHeight() / 2);
        button.setIcon(image);
        button.setSize(image.getIconWidth(), image.getIconHeight() / 2);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
        add(statusLabel);
    }

    private void setBackground() {
        ImageIcon icon = new ImageIcon("resource/背景最终版.gif"); // 创建一个图标对象，使用缩放后的图片
        JLabel background = new JLabel(icon); // 创建一个标签对象，使用图标对象
        background.setLayout(null); // 给标签对象设置一个布局管理器
        background.setSize(WIDTH, HEIGHT);
// 将JFrame的内容面板设置为标签对象，并继续正常工作，向JFrame添加组件
        setContentPane(background);
    }


    public void addUndoButton(GameController gameController) {
        JButton button = new JButton("悔棋");
        button.addActionListener((e) -> {
            int value = JOptionPane.showConfirmDialog(this, "是否悔棋");
            switch (value) {
                case JOptionPane.YES_OPTION -> gameController.UndoMove();
                case JOptionPane.CLOSED_OPTION, JOptionPane.NO_OPTION, JOptionPane.CANCEL_OPTION ->
                        JOptionPane.showMessageDialog(this, "取消悔棋");

            }
        });
        button.setSize(200, 60);
        button.setFont(new Font("宋体", Font.PLAIN, 20));
        jPanel.add(button);
    }

    public void addRestartButton(GameController gameController) {
        JButton button = new JButton("重新开始");
        button.addActionListener((e) -> {
            int value = JOptionPane.showConfirmDialog(this, "是否重新开始");
            switch (value) {
                case JOptionPane.YES_OPTION -> gameController.restartGame();
                case JOptionPane.CLOSED_OPTION, JOptionPane.NO_OPTION, JOptionPane.CANCEL_OPTION ->
                        JOptionPane.showMessageDialog(this, "游戏继续");

            }
        });
        System.out.println("restart");
        button.setSize(200, 60);
        button.setFont(new Font("宋体", Font.PLAIN, 20));
        jPanel.add(button);
    }

    private void Save(GameController gameController, String name) {
        try {

            File newFile = new File("Save/" + name + ".txt");
            if (newFile.createNewFile()) {
                FileWriter fileWriter = new FileWriter(newFile);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                if (newFile.exists()) {
                    bufferedWriter.write(gameController.getCount() + "\n");
                    for (int i = 0; i < gameController.getBeforeMove().size(); i++) {
                        bufferedWriter.write(gameController.getBeforeMove().get(i).getRow() + "," + gameController.getBeforeMove().get(i).getCol() + "," + gameController.getAfterMove().get(i).getRow() + "," + gameController.getAfterMove().get(i).getCol() + "\n");
                    }
                    bufferedWriter.write(gameController.getCurrentPlayer().equals(PlayerColor.BLUE) ? "Blue" : "Red");
                    bufferedWriter.close();
                    JOptionPane.showMessageDialog(null, "存档成功");
                }
            } else JOptionPane.showMessageDialog(this, "文件名已存在");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void addSaveButton(GameController gameController) {
        JButton button = new JButton("存档");
        button.addActionListener((e) -> {
            JOptionPane.showMessageDialog(this, "游戏已暂停");
            this.SaveName = JOptionPane.showInputDialog("请输入文件名");
            if (this.SaveName == null) {
                JOptionPane.showMessageDialog(this, "文件名不能为空");
            } else Save(gameController, this.SaveName);
        });

        button.setSize(200, 60);
        button.setFont(new Font("宋体", Font.PLAIN, 20));
        jPanel.add(button);
    }



    public File chooseBackgroundFile() {
        // 创建一个JFileChooser对象
        JFileChooser fileChooser = new JFileChooser();
        // 设置文件选择器的标题
        fileChooser.setDialogTitle("请选择一个文件");
        // 设置文件选择器的当前目录，可以根据需要更改
        fileChooser.setCurrentDirectory(new File("resource/"));
        // 设置文件选择器的选择模式，只能选择文件
        fileChooser.setAcceptAllFileFilterUsed(false);
        //设置不能全选
        fileChooser.setMultiSelectionEnabled(false);
        //禁止多选
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // 弹出文件选择器对话框，并获取用户的操作结果
        fileChooser.setFileFilter(new FileNameExtensionFilter("image(*.jpg,*.png,*.gif)", "jpg","gif","png"));
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
    public File chooseMusicFile() {
        // 创建一个JFileChooser对象
        JFileChooser fileChooser = new JFileChooser();
        // 设置文件选择器的标题
        fileChooser.setDialogTitle("请选择一个文件");
        // 设置文件选择器的当前目录，可以根据需要更改
        fileChooser.setCurrentDirectory(new File("resource/"));
        // 设置文件选择器的选择模式，只能选择文件
        fileChooser.setAcceptAllFileFilterUsed(false);
        //设置不能全选
        fileChooser.setMultiSelectionEnabled(false);
        //禁止多选
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // 弹出文件选择器对话框，并获取用户的操作结果
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.wav", "wav"));
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




    private Clip clip;


    public void musicPerformed(GameController ignoredGameController) {
        try {
            if (clip == null || !clip.isOpen()) {
                int choose = JOptionPane.showConfirmDialog(this, "是否手动选择");
                File newfile = new File("resource/刘德华-吴京-细水长流.wav");
                if (choose == JOptionPane.YES_OPTION) {
                    newfile = chooseMusicFile();
                }
                JOptionPane.showMessageDialog(this, "已播放");
                InputStream is = new BufferedInputStream(new FileInputStream(newfile));
                AudioInputStream ais = AudioSystem.getAudioInputStream(is);
                clip = AudioSystem.getClip();
                clip.open(ais);
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        clip = null;
                    }
                });
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
            } else {
                clip.stop();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addMusicButton(GameController gameController) {
        JButton MusicButton = new JButton("音乐");
        MusicButton.addActionListener((e) -> musicPerformed(gameController));
        ImageIcon icon=new ImageIcon("resource/音乐.jpg");
        MusicButton.setIcon(icon);
        MusicButton.setHorizontalTextPosition(JButton.CENTER);
        MusicButton.setVerticalTextPosition(JButton.CENTER);
        MusicButton.setSize(200, 60);
        MusicButton.setForeground(Color.RED);
        MusicButton.setFont(new Font("宋体", Font.PLAIN, 20));
        jPanel.add(MusicButton);
    }

    public void addMusicEffectButton(GameController gameController) {
        JButton MusicButton = new JButton("音效");
        ImageIcon icon=new ImageIcon("resource/音效.jpg");
        MusicButton.setIcon(icon);
        MusicButton.addActionListener((ActionEvent e) -> {
            if (gameController.isPlayEffect()) {
                gameController.setPlayEffect(false);
                JOptionPane.showMessageDialog(this, "音效关闭");
            } else {
                gameController.setPlayEffect(true);
                JOptionPane.showMessageDialog(this, "音效开启");
            }
        });

        MusicButton.setSize(200, 60);
        MusicButton.setHorizontalTextPosition(JButton.CENTER);
        MusicButton.setVerticalTextPosition(JButton.CENTER);
        MusicButton.setFont(new Font("宋体", Font.PLAIN, 20));
        MusicButton.setForeground(Color.GREEN);
        jPanel.add(MusicButton);
    }

    public void addExitButton(GameController gameController) {
        ImageIcon imageIcon = new ImageIcon("resource/老师图片2.gif");

        JButton ExitButton = new JButton("退出", imageIcon);
        ExitButton.addActionListener((ActionEvent e) -> {
            int choose = JOptionPane.showConfirmDialog(this, "是否退出");
            switch (choose) {
                case JOptionPane.YES_OPTION -> {
                    int judge = JOptionPane.showConfirmDialog(this, "是否存档");
                    if (judge == JOptionPane.YES_OPTION) {
                        this.SaveName = JOptionPane.showInputDialog("请输入文件名");
                        if (this.SaveName == null) {
                            JOptionPane.showMessageDialog(this, "文件名不能为空");
                        } else Save(gameController, this.SaveName);
                    }
                    this.dispose();
                }
                case JOptionPane.CLOSED_OPTION, JOptionPane.NO_OPTION, JOptionPane.CANCEL_OPTION ->
                        JOptionPane.showMessageDialog(this, "游戏继续");
                default -> throw new IllegalStateException("Unexpected value: " + choose);
            }
        });

        ExitButton.setSize(200, 60);
        ExitButton.setHorizontalTextPosition(JButton.CENTER);
        ExitButton.setVerticalTextPosition(JButton.CENTER);
        ExitButton.setFont(new Font("宋体", Font.PLAIN, 20));
        jPanel.add(ExitButton);
    }

    public void addButton(GameController gameController) {
        addSaveButton(gameController);
        addUndoButton(gameController);
        addRestartButton(gameController);
        addMusicButton(gameController);
        addMusicEffectButton(gameController);
        addExitButton(gameController);

        this.gameController=gameController;

    }


}
/*class ReadThread extends Thread{
    private GameController gameController;
    public ReadThread(GameController gameController){
          this.gameController=gameController;
    }
    public ReadThread(String name,GameController gameController){
        super(name);
        this.gameController=gameController;
    }
    @Override
    public void run(){
        try {
            System.out.println(7);
            sleep(300);
            gameController.getView().repaint();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

}
}*/


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



