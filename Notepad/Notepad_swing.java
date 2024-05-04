import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

// 繼承Swing window類別中的JFrame
public class Notepad_swing extends JFrame {

  Container c = null;

  public Notepad_swing(String title) {
    super(title);

    // 獲取JFrame的容器  https://blog.csdn.net/happyAliceYu/article/details/52150738
    c = this.getContentPane();
    c.setLayout(new BorderLayout());

    // 多行文字框
    // final ~= const
    final JTextArea t = new JTextArea();
    t.setLineWrap(true);      // 自動換行

    // 帶有滾動條的container
    JScrollPane s = new JScrollPane(t);
    c.add(s, BorderLayout.CENTER);

    // 文件選擇對話框
    final JFileChooser fc = new JFileChooser();

    JMenuBar mb;
    JMenu fmi;        // 檔案
    JMenuItem nmi;    // 開新檔案
    JMenuItem omi;    // 開啟舊檔
    JMenuItem smi;    // 儲存檔案
    JMenuItem exmi;   // 離開

    JMenu emi;        //編輯
    JMenuItem cmi;    //複製
    JMenuItem pmi;    //貼上
    JMenuItem cutmi;  //剪下

    mb = new JMenuBar();
    // 創建了一個降低（LOWERED）的斜角邊框 -> 使其具有立體感
    mb.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    setJMenuBar(mb);  // 不能使用 c.add()

    fmi = new JMenu("檔案");

    // 開啟新檔功能
    nmi = new JMenuItem("開新檔案");
    nmi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        t.setText("");
      }
    });
    fmi.add(nmi);

    // 開啟新檔功能
    omi = new JMenuItem("開啟舊檔");
    omi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        // 開啟檔案對話框 並回傳結果(是否點選)
        int yesornot = fc.showOpenDialog(Notepad_swing.this);
        if (yesornot == JFileChooser.APPROVE_OPTION) {
          try {
            String line;
            // 獲取選擇的檔案
            File f = fc.getSelectedFile();
            // 讀取檔案內容
            BufferedReader br = new BufferedReader(new FileReader(f.getAbsolutePath()));
            t.setText(br.readLine());
            while ((line = br.readLine()) != null) {
              t.append("\n" + line);
            }
            br.close();
          } catch (IOException ioe) {
            // 列印異常訊息
            //ioe.printStackTrace();
          }
        }
      }
    });
    fmi.add(omi);

    // 儲存檔案功能
    smi = new JMenuItem("儲存檔案");
    smi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        try {
          int op;
          op = fc.showSaveDialog(Notepad_swing.this);
          if (op == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(f.getAbsolutePath()));
            String lines[] = t.getText().split("\n");
            for (int i = 0; i < lines.length; i++) {
              bw.write(lines[i]);
              bw.newLine();
            }
            bw.close();
          }
        } catch (IOException ioe) {
          //ioe.printStackTrace();
        }

      }
    });
    fmi.add(smi);

    fmi.addSeparator();

    exmi = new JMenuItem("離開");
    exmi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        System.exit(0);
      }
    });
    fmi.add(exmi);

    mb.add(fmi);

    emi = new JMenu("編輯");

    cmi = new JMenuItem("複製");
    cmi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        t.copy();
      }
    });
    emi.add(cmi);

    pmi = new JMenuItem("貼上");
    pmi.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
            t.paste();
      }
    });
    emi.add(pmi);

    cutmi = new JMenuItem("剪下");
    cutmi.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
            t.cut();
      }
    });
    emi.add(cutmi);

    mb.add(emi);

    this.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
      }
    });

    try {
        // 設置應用程式的外觀
        UIManager.setLookAndFeel("com.sun.swing.plaf.gtk.GTKLookAndFeel");
        SwingUtilities.updateComponentTreeUI(this);

    } catch (Exception e) {
        e.printStackTrace();
    }

    this.setSize(400, 250);
    this.setVisible(true);
  }

  public static void main(String s[]) {
    Notepad_swing app = new Notepad_swing("簡易記事本");
  }
}
