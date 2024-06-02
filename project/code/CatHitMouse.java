package code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
import java.util.Random;
import java.util.LinkedList;
import java.io.File;
import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.Collections;


public class CatHitMouse extends JFrame implements ActionListener {
    // data member
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GameStart startPanel;
    private GamePanel gamePanel;
    private JButton startButton;

    public CatHitMouse() {
        setTitle("Welcome to Cat Hit Mouse Game");
        setSize(455, 610);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 將視窗放置在中央
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        startPanel = new GameStart();
        gamePanel = new GamePanel();

        mainPanel.add(startPanel, "Start");
        mainPanel.add(gamePanel, "Game");
        add(mainPanel);

        // 初始顯示的面板 "Start"
        cardLayout.show(mainPanel, "Start");

        startButton = startPanel.getStartButton();
        startButton.addActionListener(this);
    }



    private void setFullscreen(boolean fullscreen) {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (fullscreen) {
            dispose();
            setUndecorated(true);    // 無邊寬模式
            device.setFullScreenWindow(this);
        } else {
            device.setFullScreenWindow(null);
            setUndecorated(false);
            setVisible(true);
        }

        /* 無效 
        setLocation(0, 0);
        setBounds(0, 0, getWidth(), getHeight());*/
        gamePanel.setBounds(0, 0, getWidth(), getHeight()); // Adjust size of GamePanel
        gamePanel.revalidate();
        gamePanel.repaint();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CatHitMouse().setVisible(true);
            }
        });
    }

    // GameStart視窗事件處理
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == startButton){
            // System.out.println("startButton is ckicked");
            setTitle("Cat Hit Mouse Game");
            setFullscreen(true);
            cardLayout.show(mainPanel, "Game");
            gamePanel.startGame();
            startPanel.stopBackgroundMusic();
        }
    }

}




/*            貓咪打老鼠                 */

// 老鼠類別存放內容
class Mouse {
    public Image image;
    public int X, Y;
    public int width, height;
    public int speed;
    public boolean canClick;

    public Mouse(Image image, int X, int Y, int width) {
        this.image = image;
        this.X = X;
        this.Y = Y;
        this.width = width;
        this.height = width;
        speed = 30;
        canClick = true;

    }
    
