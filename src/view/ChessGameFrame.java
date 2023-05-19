package view;

import controller.GameController;
import model.*;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
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

    private String SaveName;



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
    }


    public String getName(){
        return SaveName;
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
        JLabel statusLabel = new JLabel("功能按键");
        statusLabel.setLocation(HEIGHT, HEIGHT /10);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("宋体", Font.BOLD, 20));
        add(statusLabel);
    }

    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */

    public void musicPlayer(){
        File file=new File("resource/刘德华-吴京-细水长流.mp3");

    }
    public void addUndoButton(GameController gameController) {
        JButton button = new JButton("悔棋");
        button.addActionListener((e) -> {
            int value=JOptionPane.showConfirmDialog(this,"是否悔棋");
            switch (value){
                case JOptionPane.YES_OPTION -> gameController.UndoMove();
                case JOptionPane.CLOSED_OPTION, JOptionPane.NO_OPTION,JOptionPane.CANCEL_OPTION ->JOptionPane.showMessageDialog(this,"取消悔棋");

            }
        });
        button.setLocation(HEIGHT, HEIGHT / 10 + 120);
        button.setSize(200, 60);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        add(button);
    }

    public void addRestartButton(GameController gameController) {
        JButton button = new JButton("重新开始");
        button.addActionListener((e) -> {
            int value=JOptionPane.showConfirmDialog(this,"是否重新开始");
            switch (value){
                case JOptionPane.YES_OPTION -> gameController.restartGame();
                case JOptionPane.CLOSED_OPTION, JOptionPane.NO_OPTION,JOptionPane.CANCEL_OPTION ->JOptionPane.showMessageDialog(this,"游戏继续");

            }
        });
        button.setLocation(HEIGHT, 2*HEIGHT/11 +120);
        button.setSize(200, 60);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        add(button);
    }

    private void Save(GameController gameController,String name){
        try {

            File newFile = new File("Save/"+name+".txt");
            if (newFile.createNewFile()){
                FileWriter fileWriter = new FileWriter(newFile);
                BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
                if (newFile.exists()) {
                    int ValidChess=gameController.getValidBlueChess()+gameController.getValidRedChess();
                    bufferedWriter.write(ValidChess+"\n");
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 7; j++) {
                            Cell cell = gameController.getModel().getGrid()[i][j];
                            if (cell.getPiece() !=null) {
                                bufferedWriter.write(i+"," + j +"," + (cell.getPiece().getOwner().equals(PlayerColor.BLUE) ?  "Blue":"Red" ) + "," + cell.getPiece().getName() + "," + cell.getPiece().getRank() + "\n");
                            }
                        }
                    }
                    bufferedWriter.write(gameController.getCurrentPlayer().equals(PlayerColor.BLUE)?"Blue":"Red");
                    if (gameController.getBeforeMove()!=null) bufferedWriter.write("\n" + gameController.getBeforeMove().getRow()+","+gameController.getBeforeMove().getCol()+","+gameController.getAfterMove().getRow()+","+gameController.getAfterMove().getCol()+","+gameController.getAteAnimal().getName()+","+(gameController.getChessAfterMove()==null?null:gameController.getChessAfterMove().getOwner()));
                    bufferedWriter.close();
                    JOptionPane.showMessageDialog(null,"存档成功");
                }
            }
            else JOptionPane.showMessageDialog(this,"文件名已存在");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

   public void addSaveButton(GameController gameController) {
        JButton button = new JButton("存档");
        button.addActionListener((e) -> {
            JOptionPane.showMessageDialog(this, "游戏已暂停");
            this.SaveName =JOptionPane.showInputDialog("请输入文件名");
            Save(gameController,this.SaveName);
        });
        button.setLocation(HEIGHT, HEIGHT / 50 + 120);
        button.setSize(200, 60);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        add(button);
    }
    public void Read(GameController gameController){
        File ReadFile=chooseFile();
        if (ReadFile!=null){try {
                FileReader fileReader=new FileReader(ReadFile);
                BufferedReader bufferedReader=new BufferedReader(fileReader);
                int ValidNumber= Integer.parseInt(bufferedReader.readLine());
                for (int i=0;i<9;i++){
                    for (int j=0;j<7;j++){
                        ChessboardPoint chessboardPoint=new ChessboardPoint(i,j);
                        if (gameController.getModel().getChessPieceAt(chessboardPoint)!=null) gameController.getModel().removeChessPiece(chessboardPoint);
                    }
                }
            for(int i=0;i<ValidNumber;i++){
                    String[]Read=bufferedReader.readLine().split(",",5);
                    for (String s:Read)System.out.print(s+" ");
                    ChessboardPoint chessboardPoint=new ChessboardPoint(Read[0].equals("0")?0:Integer.parseInt(Read[0]),Read[1].equals("0")?0:Integer.parseInt(Read[1]));
                    gameController.getModel().setChessPiece(chessboardPoint,new ChessPiece(Read[2].equals("Red")?PlayerColor.RED:PlayerColor.BLUE,Read[3],Integer.parseInt(Read[4])));
                }
                gameController.getView().initiateChessComponent(gameController.getModel());
                gameController.initialize();
                gameController.getView().repaint();
                gameController.setCurrentPlayer(bufferedReader.readLine().equals("Red")?PlayerColor.RED:PlayerColor.BLUE);
                bufferedReader.close();
                JOptionPane.showMessageDialog(null,"读档成功！");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        }
        else JOptionPane.showMessageDialog(null,"读档取消");
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
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt","txt"));
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
        JButton button = new JButton("读档" );
        button.addActionListener((e) -> {
            int Confirm= JOptionPane.showConfirmDialog(this, "读档后将丢失当前进度，是否读档？");
            switch (Confirm){
                case JOptionPane.YES_OPTION ->  Read(gameController);
                case JOptionPane.CLOSED_OPTION, JOptionPane.NO_OPTION,JOptionPane.CANCEL_OPTION-> JOptionPane.showMessageDialog(this,"读档取消");
            }
        });
        button.setLocation(HEIGHT, 15*HEIGHT /56+120);
        button.setSize(200, 60);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        add(button);
    }
    private Clip clip;

    public void actionPerformed(GameController ignoredGameController) {
        try {
            if (clip == null || !clip.isOpen()) {
                InputStream is = new BufferedInputStream(new FileInputStream("resource/刘德华-吴京-细水长流.wav"));
                AudioInputStream ais = AudioSystem.getAudioInputStream(is);
                AudioFormat baseFormat = ais.getFormat();
                AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
                AudioInputStream converted = AudioSystem.getAudioInputStream(targetFormat, ais);
                clip = AudioSystem.getClip();
                clip.open(converted);
                clip.addLineListener(event -> {
                    if(event.getType()==LineEvent.Type.STOP){
                        clip.close();
                        clip=null;
                }});
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
        JButton MusicButton = new JButton("Music");
        MusicButton.addActionListener((e) -> actionPerformed(gameController));
        MusicButton.setLocation(HEIGHT, HEIGHT / 3+160);
        MusicButton.setSize(200, 60);
        MusicButton.setFont(new Font("宋体", Font.BOLD, 20));
        add(MusicButton);
    }









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



