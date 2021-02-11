package com.intelligents.haunting;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;

public class HauntingJFrame extends JWindow implements ActionListener{

    JWindow window = new JWindow();

    String[] userResponse;
    JTextField userInput = new JTextField();
    JButton showJournal = new JButton("Journal");
    JButton showMap = new JButton("Map");
    JTextArea textDisplayGameWindow = new JTextArea();
    JTextArea textDisplayJournal = new JTextArea();
    JFrame frame;
    JPanel panel_00;
    JPanel panel_01;
    JPanel panel_02;
    boolean calledOnce=false;
    String currentRoom;
    Game game;
    Controller controller;

    private final MusicPlayer themeSong = new MusicPlayer("resources/Sounds/VIKINGS THEME SONG.wav");


    public HauntingJFrame() throws IOException {
        splashWindow();
        gameWindow();
        game = new Game(this);
        controller = new Controller(game);
//        currentRoom = "resources/Images/Map(Lobby).png";
    }


     private void gameWindow() {
        frame = new JFrame("The Haunting of Amazon Hill");
        frame.setSize(700, 700);

        panel_00 = new JPanel();
        panel_01 = new JPanel();
        panel_02 = new JPanel();


        panel_00.setBackground(Color.black);
        panel_02.setBackground(Color.DARK_GRAY);
        panel_02.add(showJournal);
        panel_02.add(Box.createHorizontalGlue());
        panel_02.add(showMap);

        showJournal.addActionListener(this);
        showMap.addActionListener(this);

        // Unchangeable to user, a textbox to display game text
        DefaultCaret caret = (DefaultCaret) textDisplayGameWindow.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        textDisplayGameWindow.setCaretPosition(0);
        textDisplayGameWindow.setText("What game would you like to play?\n " +
                "Chapter 1. The Haunting of Amazon Hill\n " +
                "Chapter 2. Chasing Ghosts (COMING SOON!)\n " +
                "Chapter 3. Hangman's Gallows (COMING SOON!)\n " +
                "Press 4. to load saved game\n" +
                "Please enter a number for Chapter: ");
        textDisplayGameWindow.setLineWrap(true);
        textDisplayGameWindow.setWrapStyleWord(true);
        textDisplayGameWindow.setBorder(BorderFactory.createBevelBorder(1));
        textDisplayGameWindow.setForeground(Color.white);
        textDisplayGameWindow.setFont(new Font("Comic Sans",Font.BOLD, 15));
        textDisplayGameWindow.setEditable(false);
        textDisplayGameWindow.setBackground(Color.DARK_GRAY);

        // Allows for scrolling if text extends beyond panel
        JScrollPane scrollPane = new JScrollPane(textDisplayGameWindow);
        scrollPane.setPreferredSize(new Dimension(700,500));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // Text field for user to input
        userInput.setSize(new Dimension(500,100));
        userInput.setFont(new Font("Consolas", Font.CENTER_BASELINE, 15));
        userInput.setForeground(Color.white);
        userInput.setBackground(Color.DARK_GRAY);
        userInput.setCaretColor(Color.BLACK);

        panel_01.setBackground(Color.BLACK);
        panel_00.add(scrollPane);
        panel_01.setLayout(new GridLayout(1,2));
        panel_01.add(userInput);

        frame.add(panel_00, BorderLayout.NORTH);
        frame.add(panel_01, BorderLayout.CENTER);
        frame.add(panel_02, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        if (!calledOnce) {
            userInput.addActionListener(this);
            calledOnce = true;
        }
        userInput.requestFocusInWindow();

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Journal")) {
            showJournal();
        }
        if (e.getActionCommand().equals("Map")) {
            game.processInput(true, new String[]{"open"}, 0);
        }
        if (e.getSource() == userInput) {
            userResponse = userInput.getText().strip().toLowerCase().split(" ");
            userInput.setText("");
            controller.kickoffResponse(userResponse);
        }
    }

    public void setTextBox(String text) {
        textDisplayGameWindow.setText(text);
    }

    public  void appendToTextBox(String text) {
        textDisplayGameWindow.append(text);
    }

    private void showJournal() {
        frame = new JFrame("Journal");
        frame.setSize(500, 500);

        textDisplayJournal = new JTextArea();
        DefaultCaret caret = (DefaultCaret) textDisplayJournal.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        textDisplayJournal.setCaretPosition(0);
//        textDisplayJournal.setText("This is your journal! There is text added here... blah blah blah");
        game.openNewWindowJournalWithUpdatedInfo();
        textDisplayJournal.setLineWrap(true);
        textDisplayJournal.setWrapStyleWord(true);
        textDisplayJournal.setBorder(BorderFactory.createBevelBorder(1));
        textDisplayJournal.setForeground(new Color(0, 60, 70));
        textDisplayJournal.setFont(new Font("Comic Sans",Font.BOLD, 15));
        textDisplayJournal.setEditable(false);
        textDisplayJournal.setBackground(new Color(196, 223, 230));

        // Allows for scrolling if text extends beyond panel
        JScrollPane scrollPane = new JScrollPane(textDisplayJournal);
        scrollPane.setPreferredSize(new Dimension(700,500));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    void showMap() throws IOException {
        currentRoom = game.currentRoom.replaceAll("\\s", "");

        File currentRoomMap = new File("resources/Images/Map(" + currentRoom + ").png");
        frame = new JFrame("Map");
        frame.setSize(500, 500);

        BufferedImage mapImage = ImageIO.read(currentRoomMap);
        JLabel picLabel = new JLabel(new ImageIcon(mapImage));


        frame.add(picLabel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void splashWindow() throws IOException {
        themeSong.playSoundEffect();
        themeSong.setVolume((float) -10.69);

        ImageIcon splashScreenImage = new ImageIcon("resources/Images/asciiSplashScreen.png");
        JLabel image = new JLabel(splashScreenImage);

        window.getContentPane().add(image);
        window.setBounds(500, 150, 300, 200);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        window.setVisible(false);
        window.dispose();
    }

    public void stopThemeSong() {
        themeSong.stopSoundEffect();
    }

}

