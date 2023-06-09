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
    final int[] timeLeft = {3000};
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGHT;
    private int ONE_CHESS_SIZE;

    private String SaveName;
    private String[] Animal = new String[]{"Elephant", "Cat", "Leopard", "Mouse", "Lion", "Tiger", "Wolf", "Dog"};
    private JPanel jPanel = new JPanel();
    private boolean hide = false;

    private ChessboardComponent chessboardComponent;
    private GameController gameController;
    private int count = 2;
    private Clip clip;


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
        //addRoundLable();
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

    private void addJPanel() {
        jPanel.setLayout(new GridLayout(9, 1, 0, 10));
        jPanel.setLocation(HEIGHT, HEIGHT / 10 + 100);
        jPanel.setOpaque(false);
        jPanel.setSize(200, 500);
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
        statusLabel.setLayout(new GridLayout(1, 1));
        JButton button = new JButton("隐藏");
        button.setFont(new Font("宋体", Font.PLAIN, 20));
        button.addActionListener(e -> {
            if (hide) {
                jPanel.setVisible(true);
                button.setText("隐藏");
                hide = false;
            } else {
                jPanel.setVisible(false);
                button.setText("展开");
                hide = true;
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
        ImageIcon icon = new ImageIcon("resource/悔棋.jpg");
        JButton button = new JButton("悔棋", icon);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
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
        ImageIcon icon = new ImageIcon("resource/restart.jpg");
        JButton button = new JButton("重新开始", icon);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
        timeLeft[0] = 3000;
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
        button.setForeground(Color.green);
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

        ImageIcon icon = new ImageIcon("resource/保存.jpg");
        JButton button = new JButton(icon);
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
        fileChooser.setFileFilter(new FileNameExtensionFilter("image(*.jpg,*.png,*.gif)", "jpg", "gif", "png"));
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
        ImageIcon icon = new ImageIcon("resource/音乐.jpg");
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
        ImageIcon icon = new ImageIcon("resource/音效.jpg");
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
                            gameController.setCount(gameController.getCount() + 1);
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
                    timeLeft[0] = 3000;
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
        ImageIcon icon = new ImageIcon("resource/读取.jpg");
        JButton button = new JButton(icon);
        button.addActionListener((e) -> {
            int Confirm = JOptionPane.showConfirmDialog(this, "读档后将丢失当前进度，是否读档？");
            switch (Confirm) {
                case JOptionPane.YES_OPTION -> Read(gameController);
                case JOptionPane.CLOSED_OPTION, JOptionPane.NO_OPTION, JOptionPane.CANCEL_OPTION ->
                        JOptionPane.showMessageDialog(this, "读档取消");
            }
        });

        button.setSize(200, 60);
        button.setFont(new Font("宋体", Font.PLAIN, 20));
        jPanel.add(button);
    }

    public void addButton(GameController gameController) {
        addSaveButton(gameController);
        addReadButton(gameController);
        addUndoButton(gameController);
        addRestartButton(gameController);
        addMusicButton(gameController);
        addMusicEffectButton(gameController);
        addExitButton(gameController);
        addTimeLabel(gameController);
        addRoundLabel(gameController);
        this.gameController = gameController;
    }

    public void addRoundLabel(GameController gameController) {
        JLabel roundLabel = new JLabel();
        final int[] count = {gameController.getCount() / 2};
        final int[] Left = {gameController.getCount() % 2};
        jPanel.add(roundLabel);
        Timer timer = new Timer(100, e -> {
            if (!gameController.isRestart()){
                gameController.setCount(2);
                gameController.setRestart(true);
                gameController.setCurrentPlayer(PlayerColor.BLUE);
            }
            System.out.println(gameController.getCount());
            count[0] = gameController.getCount() / 2;
            Left[0] = gameController.getCount() % 2;
            roundLabel.setText("轮次： " + count[0] + " 玩家 " + (gameController.getCount() % 2 == 1 ? "红方" : "蓝方"));
            roundLabel.setFont(new Font("宋体", Font.PLAIN, 20));
            roundLabel.setForeground(Left[0] == 1 ? Color.RED : Color.BLUE);
            roundLabel.setBackground(new Color(30, 30, 30));
        });
        timer.start();


    }


    public void addTimeLabel(GameController gameController) {
        //倒计时时间，单位秒
        JLabel timeLabel = new JLabel("Time Left: " + timeLeft[0]);
        jPanel.add(timeLabel);
        Timer timer = new Timer(100, e -> {
            timeLeft[0]--;
            timeLabel.setText("Time Left: " + timeLeft[0] / 10);
            timeLabel.setSize(200, 60);
            timeLabel.setForeground(gameController.getCurrentPlayer().getColor());
            timeLabel.setFont(new Font("宋体", Font.PLAIN, 20));
            timeLabel.setBackground(new Color(30, 30, 30));
            if (timeLeft[0] <= 0) {
                timeLeft[0] = 3000;
                timeLabel.setText("Time Left: " + timeLeft[0] / 10);
                timeLabel.setForeground(gameController.getCurrentPlayer().getColor());
                gameController.swapColor();
                gameController.setSelectedComponent(null);
                gameController.setSelectedComponent(null);
                if (gameController.getValidMove() != null) gameController.closeValidMove(gameController.getValidMove());
                gameController.setCount(gameController.getCount() + 1);

            } else if (gameController.getCount() != count) {
                timeLeft[0] = 3000;
                timeLabel.setText("Time Left: " + timeLeft[0] / 10);
                timeLabel.setForeground(gameController.getCurrentPlayer().getColor());
            }
            count = gameController.getCount();


        });
        timer.start();

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