    public void setSpeed(int speed){
        this.speed = speed;
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener{
    // data member
    private static final int DELAY = 400; // 0.3秒的延遲
    private static  int DELAY2 = 150;

    private Toolkit toolkit;
    private Image mouse_img, mouse_img2, cat_img;
    private Timer timer1, timer2, timer3;
    private int[] y_position;         // 隨機選擇出現位置
    private LinkedList<Mouse> mice, mice2;
    private Random random;
    private Image backgroundImage;
    private JButton exitButton, restartButton;
    private int ori_width, ori_height;
    private int width, height;
    private int mice_chcekNum = 0, mice_chcekNum2 = 0;
    private JLabel timer_label, score_label;
    private int time, score;
    private Clip clip;
    private boolean is_accelerate;

    private ArrayList<Integer> bestScores;

    public GamePanel() {
        setLayout(null);
        random = new Random();

        is_accelerate = false;
        
        toolkit = Toolkit.getDefaultToolkit();
        // 載入背景圖
        backgroundImage = toolkit.getImage("attachment/image/background.jpg");
        ImageIcon tmpIcon = new ImageIcon(backgroundImage);
        ori_width = tmpIcon.getIconWidth();
        ori_height = tmpIcon.getIconHeight();
        
        // exit button
        exitButton = new JButton("Exit Game");
        exitButton.setBounds(getWidth() - 120, getHeight() - 60, 100, 40);
        add(exitButton);
        exitButton.addActionListener(this);


        // restart button
        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Times New Roman", Font.PLAIN, 70));
        restartButton.setBounds((getWidth()-300)/2, (getHeight() - 100)/2, 300, 100);
        add(restartButton);
        restartButton.addActionListener(this);
        restartButton.setVisible(false);


        // timer
        time = 30;
        timer_label = new JLabel("剩下時間: "+time+"秒");
        timer_label.setFont(new Font("標楷體", Font.BOLD, 40));
        timer_label.setBounds((getWidth()/2-400), 0, 320, 70);
        timer_label.setOpaque(true);
        timer_label.setBackground(Color.WHITE);
        add(timer_label);


        // score
        score = 0;
        score_label = new JLabel("Score: "+score);
        score_label.setFont(new Font("標楷體", Font.BOLD, 40));
        score_label.setBounds(50, 0, 230, 70);
        score_label.setOpaque(true);
        score_label.setBackground(Color.WHITE);
        add(score_label);

        bestScores = new ArrayList<Integer>();

        // 初始化老鼠
        mice = new LinkedList<Mouse>();
        mice2 = new LinkedList<Mouse>();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                exitButton.setLocation(getWidth() - 120, getHeight() - 60);
                restartButton.setLocation((getWidth()-300)/2, (getHeight() - 100)/2);

                timer_label.setLocation((getWidth()-400)/2, 0);
                score_label.setLocation(50, 0);

                //y_position = new int[]{(int)(0+scale), (int)(225*scale), (int)(450*scale)};
                // n1:n2=o1:o2    o1*n2 = n1*o2  
                // 1920*1080
                width = getWidth();
                height = getHeight();
                mouse_img = toolkit.getImage("attachment/image/mouse.gif").getScaledInstance(height/3+(150*height)/1080, height/3+(150*height)/1080, Image.SCALE_DEFAULT);
                mouse_img2 = toolkit.getImage("attachment/image/mouse2.gif").getScaledInstance(height/3+(150*height)/1080, height/3+(150*height)/1080, Image.SCALE_DEFAULT);

                y_position = new int[]{(50*height)/1080, (350*height)/1080, (650*height)/1080};
                
                cat_img = toolkit.getImage("attachment/image/cat_hand.png").getScaledInstance(height/4, height/4, Image.SCALE_DEFAULT);
        
            }
        });

        addKeyListener(this);
        setFocusable(true);

