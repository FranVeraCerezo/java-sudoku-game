package pkgPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Objects;

public class Panel extends JPanel {

    private static final JCheckBox highlightBox = new JCheckBox("Highlight Background", false);
    private static final String[] levels = {"Easy", "Medium", "Hard"};
    private static final JComboBox<String> levelsBox = new JComboBox<>(levels);
    private static final JButton playButton = new JButton("Play");
    private static final JButton checkButton = new JButton("Check");
    private static final JButton hintButton = new JButton("Hint");
    private final static JLabel pointsNumberLabel = new JLabel("0");
    private final JButton exitButton = new JButton("Exit");
    private static JDialog winDialog, looseDialog;
    private static final JLabel winLabel = new JLabel("Congratulations!! You have win!");
    private static final JLabel looseLabel = new JLabel("Try it again.");

    private static final int ROWS = 9;
    private static final int COLUMNS = 9;
    private static final int SUBROWS = 3;
    private static final int SUBCOLUMNS =3;
    private static final JTextField[][] fields = new JTextField[ROWS][COLUMNS];

    private static final Color highlightedColor = new Color(192, 241, 255);
    private static final Color selectedColor = new Color(132, 222, 245);


    public Panel(JFrame frame, Controller controller) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                ImageIcon icon = new ImageIcon("image/sudoku.png");
                frame.setIconImage(icon.getImage());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocation(100, 100);
                frame.setLayout(new BorderLayout());
                frame.add(new SudokuBoard());
                frame.add(new MenuPane(), BorderLayout.AFTER_LINE_ENDS);
                frame.pack();
                frame.setVisible(true);
                addListeners(controller);

                winDialog = new JDialog(frame);
                winDialog.setPreferredSize(new Dimension(450,100));
                winDialog.setLocation(250, 400);
                winDialog.setLayout(new BorderLayout());
                winLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
                winLabel.setHorizontalAlignment(JLabel.CENTER);
                winDialog.add(winLabel);
                winDialog.pack();

