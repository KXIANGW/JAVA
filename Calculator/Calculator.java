import java.awt.*;
import java.awt.event.*;

public class Calculator extends Frame implements ActionListener {

    private TextField textField;
    private String text = "";
    private double result = 0;
    private boolean clear = false;

    public Calculator() {
        setLayout(new BorderLayout());

        textField = new TextField(20);
        textField.setEditable(false);
        // 釘選在上方
        add(textField, BorderLayout.NORTH);

        Panel buttonPanel = new Panel();
        // 由左至右 由上至下放置
        buttonPanel.setLayout(new GridLayout(5, 4));

        String[] buttonLabels = {
            "(", ")", "C", "CE",
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+"
        };

        for (String label : buttonLabels) {
            Button button = new Button(label);
            button.addActionListener(this);
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);

        setTitle("1101327's Calculator");
        setSize(250, 200);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if (clear){
            text = "";
            clear = false;
        }
        
        if (command.equals("=")) {
            try {
                result = Express.calculate(text);
                textField.setText(Double.toString(result));
                clear = true;
                return;
            } 
            catch (ArithmeticException E) {
                textField.setText("Error");
            }
        } 
        else if (command.equals("C")) {
            text = "";
        } 
        else if (command.equals("CE")) {
            // 去除字串中最後一位數
            text = text.substring(0, text.length() - 1);
        } 
        else {
            text += command; 
        }
        textField.setText(text);
    }


    // Main Code
    public static void main(String[] args) {
        new Calculator();
    }
}
