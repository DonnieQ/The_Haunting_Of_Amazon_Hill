package com.intelligents.haunting;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class HauntingJFrame extends JWindow implements ActionListener{

    JWindow window = new JWindow();

    JTextArea rpsGame;
    JPanel promptPanel;
    JScrollPane scrollPane;
    boolean bossDead = false;
    String result;
    JLabel inputFromUser = new JLabel();
    JTextField textField = new JTextField();
    JButton showJournal = new JButton("Journal");
    JButton showMap = new JButton("Map");
    JTextArea textDisplay;
    JTextField desicionField = new JTextField();
    boolean decisionListener = false;
    JFrame frame;
    JPanel panel_00;
    JPanel panel_01;
    JPanel panel_02;
    JPanel panel_03;
    JPanel panel_04;
    JTextField numInput;


    public HauntingJFrame() throws IOException {
        splashWindow();

        frame = new JFrame("The Haunting of Amazon Hill");
        frame.setSize(500, 500);

        panel_01 = new JPanel();
        panel_02 = new JPanel();


        panel_01.setBackground(Color.black);
        panel_02.setBackground(Color.DARK_GRAY);
        panel_02.add(showJournal);

        showJournal.addActionListener(this);

        textDisplay = new JTextArea();
        DefaultCaret caret = (DefaultCaret) textDisplay.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        textDisplay.setCaretPosition(0);
        textDisplay.setText("This is a placeholder text");
        textDisplay.setLineWrap(true);
        textDisplay.setWrapStyleWord(true);
        textDisplay.setBorder(BorderFactory.createBevelBorder(1));
        textDisplay.setForeground(new Color(0, 60, 70));
        textDisplay.setFont(new Font("Comic Sans",Font.BOLD, 15));
        textDisplay.setEditable(false);
        textDisplay.setBackground(new Color(196, 223, 230));

        frame.add(panel_01, BorderLayout.CENTER);
        frame.add(panel_02, BorderLayout.PAGE_END);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "Journal") {
            showJournal();
        }
        if (e.getActionCommand() == "Map") {
            showMap();
        }

    }

    private void showJournal() {
        frame = new JFrame("Journal");
        frame.setSize(500, 500);

        textDisplay = new JTextArea();
        DefaultCaret caret = (DefaultCaret) textDisplay.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        textDisplay.setCaretPosition(0);
        textDisplay.setText("This is your journal! There is text added here... blah blah blah");
        textDisplay.setLineWrap(true);
        textDisplay.setWrapStyleWord(true);
        textDisplay.setBorder(BorderFactory.createBevelBorder(1));
        textDisplay.setForeground(new Color(0, 60, 70));
        textDisplay.setFont(new Font("Comic Sans",Font.BOLD, 15));
        textDisplay.setEditable(false);
        textDisplay.setBackground(new Color(196, 223, 230));

        frame.add(textDisplay, BorderLayout.CENTER);

//        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        window.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void showMap() {
        frame = new JFrame("Map");
        frame.setSize(500, 500);

        textDisplay = new JTextArea();


        frame.add(textDisplay, BorderLayout.CENTER);

//        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        window.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void splashWindow() throws IOException {
        // Read the ascii file line by line and build it into a string
        FileInputStream asciiSplashScreen = new FileInputStream("resources/splashScreen");
        DataInputStream in = new DataInputStream(asciiSplashScreen);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String strLine;
//        JLabel ascii = new JLabel("");
        while ((strLine = br.readLine()) != null)   {
            // Print the content on the console
            System.out.println (strLine);
            sb.append(strLine);
//            JLabel ascii = new JLabel(String.valueOf(sb));
//            JPanel panel = new JPanel();
//            ascii.setFont(new Font("Monospaced", Font.PLAIN, 12));
////        ascii.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
//            ascii.setSize(1000, 1000);
//            panel.add(ascii);
//            window.getContentPane().add(panel);
        }
        in.close();

        JLabel ascii = new JLabel(String.valueOf(sb));
        ascii.setFont(new Font("Monospaced", Font.PLAIN, 12));
//        ascii.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        ascii.setSize(100, 100);
        window.getContentPane().add(ascii);
        window.setBounds(500, 150, 300, 200);

        window.pack();
        window.setSize(window.getWidth() * 30, window.getHeight() * 30);
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
}