                looseDialog = new JDialog(frame);
                looseDialog.setPreferredSize(new Dimension(450,100));
                looseDialog.setLocation(250, 400);
                looseDialog.setLayout(new BorderLayout());
                looseLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
                looseLabel.setHorizontalAlignment(JLabel.CENTER);
                looseDialog.add(looseLabel);
                looseDialog.pack();
            }
        });
    }

    public class MenuPane extends JPanel {

        public MenuPane() {
            setBorder(new EmptyBorder(4, 4, 4, 4));
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            highlightBox.setActionCommand("highlightBackground");
            playButton.setActionCommand("Play");
            checkButton.setActionCommand("Check");
            hintButton.setActionCommand("Hint");
            exitButton.setActionCommand("Exit");

            enableCheckButton(false);
            enableHintButton(false);

            JLabel pointsLabel = new JLabel("Points:");
            pointsLabel.setFont(new Font("SansSerif",Font.BOLD,14));
            pointsNumberLabel.setFont(new Font("SansSerif",Font.BOLD,14));
            ImageIcon starImg = new ImageIcon("image/coins.png");
            ImageIcon starResize = new ImageIcon(starImg.getImage().getScaledInstance(22,22, java.awt.Image.SCALE_SMOOTH));
            JLabel starLabel = new JLabel(starResize);
            JLabel diff = new JLabel("Difficulty level: ");
            diff.setFont(new Font("SansSerif",Font.BOLD,14));
            levelsBox.setFont(new Font("SansSerif",Font.BOLD,12));

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 0;
            gbc.weighty = 1;
            gbc.gridwidth = 3;
            gbc.anchor = GridBagConstraints.LINE_START;
            //gbc.fill = GridBagConstraints.HORIZONTAL;
            add(highlightBox, gbc);

            gbc.gridy++;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            add(pointsLabel, gbc);

            gbc.gridx++;
            gbc.anchor = GridBagConstraints.LINE_END;
            add(pointsNumberLabel, gbc);

            gbc.gridx++;
            gbc.anchor = GridBagConstraints.LINE_START;
            add(starLabel, gbc);

            gbc.gridwidth =2;
            gbc.gridy++;
            gbc.gridx = 0;
            add(diff, gbc);

            gbc.gridwidth = 1;
            gbc.gridx = 3;
            gbc.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
            add(levelsBox,gbc);

            gbc.gridy++;
            gbc.gridx = 0;
            gbc.gridwidth = 3;
            gbc.anchor = GridBagConstraints.CENTER;
            add(playButton, gbc);

            gbc.gridy++;
            add(checkButton, gbc);

            gbc.gridy++;
            add(hintButton, gbc);

            gbc.gridy++;
            add(exitButton, gbc);
        }
    }

    public static class SudokuBoard extends JPanel {

        public SudokuBoard() {
            setBorder(new EmptyBorder(30, 30, 30, 10));
            SubBoard[] subBoards = new SubBoard[9];
            setLayout(new GridLayout(SUBROWS, SUBCOLUMNS));
            for (int row = 0; row < SUBROWS; row++) {
                for (int col = 0; col < SUBCOLUMNS; col++) {
                    int index = (row * SUBROWS) + col;
                    SubBoard board = new SubBoard(index);
                    board.setBorder(new LineBorder(Color.GRAY, 3));
                    subBoards[index] = board;
                    add(board);
                }
            }
        }
    }

    public static class SubBoard extends JPanel{

        public SubBoard(int bigIndex) {
            setLayout(new GridLayout(3, 3));
            int offsetY,offsetX;
            if(bigIndex<3){
                offsetY = 0;
            }else if(bigIndex<6){
                offsetY = 3;
            }else{
                offsetY = 6;
            }
            if(bigIndex%3==0){
                offsetX = 0;
            }else if(bigIndex%3==1){
                offsetX = 3;
            }else{
                offsetX = 6;
            }
            for (int row = 0; row < SUBROWS; row++) {
                int indexY = offsetY + row;
                for (int col = 0; col < SUBCOLUMNS; col++) {
                    int indexX =offsetX + col;
                    JTextField field = new JTextField();
                    field.setPreferredSize(new Dimension(60, 60));
                    field.setHorizontalAlignment(JTextField.CENTER);
                    field.setDocument(new JTextFieldLimit());
                    field.setEditable(false);
                    fields[indexY][indexX] = field;
                    fields[indexY][indexX].setName(indexY + " " + indexX);
                    add(field);
                }
            }
        }
    }

    public void addListeners(Controller controller) {
        playButton.addActionListener(controller);
        checkButton.addActionListener(controller);
        hintButton.addActionListener(controller);
        exitButton.addActionListener(controller);
        highlightBox.addActionListener(controller);
    }

    public void fillSudoku(int[][] sudoku){
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if(sudoku[i][j]!=0) {
                    fields[i][j].setEditable(false);
                    fields[i][j].setBackground(Color.WHITE);
                    fields[i][j].setFont(new Font("SansSerif", Font.BOLD, 25));
                    fields[i][j].setText(String.valueOf(sudoku[i][j]));
                    fields[i][j].setForeground(Color.BLACK);
                }else{
                    fields[i][j].setBackground(Color.WHITE);
                    fields[i][j].setText("");
                    fields[i][j].setEditable(true);
                    fields[i][j].setFont(new Font("SansSerif", Font.PLAIN, 25));
                    fields[i][j].setForeground(Color.DARK_GRAY);
                }
            }
        }
    }

    public static int[][] getSudoku(){
        int[][] sudoku = new int[9][9];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if(!Objects.equals(fields[i][j].getText(), "")){
                    sudoku[i][j] = Integer.parseInt(fields[i][j].getText());
                }else{
                    sudoku[i][j] = 0;
                }
            }
        }
        return sudoku;
    }

    public void checkSudoku(int[][] solvedSudoku){
        boolean ok= true;

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if(solvedSudoku[i][j]!=0){
                    ok=false;
                    if(Objects.equals(fields[i][j].getText(),"")) {
                        fields[i][j].setForeground(Color.RED);
                    }else{
                        fields[i][j].setForeground(new Color(255, 181, 0));
                    }
                    fields[i][j].setText(String.valueOf(solvedSudoku[i][j]));
                }
                fields[i][j].setEditable(false);
            }
        }

        if(ok){
            looseDialog.dispose();
            winDialog.setVisible(true);
        }else{
            winDialog.dispose();
            looseDialog.setVisible(true);
        }
    }

    public void setHint(int[][] hint){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(hint[i][j]!=0){
                    fields[i][j].setText(String.valueOf(hint[i][j]));
                    fields[i][j].setEditable(false);
                    fields[i][j].setForeground(Color.GREEN);
                }
            }
        }
    }

    public void setPoints(int point){
        if(point<5){
            enableHintButton(false);
        }
        pointsNumberLabel.setText(String.valueOf(point));
    }

    public static int getPoints(){
        return Integer.parseInt(pointsNumberLabel.getText());
    }

    public static String getTextComboBox(){

        return levelsBox.getItemAt(levelsBox.getSelectedIndex());
    }

    public static boolean highlightSelected(){
        return highlightBox.isSelected();
    }

    public static void highlightBackground(boolean enable, Controller controller){
        if(enable){
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Panel.fields[i][j].addFocusListener(controller);
                }
            }
        }else{
            setBackgroundWhite();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Panel.fields[i][j].removeFocusListener(controller);
                }
            }
        }
    }

    public static void setBackgroundColor(int row, int col){
        setBackgroundWhite();
        for (int i = 0; i < 9; i++) {
            Panel.fields[row][i].setBackground(highlightedColor);
            Panel.fields[i][col].setBackground(highlightedColor);
        }

        if(col<3){
            for (int i = 0; i < 3; i++) {
                colorBox(row, i);
            }
        }else if(col<6){
            for (int i = 3; i < 6; i++) {
                colorBox(row, i);
            }
        }else{
            for (int i = 6; i < 9; i++) {
                colorBox(row, i);
            }
        }
        Panel.fields[row][col].setBackground(selectedColor);
    }

    private static void colorBox(int row, int i) {
        if(row%3==0){
            Panel.fields[row+1][i].setBackground(highlightedColor);
            Panel.fields[row+2][i].setBackground(highlightedColor);
        }else if(row%3==1){
            Panel.fields[row-1][i].setBackground(highlightedColor);
            Panel.fields[row+1][i].setBackground(highlightedColor);
        }else{
            Panel.fields[row-1][i].setBackground(highlightedColor);
            Panel.fields[row-2][i].setBackground(highlightedColor);
        }
    }

    private static void setBackgroundWhite(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Panel.fields[i][j].setBackground(Color.WHITE);
            }
        }
    }

    public static void enableCheckButton(boolean enabled){
        checkButton.setEnabled(enabled);
    }

    public static void enableHintButton(boolean enabled){
        hintButton.setEnabled(enabled);
    }

    public static void enablePlayButton(boolean enabled){
        playButton.setEnabled(enabled);
    }
}

