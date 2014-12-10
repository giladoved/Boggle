import java.applet.Applet;
import java.awt.Button;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.Timer;

public class Starter extends Applet implements ActionListener {

	Button[][] table = new Button[4][4];
	char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
			'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
			'Y', 'Z' };
	double[] probabilites = {.08167, .01492, .02782, .04253, .12702, .02228, .02015, .06094, .06966, .00153, .00772, .04025, .02406, .06749,
			.07507, .01929, .00095, .05987, .06327, .09056, .02758, .00978, .02360, .00150, .01974, .00074};

	String guess = "";
	Timer timer;
	int wordsFound = 0;
	int timeRemaining = 120;
	int points = 0;
	boolean isFinished = false;
	ArrayList<String> englishWords = new ArrayList<String>();
	ArrayList<String> guessedWords = new ArrayList<String>();
	
	TextField nameField;
	Label word = new Label("Find as many words as you can!");
	Label guessLbl = new Label("");
	Button submit = new Button("Submit");
	Button clear = new Button("Clear");
	Label timeLbl = new Label("Seconds Left: 120");
	Label pointsLbl = new Label("Points: " + points);

	public void NewRound() {
		guessedWords = new ArrayList<String>();
		guess = "";
		guessLbl.setText("");
		timeLbl.setText("Seconds Left: 120");
		timeRemaining = 60;
		points = 0;
		wordsFound = 0;
		pointsLbl.setText("Points: " + points);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				Random r = new Random();
				int letter = r.nextInt(26);
				table[i][j].setLabel(String.valueOf(letters[letter])
						.toString());
				table[i][j].setEnabled(true);
			}
		}
		timer = new Timer(1000, new CountdownTimerListener());
		timer.start();
		if (timeRemaining > 1) {
			isFinished = true;
		}
	}

	public void init() {
		loadWordsToArray();
		setLayout(null);
		setSize(400, 450);
		if (!isFinished) {
			pointsLbl.setBounds(265, 60, 150, 30);
			pointsLbl.setFont(new Font("Arial", Font.PLAIN, 14));
			add(pointsLbl);
			timeLbl.setBounds(20, 60, 150, 30);
			timeLbl.setFont(new Font("Arial", Font.PLAIN, 14));
			add(timeLbl);
			submit.setBounds(280, 370, 80, 40);
			submit.setFont(new Font("Arial", Font.PLAIN, 14));
			submit.addActionListener(this);
			add(submit);
			clear.setBounds(20, 370, 60, 40);
			clear.setFont(new Font("Arial", Font.PLAIN, 14));
			clear.addActionListener(this);
			add(clear);
			guessLbl.setBounds(90, 375, 400, 30);
			guessLbl.setFont(new Font("Arial", Font.PLAIN, 20));
			add(guessLbl);
			wordsFound = 0;
			word.setFont(new Font("Arial", Font.PLAIN, 24));
			word.setBounds(20, 5, 400, 50);
			add(word);

			int x = 80;
			int y = 115;

			for (int i = 0; i < 4; i++) {
				x = 80;
				for (int j = 0; j < 4; j++) {
					Random r = new Random();
					int letter = r.nextInt(26);
					table[i][j] = new Button(String.valueOf(letters[letter])
							.toString());
					table[i][j].setBounds(x, y, 50, 50);
					table[i][j].setFont(new Font("Arial", Font.PLAIN, 18));
					x += 60;
					add(table[i][j]);
					table[i][j].addActionListener(this);
				}
				y += 60;
			}
		}
		timer = new Timer(1000, new CountdownTimerListener());
		timer.start();
		if (timeRemaining > 1) {
			isFinished = true;
		}
	}

	public void loadWordsToArray() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File("allwords.txt")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		try {
			while ((line = br.readLine()) != null) {
				if (line.length() > 2)
					englishWords.add(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	class CountdownTimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			timeRemaining--;
			if (timeRemaining > 0) {
				if (timeRemaining > 9)
				{
				timeLbl.setText("Seconds Left: "
						+ String.valueOf(timeRemaining));
				}
				else
				{
					timeLbl.setText("Seconds Left: 0:0"
							+ String.valueOf(timeRemaining));
				}
			} else {
				JOptionPane.showMessageDialog(null, "Time's Up! You earned " + points + " points and found " + wordsFound + " words!", "Good Game!", JOptionPane.PLAIN_MESSAGE);
				if (guessedWords.size() > 0) {
					JList list = new JList(guessedWords.toArray());
			        JScrollPane scrollPane = new JScrollPane(list);  
			        JOptionPane.showMessageDialog(null, scrollPane, "These are the words you found", JOptionPane.INFORMATION_MESSAGE);
				}
				
				timeLbl.setText("Time's Up!!");
				timer.stop();
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) {
						table[i][j].setEnabled(false);
					}
				}
				int n = JOptionPane.showConfirmDialog(null,
					    "Would you like to play again?",
					    "Play again?",
					    JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION)
				{
					NewRound();
				}
			}
		}
	}
	
	public boolean isWord(String word) {
		int high = englishWords.size() - 1;
		int low = 0;
		while (high >= low) {
			int mid = ((high - low) / 2) + low;
			if (word.compareToIgnoreCase(englishWords.get(mid)) < 0) {
				high = mid - 1;
			} 
			else if (word.compareToIgnoreCase(englishWords.get(mid)) > 0) {
				low = mid + 1;
			}
			else {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (e.getSource() == table[i][j]) {
					if (guess.length() < 10) { 
						guess += table[i][j].getLabel();
						table[i][j].setEnabled(false);
						guessLbl.setText(guess);
					}
				}
			}
		}
		if (e.getSource() == submit) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
						table[i][j].setEnabled(true);
				}
			}
			timer.stop();
			if (isWord(guess.toLowerCase())) {
				if (!guessedWords.contains(guess.toLowerCase())) {
					JOptionPane.showMessageDialog(null, guess.toLowerCase()
							+ " is a word!", "It's a word!", JOptionPane.PLAIN_MESSAGE);
					points += guess.length();
					wordsFound++;
					pointsLbl.setText("Points: " + points);
					guessedWords.add(guess.toLowerCase());
				}
				else
				{
					JOptionPane.showMessageDialog(null, "You already guess that word! Keep thinking!", "Already Guessed", JOptionPane.PLAIN_MESSAGE);
				}
			} else {
				if (guess.length() < 3) {
					JOptionPane.showMessageDialog(null, "Please, you're better than that... Find a longer word", "Longer Words", JOptionPane.PLAIN_MESSAGE);
				}else {
					JOptionPane.showMessageDialog(null, guess.toLowerCase()
						+ " is NOT a word. Try again!", "Not a word", JOptionPane.PLAIN_MESSAGE);
				}
			}
			guess = "";
			guessLbl.setText("");
			timer.start();
		}  else if (e.getSource() == clear) {
			guess = "";
			guessLbl.setText(guess);
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					table[i][j].setEnabled(true);
				}
			}
		}
	}
}
