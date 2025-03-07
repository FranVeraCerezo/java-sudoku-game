package pkgClient;
import pkgUtils.SocketUtils;
import pkgPanel.Panel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

public class MyClient extends NetworkClient{

    public static PrintWriter out;
    private static BufferedReader in;
    public MyClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected void handleConnection(Socket client, Panel panel) throws IOException {
        int points;
        int[][] sudoku = new int[9][9];
        int[][] solvedSudoku = new int[9][9];
        String line;

        in = SocketUtils.getReader(client);
        out = SocketUtils.getWriter(client);

        try {
            out.println("SYN");

            if (Objects.equals(in.readLine(), "ACK")) {
                while (!Objects.equals(line = in.readLine(), "exit")) {
                    if (Objects.equals(line, "Sending Sudoku")) {
                        readSudoku(sudoku);
                        panel.fillSudoku(sudoku);
                        Panel.enableCheckButton(true);
                        Panel.enablePlayButton(true);
                    } else if (Objects.equals(line, "Checked Sudoku")) {
                        readSudoku(solvedSudoku);
                        panel.checkSudoku(solvedSudoku);
                        points = Integer.parseInt(in.readLine());
                        panel.setPoints(points);
                    } else if (Objects.equals(line, "Sending Hint")) {
                        readSudoku(solvedSudoku);
                        panel.setHint(solvedSudoku);
                        points = Integer.parseInt(in.readLine());
                        panel.setPoints(points);
                    }
                }
            }
            client.close();
        }catch (SocketException e){
            client.close();
        }
    }

    private void readSudoku(int[][] sudoku) throws IOException {
        String line;
        line = in.readLine();
        String numbers = line.replaceAll("[\\[\\]\\s+]","");
        String[] numberString = numbers.split(",");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudoku[i][j] = Integer.parseInt(numberString[i*9+j]);
            }
        }
    }
}
