package code;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.sound.sampled.*;

public class GameStart extends JPanel {
    // data member
    private JButton startButton;
    private ResizableImagePanel gifPanel;
    private Clip clip;

    // initialization
    public GameStart() {
        setLayout(new BorderLayout());

        ImageIcon gifIcon = new ImageIcon("attachment/image/cat_dance.gif");

        // 存放GIF
        gifPanel = new ResizableImagePanel(gifIcon);
        add(gifPanel, BorderLayout.CENTER);

        // 開始按鈕
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBackground(new Color(255,255,169));
        startButton.setForeground(new Color (29,46,187));
        // startButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        add(startButton, BorderLayout.EAST);

        playBackgroundMusic("attachment/music/girlfriend.wav");
    }

    public JButton getStartButton() {
        return startButton;
    }

    // 用於縮放GIF圖
    private class ResizableImagePanel extends JPanel {
        private ImageIcon imageIcon;

        public ResizableImagePanel(ImageIcon imageIcon) {
            this.imageIcon = imageIcon;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (imageIcon != null) {
                Image image = imageIcon.getImage();

                int panelWidth = getWidth();
                int panelHeight = getHeight();
                int imageWidth = imageIcon.getIconWidth();
                int imageHeight = imageIcon.getIconHeight();

                // 缩放比例(依據GIF的高)
                double scaleX = (double) panelWidth / imageWidth;
                double scaleY = (double) panelHeight / imageHeight;
                double scale = imageHeight > panelHeight ? scaleX : scaleY;

                int scaledWidth = (int) (imageWidth * scale);
                int scaledHeight = (int) (imageHeight * scale);
                g.drawImage(image, 0, 0, scaledWidth, scaledHeight, this);
            }
        }
    }


    // use ChatGPT
    private void playBackgroundMusic(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // 循环播放
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopBackgroundMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
