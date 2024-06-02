import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MyPainter extends JFrame {
    private JPanel canvas;
    private String currentTool = "畫筆";
    private String currentShape = "長方形";
    private Point startPoint = null;
    private Point currentPoint = null;
    private List<Shape> shapes = new ArrayList<>();
    // 呈現目前功能
    private JLabel statusLabel;

    public MyPainter() {
        setTitle("小畫家");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // 繪畫功能
        JMenu toolsMenu = new JMenu("功能");
        menuBar.add(toolsMenu);

        JMenuItem brushMenuItem = new JMenuItem("畫筆");
        brushMenuItem.addActionListener(e -> {
            currentTool = "畫筆";
            statusLabel.setText("目前功能: " + currentTool);
        });
        toolsMenu.add(brushMenuItem);

        // 圖形功能
        JMenu shapeMenu = new JMenu("圖形");
        toolsMenu.add(shapeMenu);

        JMenuItem rectMenuItem = new JMenuItem("長方形");
        rectMenuItem.addActionListener(e -> {
            currentTool = "圖形";
            currentShape = "長方形";
            statusLabel.setText("目前功能: " + currentShape);
        });
        shapeMenu.add(rectMenuItem);

        JMenuItem squareMenuItem = new JMenuItem("正方形");
        squareMenuItem.addActionListener(e -> {
            currentTool = "圖形";
            currentShape = "正方形";
            statusLabel.setText("目前功能: " + currentShape);
        });
        shapeMenu.add(squareMenuItem);

        JMenuItem circleMenuItem = new JMenuItem("圓形");
        circleMenuItem.addActionListener(e -> {
            currentTool = "圖形";
            currentShape = "圓形";
            statusLabel.setText("目前功能: " + currentShape);
        });
        shapeMenu.add(circleMenuItem);

        JMenuItem triangleMenuItem = new JMenuItem("三角形");
        triangleMenuItem.addActionListener(e -> {
            currentTool = "圖形";
            currentShape = "三角形";
            statusLabel.setText("目前功能: " + currentShape);
        });
        shapeMenu.add(triangleMenuItem);

        JMenuItem lineMenuItem = new JMenuItem("直線");
        lineMenuItem.addActionListener(e -> {
            currentTool = "圖形";
            currentShape = "直線";
            statusLabel.setText("目前功能: " + currentShape);
        });
        shapeMenu.add(lineMenuItem);

        JMenuItem clearMenuItem = new JMenuItem("清除");
        clearMenuItem.addActionListener(e -> clearCanvas());
        toolsMenu.add(clearMenuItem);

        // 狀態標籤
        statusLabel = new JLabel("目前功能: 畫筆" );
        add(statusLabel, BorderLayout.SOUTH);

        // 建造 canvas
        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Shape shape : shapes) {
                    shape.draw(g);
                }
                if ("圖形".equals(currentTool) && startPoint != null && currentPoint != null) {
                    drawCurrentShape(g);
                }
            }

            // 匿名內部類別
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        startPoint = e.getPoint();
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if ("圖形".equals(currentTool) && startPoint != null) {
                            currentPoint = e.getPoint();
                            Shape shape = createShape();
                            if (shape != null) {
                                shapes.add(shape);
                            }
                            repaint();
                            startPoint = null;
                            currentPoint = null;
                        }
                    }
                });

                addMouseMotionListener(new MouseAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        if ("畫筆".equals(currentTool)) {
                            Graphics g = getGraphics();
                            g.fillOval(e.getX(), e.getY(), 5, 5);
                        } else if ("圖形".equals(currentTool) && startPoint != null) {
                            currentPoint = e.getPoint();
                            repaint();
                        }
                    }
                });
            }
        };

        canvas.setBackground(Color.WHITE);
        add(canvas, BorderLayout.CENTER);
    }

    private Shape createShape() {
        int x = Math.min(startPoint.x, currentPoint.x);
        int y = Math.min(startPoint.y, currentPoint.y);
        int width = Math.abs(currentPoint.x - startPoint.x);
        int height = Math.abs(currentPoint.y - startPoint.y);
        switch (currentShape) {
            case "長方形":
                return new Rectangle(x, y, width, height);
            case "正方形":
                int side = Math.min(width, height);
                if(startPoint.x < currentPoint.x)
                    x = startPoint.x;
                else
                    x = startPoint.x - side;
                if(startPoint.y < currentPoint.y)
                    y = startPoint.y;
                else
                    y = startPoint.y - side;
                return new Rectangle(x, y, side, side);
            case "圓形":
                int diameter = Math.min(width, height);
                if(startPoint.x < currentPoint.x)
                    x = startPoint.x;
                else
                    x = startPoint.x - diameter;
                if(startPoint.y < currentPoint.y)
                    y = startPoint.y;
                else
                    y = startPoint.y - diameter;
                return new Oval(x, y, diameter, diameter);
            case "三角形":
                return new Triangle(startPoint.x, startPoint.y,currentPoint.x, currentPoint.y);
            case "直線":
                return new Line(startPoint.x, startPoint.y, currentPoint.x, currentPoint.y);
            default:
                return null;
        }
    }

    private void drawCurrentShape(Graphics g) {
        int x = Math.min(startPoint.x, currentPoint.x);
        int y = Math.min(startPoint.y, currentPoint.y);
        int width = Math.abs(currentPoint.x - startPoint.x);
        int height = Math.abs(currentPoint.y - startPoint.y);
        switch (currentShape) {
            case "長方形":
                g.drawRect(x, y, width, height);
                break;
            case "正方形":
                int side = Math.min(width, height);
                if(startPoint.x < currentPoint.x)
                    x = startPoint.x;
                else
                    x = startPoint.x - side;
                if(startPoint.y < currentPoint.y)
                    y = startPoint.y;
                else
                    y = startPoint.y - side;

                g.drawRect(x, y, side, side);
                break;
            case "圓形":
                int diameter = Math.min(width, height);
                if(startPoint.x < currentPoint.x)
                    x = startPoint.x;
                else
                    x = startPoint.x - diameter;
                if(startPoint.y < currentPoint.y)
                    y = startPoint.y;
                else
                    y = startPoint.y - diameter;
                g.drawOval(x, y, diameter, diameter);
                break;
            case "三角形":
                drawTriangle(g, startPoint.x, startPoint.y, currentPoint.x, currentPoint.y);
                break;
            case "直線":
                g.drawLine(startPoint.x, startPoint.y, currentPoint.x, currentPoint.y);
                break;
        }
    }
    // 繪製三角形
    private void drawTriangle(Graphics g, int x1, int y1, int x2, int y2) {
        int[] xPoints = {x1, x2, (x1 + x2) / 2};
        int[] yPoints = {y1, y2, y1 - (y2 - y1)};
        g.drawPolygon(xPoints, yPoints, 3);
    }

    abstract class Shape {
        int x, y, width, height;

        Shape(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        abstract void draw(Graphics g);
    }

    class Rectangle extends Shape {
        Rectangle(int x, int y, int width, int height) {
            super(x, y, width, height);
        }

        @Override
        void draw(Graphics g) {
            g.drawRect(x, y, width, height);
        }
    }

    class Oval extends Shape {
        Oval(int x, int y, int width, int height) {
            super(x, y, width, height);
        }

        @Override
        void draw(Graphics g) {
            g.drawOval(x, y, width, height);
        }
    }

    class Triangle extends Shape {
        int x2, y2;

        Triangle(int x1, int y1, int x2, int y2) {
            super(x1, y1, Math.abs(x1 - x2), Math.abs(y1 - y2));
            this.x2 = x2;
            this.y2 = y2;
        }

        @Override
        void draw(Graphics g) {
            int[] xPoints = {x, x2, (x + x2) / 2};
            int[] yPoints = {y, y2, y - (y2 - y)};
            g.drawPolygon(xPoints, yPoints, 3);
        }
    }

    class Line extends Shape {
        int x2, y2;

        Line(int x1, int y1, int x2, int y2) {
            super(x1, y1, Math.abs(x1 - x2), Math.abs(y1 - y2));
            this.x2 = x2;
            this.y2 = y2;
        }

        @Override
        void draw(Graphics g) {
            g.drawLine(x, y, x2, y2);
        }
    }

    private void clearCanvas() {
        shapes.clear();
        canvas.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MyPainter painter = new MyPainter();
            painter.setVisible(true);
        });
    }
}
