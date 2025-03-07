package pkgPanel;

import pkgClient.MyClient;
import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Objects;

public class Controller implements ActionListener, FocusListener{
    JFrame frame;



    public Controller(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equalsIgnoreCase("Play")){
            String level = Panel.getTextComboBox();
            if(Objects.equals(level, "Easy")){
                MyClient.out.println("easyMode");
            }else if(Objects.equals(level, "Medium")){
                MyClient.out.println("mediumMode");
            }else if (Objects.equals(level, "Hard")){
                MyClient.out.println("hardMode");
            }
            Panel.enablePlayButton(false);
            if(Panel.getPoints()>5){
                Panel.enableHintButton(true);
            }

        } else if(e.getActionCommand().equalsIgnoreCase("Check")){
            MyClient.out.println("check");
            int[][] sudoku = Panel.getSudoku();
            MyClient.out.println(Arrays.deepToString(sudoku));
            Panel.enableCheckButton(false);
            Panel.enableHintButton(false);

        }else if(e.getActionCommand().equalsIgnoreCase("Hint")){
            MyClient.out.println("hint");
            int[][] sudoku = Panel.getSudoku();
            MyClient.out.println(Arrays.deepToString(sudoku));

        } else if(e.getActionCommand().equalsIgnoreCase("Exit")) {
            frame.dispose();
            MyClient.out.println("exit");
        }else if(e.getActionCommand().equalsIgnoreCase("highlightBackground")) {
            Panel.highlightBackground(Panel.highlightSelected(), this);
        }else{
            System.err.println("Error: unexpected event");
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        int row = Character.getNumericValue(e.getComponent().getName().charAt(0));
        int col = Character.getNumericValue(e.getComponent().getName().charAt(2));
        Panel.setBackgroundColor(row, col);
    }

    @Override
    public void focusLost(FocusEvent e) {
    }

}