        timer1 = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateMouse();
            }
        });

        // 設置計時器以移動老鼠
        timer2 = new Timer(DELAY2, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveMice();
            }
        });

        timer3 = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                --time;
                timer_label.setText("剩下時間: "+time+"秒");

                repaint();

                if(time<=10 &&  !is_accelerate){
                    is_accelerate = true;
                    DELAY2/=10.0;
                    timer_label.setForeground(Color.RED);
                }
                   
                
                    

                if(time != 0) return;
     
                timer1.stop();
                timer2.stop();
                timer3.stop();
                repaint();

                if(!bestScores.contains(score)){
                    bestScores.add(score);
                    Collections.sort(bestScores, Collections.reverseOrder());
                }
                

                restartButton.setVisible(true);
                
            }
        });

 
    }

    public void startGame() {
        timer1.start();
        timer2.start();
        timer3.start();
    }


    private void generateMouse(){
        int is_do = random.nextInt(2);

        if(is_do == 1){
            Mouse mouse = new Mouse(mouse_img, getWidth()-(250*height)/1080, y_position[random.nextInt(y_position.length)], height/3+(150*height)/1080);
            mice.offer(mouse);
        }

        is_do = random.nextInt(10);
        if(is_do == 1){
            Mouse mouse = new Mouse(mouse_img2, getWidth()-(250*height)/1080, y_position[random.nextInt(y_position.length)], height/3+(150*height)/1080);
            mouse.setSpeed(60);
            mice2.offer(mouse);
        }

        
    }

    private void moveMice() {

        // mice1
        int poll_count = 0;
        for (Mouse mouse : mice) {
            mouse.X -= mouse.speed;
            if(mouse.canClick && mouse.X <=  -height/8){
                mouse.canClick = false;
                ++mice_chcekNum;
            }
            if (mouse.X < -height/4) {
                ++poll_count;
                // System.out.println(mouse.X+ " < "+ -height/4);
            }
        }
        while(poll_count != 0){
            mice.pollFirst();
            --mice_chcekNum;
            --poll_count;
            score = score-3 > 0 ? score-3 : 0;
        }

        // mice2
        poll_count = 0;
        for (Mouse mouse : mice2) {
            mouse.X -= mouse.speed;
            if(mouse.canClick && mouse.X <=  -height/8){
                mouse.canClick = false;
                ++mice_chcekNum2;
            }
            if (mouse.X < -height/4) {
                ++poll_count;
            }
        }
        while(poll_count != 0){
            mice2.pollFirst();
            --mice_chcekNum2;
            --poll_count;
            score = score-7 > 0 ? score-7 : 0;
        }
         
       
        repaint();
    }

    /*                 貓咪打老鼠事件處理                    */
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == exitButton){
            System.exit(0);
        }
        else if(e.getSource() == restartButton){
            mice.clear();
            mice2.clear();
            mice_chcekNum = mice_chcekNum2 = 0;
            time = 30;
            score = 0;

            timer1.start();
            timer2.start();
            timer3.start();

            restartButton.setVisible(false);

            timer_label.setForeground(Color.BLACK);
            DELAY2*=10;
            is_accelerate = false;

            repaint();
        }
    }


    /*                 貓咪打老鼠按鍵處理                    */
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                checkMouseHit(0);
                break;
            case KeyEvent.VK_S:
                checkMouseHit(1);
                break;
            case KeyEvent.VK_X:
                checkMouseHit(2);
                break;
            default:
                break;
            } 
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private void checkMouseHit(int catIndex) {
        Mouse mouse;
        if(!mice.isEmpty()){
            mouse = mice.get(mice_chcekNum);
            if( mouse.canClick && mouse.Y == y_position[catIndex] && mouse.X <= height/16){
                playBackgroundMusic("attachment/music/meow.wav");
                mice.remove(mice_chcekNum);
                score+=5;
                
            }
        }
        
        if(!mice2.isEmpty()){
            mouse = mice2.get(mice_chcekNum2);
            if( mouse.canClick && mouse.Y == y_position[catIndex] && mouse.X <= height/16){
                playBackgroundMusic("attachment/music/meow.wav");
                mice2.remove(mice_chcekNum2);
                score+=10;
            }
        }
        
        repaint();
    }



    /*                 貓咪打老鼠繪圖                    */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        for (Mouse mouse : mice) {
            if (mouse.image != null) {
                g.drawImage(mouse.image, mouse.X, mouse.Y, this);
            }
        }
        
        for(Mouse mouse: mice2){
            if (mouse.image != null) {
                g.drawImage(mouse.image, mouse.X, mouse.Y, this);
            }
        }

        for(int i = 0; i<y_position.length ;++i){
            g.drawImage(cat_img, 0, y_position[i]+ (130*height)/1080, this);
        }

        score_label.setText("Score: "+score);

        if(bestScores.isEmpty()) return;
        g.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        g.setColor(Color.RED);
        int y = 90;
        g.drawString("TOP 5", width-180, 40);
        for (int i = 0; i < bestScores.size() && i < 5; ++i) {
            String rank;
            switch (i) {
                case 0:
                    rank = "1st: ";
                    break;
                case 1:
                    rank = "2nd: ";
                    break;
                case 2:
                    rank = "3rd: ";
                    break;
            
                default:
                    rank = (i+1) + "th: ";
                    g.setColor(Color.BLACK);
                    break;
            }
            g.drawString(rank+String.valueOf(bestScores.get(i)), width-180, y);
            y += 50; // 設定下一行的位置
        }
    }

    private void playBackgroundMusic(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(0); // 循环播放
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
