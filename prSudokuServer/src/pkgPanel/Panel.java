package pkgPanel;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class Panel extends JPanel {

    private JScrollPane scroll;
    private JTextArea command;

    public Panel(JFrame frame) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                command = new JTextArea(30,65);
                command.setEditable(false);
                command.setFont(new Font("Monospaced", Font.PLAIN, 15));
                DefaultCaret caret = (DefaultCaret)command.getCaret();
                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
                scroll = new JScrollPane(command);

                frame.setLocation(1200, 100);
                frame.setLayout(new BorderLayout());
                frame.add(scroll, BorderLayout.LINE_START);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    public void appendText(String text){
        command.append(text + "\n");
    }

}

