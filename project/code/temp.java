package code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.LinkedList;
import java.util.Queue;


public class CatHitMouse extends JFrame {
    // data member
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GameStart startPanel;
    private GamePanel gamePanel;

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

        JButton startButton = startPanel.getStartButton();
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTitle("Cat Hit Mouse Game");
                setFullscreen(true);
                cardLayout.show(mainPanel, "Game");
                gamePanel.startGame();
                startPanel.stopBackgroundMusic();
            }
        });
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
}


// 老鼠類別存放內容
class Mouse {
    public Image image;
    public int X;
    public int Y;
    public boolean click;

    public Mouse(Image image, int X, int Y) {
        this.image = image;
        this.X = X;
        this.Y = Y;
        this.click = false;
    }
}

class GamePanel extends JPanel {
    // data member
    private static final int MOUSE_WIDTH = 500;
    private static final int MOUSE_SPEED = 15;
    private static final int DELAY = 300; // 0.1秒的延遲
    private static final int DELAY2 = 100;

    private Toolkit toolkit;
    private Image mouse_img;
    private Timer timer1;
    private Timer timer2;
    private int[] y_position;         // 隨機選擇出現位置
    private Queue<Mouse> mice;
    private Random random;
    private Image backgroundImage;
    private JButton exitButton;
    private int ori_width, ori_height;
    private int width, height;

    public GamePanel() {
        setLayout(null);
        random = new Random();
        

        toolkit = Toolkit.getDefaultToolkit();
        // 載入背景圖
        backgroundImage = toolkit.getImage("attachment/image/background.jpg");
        ImageIcon tmpIcon = new ImageIcon(backgroundImage);
        ori_width = tmpIcon.getIconWidth();
        ori_height = tmpIcon.getIconHeight();
        

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

        // exit button
        exitButton = new JButton("Exit Game");
        exitButton.setBounds(getWidth() - 120, getHeight() - 60, 100, 40);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(exitButton);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                exitButton.setLocation(getWidth() - 120, getHeight() - 60);
            }
        });
    }

    public void startGame() {
        // 1920*1080
        width = getWidth();
        height = getHeight();
        //y_position = new int[]{(int)(0+scale), (int)(225*scale), (int)(450*scale)};
        // n1:n2=o1:o2    o1*n2 = n1*o2  
        // 初始化老鼠
        mice = new LinkedList<Mouse>();
        mouse_img = toolkit.getImage("attachment/image/mouse.gif").getScaledInstance(height/3+(150*height)/1080, height/3+(150*height)/1080, Image.SCALE_DEFAULT);
        y_position = new int[]{(50*height)/1080, (350*height)/1080, (650*height)/1080};

        timer1.start();
        timer2.start();
    }


    private void generateMouse(){
        int is_do = random.nextInt(2);

        if(is_do == 0) return;

        Mouse mouse = new Mouse(mouse_img, getWidth(), y_position[random.nextInt(y_position.length)]);
        mice.offer(mouse);
    }

    private void moveMice() {
        int poll_count = 0;
        for (Mouse mouse : mice) {
            mouse.X -= MOUSE_SPEED;
            if (mouse.X < -MOUSE_WIDTH) {
                ++poll_count;
            }
        }
        while(poll_count != 0){
            mice.poll();
            --poll_count;
        }
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        for (Mouse mouse : mice) {
            if (mouse.image != null) {
                g.drawImage(mouse.image, mouse.X, mouse.Y, this);
            }
        }
    }
}
