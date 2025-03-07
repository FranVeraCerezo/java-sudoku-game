package pkgServer;

import pkgPanel.Panel;
import pkgSudoku.Board;
import pkgSudoku.Generator;
import pkgSudoku.Solver;
import pkgUtils.SocketUtils;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class MyServer extends MultiThreadServer{
	private static boolean correct;

	public MyServer(int port) {
		super(port);
	}


	@Override
	public void handleConnection(Socket socket) throws IOException {
		JFrame frame = new JFrame("Command Prompt 'Sudoku Server'");
		Panel panel = new Panel(frame);
		try {
			PrintWriter out = SocketUtils.getWriter(socket);
			BufferedReader in = SocketUtils.getReader(socket);
			int[][] sudoku;
			int[][] solvedSudoku = new int[9][9];
			int[][] clientSudoku = new int[9][9];

			int points = 0;
			int pointInc = 1;
			String line;
			Generator.DIFFICULTY difficulty = Generator.DIFFICULTY.EASY;

			if(Objects.equals(in.readLine(), "SYN")){
				panel.appendText("Sudoku Server in port: " + getPort());
				panel.appendText("Connection Remote Port: " + socket.getPort());
				panel.appendText("Host: " + socket.getInetAddress().getHostName());
				panel.appendText("Thread: " + Thread.currentThread().getName());

				out.println("ACK");
				panel.appendText("Sending ACK");

				while (!Objects.equals(line = in.readLine(), "exit")) {
					if (line.contains("Mode")) {
						if (line.contains("easy")) {
							panel.appendText("Client asked for an 'Easy Mode' Sudoku.");
							difficulty = Generator.DIFFICULTY.EASY;
							pointInc = 10;
						} else if (line.contains("medium")) {
							panel.appendText("Client asked for a 'Medium Mode' Sudoku.");
							difficulty = Generator.DIFFICULTY.MEDIUM;
							pointInc = 40;
						} else if (line.contains("hard")) {
							panel.appendText("Client asked for a 'Hard Mode' Sudoku.");
							difficulty = Generator.DIFFICULTY.HARD;
							pointInc = 100;
						} else {
							System.err.println("Modo no válido");
						}
						Generator gen = new Generator();
						Board board = gen.generateSudoku(difficulty);
						sudoku = board.getBoardAsInt();
						out.println("Sending Sudoku");
						out.println(Arrays.deepToString(sudoku));
						panel.appendText("Sudoku sent:" + board);
						Solver solver = new Solver(board);
						solver.solveBoard();
						panel.appendText("Sudoku solution:" + board);
						solvedSudoku = board.getBoardAsInt();
					} else if (Objects.equals(line, "check")) {
						panel.appendText("Client asked for check the Sudoku.");
						readSudoku(clientSudoku, in);
						out.println("Checked Sudoku");
						out.println(Arrays.deepToString(checkSudoku(solvedSudoku, clientSudoku)));
						if (correct) {
							points += pointInc;
							panel.appendText("Client won the game!");
							panel.appendText("Client earned points: " + pointInc);
						}else{
							panel.appendText("Client lost the game.");
						}
						panel.appendText("Client total points: " + points);
						out.println(points);
					}else if(Objects.equals(line, "hint")){
						panel.appendText("Client asked for a hint.");
						readSudoku(clientSudoku, in);
						out.println("Sending Hint");
						out.println(Arrays.deepToString(sendHint(solvedSudoku, clientSudoku)));
						points -= 5;
						panel.appendText("Client lost points: 5");
						panel.appendText("Client total points: " + points);
						out.println(points);
					}
				}
			}

			out.println("exit");
			frame.dispose();
			socket.close();
		}catch (SocketException e){
			socket.close();
			frame.dispose();
		}
	}

	private void readSudoku(int[][] solvedSudoku, BufferedReader in) throws IOException {
		String line;
		line = in.readLine();
		String numbers = line.replaceAll("[\\[\\]\\s+]","");
		String[] numberString = numbers.split(",");
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				solvedSudoku[i][j] = Integer.parseInt(numberString[i*9+j]);
			}
		}
	}

	private int[][] checkSudoku(int[][] solvedSudoku, int[][] clientSudoku){
		int[][] failsSudoku = new int[9][9];
		correct = true;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if(solvedSudoku[i][j]!= clientSudoku[i][j]){
					failsSudoku[i][j] = solvedSudoku[i][j];
					correct = false;
				}else{
					failsSudoku[i][j] = 0;
				}
			}
		}
		return failsSudoku;
	}

	private int[][] sendHint(int[][] solvedSudoku, int[][] clientSudoku){
		int[][] possibleHint = new int[9][9];
		int count = 0;
		int index;
		Random rn = new Random();

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if(solvedSudoku[i][j]!=clientSudoku[i][j]){
					possibleHint[i][j] = solvedSudoku[i][j];
					count++;
				}else{
					possibleHint[i][j] = 0;
				}
			}
		}

		if(count!=0) {
			index = rn.nextInt(count);
			count = 0;

			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (possibleHint[i][j] != 0) {
						if (index != count) {
							possibleHint[i][j] = 0;
						}
						count++;
					}
				}
			}
		}
		return possibleHint;
	}
}
