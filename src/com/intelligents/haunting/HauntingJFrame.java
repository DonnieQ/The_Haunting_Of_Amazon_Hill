package com.intelligents.haunting;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class HauntingJFrame extends JWindow implements ActionListener{

    JWindow window = new JWindow();

    JTextArea rpsGame;
    JPanel promptPanel;
    JScrollPane scrollPane;
    boolean bossDead = false;
    String[] userResponse;
    JLabel questionToUser = new JLabel();
    JTextField userInput = new JTextField();
    JButton showJournal = new JButton("Journal");
    JButton showMap = new JButton("Map");
    JTextArea textDisplayGameWindow = new JTextArea();
    JTextArea textDisplayJournal = new JTextArea();
    JTextField desicionField = new JTextField();
    boolean decisionListener = false;
    JFrame frame;
    JPanel panel_00;
    JPanel panel_01;
    JPanel panel_02;
    JPanel panel_03;
    JPanel panel_04;
    JTextField numInput;
    boolean calledOnce=false;
    String openingRoom;
    private final MusicPlayer themeSong = new MusicPlayer("resources/Sounds/VIKINGS THEME SONG.wav");
    Game game;
    Controller controller;


    public HauntingJFrame() throws IOException {
        splashWindow();
        gameWindow();
        game = new Game(this);
        controller = new Controller(game);
        openingRoom = "resources/Images/Map(Lobby).png";
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

        // Allows for scrolling is text extends panel
        JScrollPane scrollPane = new JScrollPane(textDisplayGameWindow);
        scrollPane.setPreferredSize(new Dimension(700,500));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // Text field for user to input
//        userInput.setText("What do you want to do?");
        userInput.setSize(new Dimension(500,100));
        userInput.setFont(new Font("Consolas", Font.CENTER_BASELINE, 15));
        userInput.setForeground(Color.white);
        userInput.setBackground(Color.DARK_GRAY);
        userInput.setCaretColor(Color.BLACK);

        panel_01.setBackground(Color.BLACK);
        panel_00.add(scrollPane);
        panel_01.setLayout(new GridLayout(1,2));
//        panel_01.add(questionToUser);
//        panel_01.add(Box.createHorizontalGlue());
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
            try {
                showMap(openingRoom);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        if (e.getSource() == userInput) {
            System.out.println(userInput.getText());
            userResponse = userInput.getText().strip().toLowerCase().split(" ");
            userInput.setText("");
            controller.kickoffResponse(userResponse);
        }

    }

    public void setTextbox(String text) {
        textDisplayGameWindow.setText(text);
    }

    public String[] getUserResponse() {
        return userResponse;
    }

    private void showJournal() {
        frame = new JFrame("Journal");
        frame.setSize(500, 500);

        textDisplayJournal = new JTextArea();
        DefaultCaret caret = (DefaultCaret) textDisplayJournal.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        textDisplayJournal.setCaretPosition(0);
        textDisplayJournal.setText("This is your journal! There is text added here... blah blah blah");
        textDisplayJournal.setLineWrap(true);
        textDisplayJournal.setWrapStyleWord(true);
        textDisplayJournal.setBorder(BorderFactory.createBevelBorder(1));
        textDisplayJournal.setForeground(new Color(0, 60, 70));
        textDisplayJournal.setFont(new Font("Comic Sans",Font.BOLD, 15));
        textDisplayJournal.setEditable(false);
        textDisplayJournal.setBackground(new Color(196, 223, 230));

        frame.add(textDisplayJournal, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    void showMap(String mapFilePath) throws IOException {
        File currentRoom = new File(mapFilePath);
        frame = new JFrame("Map");
        frame.setSize(500, 500);

        BufferedImage mapImage = ImageIO.read(currentRoom);
        JLabel picLabel = new JLabel(new ImageIcon(mapImage));


        frame.add(picLabel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void splashWindow() throws IOException {
//        // Read the ascii file line by line and build it into a string
//        FileInputStream asciiSplashScreen = new FileInputStream("resources/splashScreen");
//        DataInputStream in = new DataInputStream(asciiSplashScreen);
//        BufferedReader br = new BufferedReader(new InputStreamReader(in));
//        StringBuilder sb = new StringBuilder();
//        String strLine;
////        JLabel ascii = new JLabel("");
//        while ((strLine = br.readLine()) != null)   {
//            // Print the content on the console
//            System.out.println (strLine);
//            sb.append(strLine);
//            JTextField ascii = new JTextField(String.valueOf(sb));
//            JPanel panel = new JPanel();
//            ascii.setFont(new Font("Monospaced", Font.PLAIN, 12));
////        ascii.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
//            ascii.setSize(1000, 1000);
//            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//            panel.add(ascii);
//            panel.setVisible(true);
//            window.getContentPane().add(panel);
//            window.setBounds(500, 150, 300, 200);
//        }
//        in.close();
        themeSong.playSoundEffect();
        themeSong.setVolume((float) -10.69);

        ImageIcon splashScreenImage = new ImageIcon("resources/Images/asciiSplashScreen.png");
        JLabel image = new JLabel(splashScreenImage);
        window.getContentPane().add(image);
//        window.setBounds(500, 150, 300, 200);
//
//        window.pack();
//        JLabel ascii = new JLabel(String.valueOf(sb));
//        ascii.setFont(new Font("Monospaced", Font.PLAIN, 12));
////        ascii.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
//        ascii.setSize(100, 100);
//        window.getContentPane().add(ascii);
        window.setBounds(500, 150, 300, 200);
//
        window.pack();
//        window.setSize(window.getWidth() * 3, window.getHeight() * 3);
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

