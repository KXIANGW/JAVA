import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.datatransfer.*;



public class Notepad_awt extends Frame{

    TextArea t;

    public Notepad_awt(String title){
        
        super(title);
        // swing需先獲取JFrame的Container
        setLayout(new BorderLayout());

        t = new TextArea();
        // 可編輯; false -> 僅檢視
        t.setEditable(true);
        t.setPreferredSize(new Dimension(500, 250));
        add(t, BorderLayout.CENTER);
        // awt TextArea or Frame內本生就有滾動條

        // 滾動顯示文字
        //  ScrollPane s = new ScrollPane();
        //  s.add(t);
        //  add(s, BorderLayout.CENTER);

        final FileDialog fc = new FileDialog(this, "選擇檔案", FileDialog.LOAD);
        MenuBar mb = new MenuBar();
        setMenuBar(mb);

        Menu fi = new Menu("檔案");
        MenuItem nfi = new MenuItem("開啟新檔");
        nfi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae){
                t.setText("");
            }
        });
        fi.add(nfi);


        MenuItem ofi = new MenuItem("開啟舊檔");
        ofi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae){
                fc.setMode(FileDialog.LOAD);
                fc.setVisible(true);

                String fileName = fc.getFile();
                String directory = fc.getDirectory();
                //System.out.println(filename+" "+directory);
                if(fileName != null && directory != null){
                    try{
                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(directory + fileName), "UTF-8"));
                        String line;
                        t.setText(br.readLine());
                        while((line = br.readLine()) != null){
                            t.append("\n" + line);
                        }
                        br.close();
                    }
                    catch(IOException ios){
                        //ioe.printStackTrace();
                    }
                }
            }
        });
        fi.add(ofi);


        MenuItem smi = new MenuItem("儲存檔案");
        smi.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                fc.setMode(FileDialog.SAVE);
                fc.setVisible(true);

                String fileName = fc.getFile();
                String directory = fc.getDirectory();
                if(fileName != null && directory != null) {
                    try{
                       BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(directory + fileName), "UTF-8"));
                       String lines[] = t.getText().split("\n");
                       for(int i = 0 ; i < lines.length ; i++){
                          bw.write(lines[i]);
                          bw.newLine();
                       }
                       bw.close();
                    }
                    catch(IOException ioe){
                       //ioe.printStackTrace();
                    }
                 }
            }
        });
        fi.add(smi);


        fi.addSeparator();
        MenuItem file_exit = new MenuItem("離開");
        file_exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae){
                System.exit(0);
            }
        });
        fi.add(file_exit);
        mb.add(fi);


        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);;
            }
        });

        
        Menu edit = new Menu("編輯");
        MenuItem copy = new MenuItem("複製");
    
        
        // Ctrl+C shortcut
        copy.setShortcut(new MenuShortcut(KeyEvent.VK_C, false));
        copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // 獲取系統剪貼板的方法
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection selection = new StringSelection(t.getSelectedText());
                // 將選定的文字放到系統剪貼板中
                clipboard.setContents(selection, selection);
            }
        });
        edit.add(copy);


        MenuItem cut = new MenuItem("剪下");
        cut.setShortcut(new MenuShortcut(KeyEvent.VK_X, false));
        cut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection selection = new StringSelection(t.getSelectedText());
                clipboard.setContents(selection, selection);
                // 將選擇的文字從文字區中移除(取代)
                t.replaceRange("", t.getSelectionStart(), t.getSelectionEnd());
            }
        });
        edit.add(cut);


        MenuItem paste = new MenuItem("貼上");
        paste.setShortcut(new MenuShortcut(KeyEvent.VK_V, false));
        paste.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                // 取得剪貼簿內容
                Transferable contents = clipboard.getContents(null);
                if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    try {
                        // 將剪貼簿內容轉換為文字
                        String pastedText = (String) contents.getTransferData(DataFlavor.stringFlavor);
                        t.replaceRange(pastedText, t.getSelectionStart(), t.getSelectionEnd());
                    } 
                    catch (Exception ex) {
                        //ex.printStackTrace();
                    }
                }
            }
        });
        edit.add(paste);

        mb.add(edit);

        setSize(1000, 1000);
        // 自動調整視窗大小
        pack(); 
        // 將視窗置中
        setLocationRelativeTo(null); 
        setVisible(true);

    }



    public static void main(String[] args){
        Notepad_awt t = new Notepad_awt("My Notepad_awt");
    }
}

