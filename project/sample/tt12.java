package sample;

/*****************************************
UI程式設計範本
*************************************************/
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
//想想還有沒有其他的東西要import
public class tt12 extends JFrame implements ActionListener,MouseMotionListener,MouseListener //名稱改一改
{
Container c;
//設定UI元件
JButton bot;
JLabel lab1,lab2;
//設定共用的變數與類別
java.util.Random rg;
int x1,y1,x2,y2,mode,ms;//mt時間,ms分數
double mt;
javax.swing.Timer t;
public tt12() //建構元，名稱改一改
{
super("打地鼠");
//初始化共用變數
x1=200;x2=400;
y1=200;y2=200;mode=0;
rg=new Random();
c=getContentPane();//取得ContentPane
//設定版面設定
c.setLayout(new FlowLayout());
//初始化UI元件
bot=new JButton("開始");
lab1=new JLabel("剩下:60秒");
lab2=new JLabel("成績:0");

//將UI元件加入ContentPane
c.add(lab1);c.add(bot);c.add(lab2);
//設定UI元件與滑鼠的事件觸發傾聽者
bot.addActionListener(this);
addMouseMotionListener(this);
addMouseListener(this);
t=new javax.swing.Timer(500,this);
setSize(640,480);//設定size，顯示出去
setVisible(true);
}
public void paint(Graphics g)
{
super.paint(g);//畫出元件

g.drawOval(x1-50,y1-50,100,100);
g.drawOval(x2-50,y2-50,100,100);
//額外的畫圖程式寫在這裡

if(mode==1)
g.fillOval(x1-50,y1-50,100,100);
else
g.drawOval(x1-50,y1-50,100,100);
if(mode==2)
g.fillOval(x2-50,y2-50,100,100);
else
g.drawOval(x2-50,y2-50,100,100);
}
public void mouseDragged(MouseEvent xxx) { };
public void mouseMoved(MouseEvent e){ };
//UI元件事件處理類別寫在這裡
public void actionPerformed(ActionEvent e)
{
if (e.getSource()==bot)
{
mt=60;
ms=0;
t.start();
}
else if (e.getSource()==t)
{
mt=mt-0.5;
mode=rg.nextInt(2)+1;
lab1.setText("剩下:"+mt+"秒");
repaint();
}
}
public void mouseEntered(MouseEvent e){ };
public void mouseExited(MouseEvent e){ };
public void mousePressed(MouseEvent e)
{
int mx,my;
mx=e.getX();my=e.getY();
if (mode==1)
{
if ((x1-mx)*(x1-mx)+(y1-my)*(y1-my)<2500)
{
ms=ms+1;
lab2.setText("成績:"+ms);
mode=0;
repaint();
}
}
else if (mode==2)
{
if ((x2-mx)*(x2-mx)+(y2-my)*(y2-my)<2500)
{
ms=ms+1;
lab2.setText("成績:"+ms);
mode=0;
repaint();
}
}

};
public void mouseReleased(MouseEvent e){ };
public void mouseClicked(MouseEvent e){ };

//滑鼠事件處理類別寫在這裡
/***主程式***/
public static void main(String args[]) //程式起點
{
tt12 app=new tt12(); //名稱改一改，啟動UI元件
app.addWindowListener(new WindowAdapter(){
public void windowClosing(WindowEvent e)
{
System.exit(0);
}
}); //處理視窗關閉要求
}
}