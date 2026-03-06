package backend;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.nio.channels.ScatteringByteChannel;

public class FrontEnd {

    private JFrame frame;
    private String[] gamesList = {"Game 1", "Game 2", "Game 3", "Game 4", "Game 5", "Game 6", "Game 7", "Game 8", "Game 9", "Game 10", "Game 11", "Game 12", "Game 13", "Game 14"};

    public FrontEnd(){
        createLoginScreen();
    }

    private void createLoginScreen(){
        frame = new JFrame("CLOUD5 Game Catalog Login");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4,1));

        JLabel label = new JLabel("Select Login", SwingConstants.CENTER);
        JButton adminButton = new JButton("Login as Admin");
        JButton userButton = new JButton("Login as User");

        panel.add(label);
        panel.add(adminButton);
        panel.add(userButton);

        frame.add(panel);
        frame.setVisible(true);

        adminButton.addActionListener(e -> openCatalog(true));
        userButton.addActionListener(e -> openCatalog(false));

    }

    private void openCatalog(boolean isAdmin) {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JTextField searchbar = new JTextField("Search games...");
        frame.add(searchbar, BorderLayout.NORTH);

        JPanel gamesPanel = new JPanel();
        int rows = (int) Math.ceil(gamesList.length / 3.0);
        gamesPanel.setLayout(new GridLayout(rows, 3, 20, 20));

//        DefaultListModel<String> gameModel = new DefaultListModel<>();
        for (String game: gamesList){
            JButton gameButton = new JButton(game);
            gameButton.setFocusPainted(false);
            gameButton.addActionListener(e -> openGame(game, isAdmin));
            gamesPanel.add(gameButton);
        }

//        JList<String> list = new JList<>(gamesList);
//        list.setFont(new Font("Arial", Font.PLAIN, 18));

//        frame.add(new JScrollPane(list), BorderLayout.CENTER);

//        JTextArea catalogArea = new JTextArea();
//        catalogArea.setEditable(false);
//
//        catalogArea.setText("game 1\n Game 2\n Game 3\n");

//        frame.add(new JScrollPane(catalogArea), BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(gamesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel bottompanel = new JPanel();
        JButton logout = new JButton("Logout");
        bottompanel.add(logout);

        if (isAdmin) {
            JButton add = new JButton("Add game");
            JButton remove = new JButton("Remove game");

            bottompanel.add(add);
            bottompanel.add(remove);
        }

        frame.add(bottompanel, BorderLayout.SOUTH);

        logout.addActionListener(e -> {
            frame.dispose();
            new FrontEnd();
        });


        frame.revalidate();
        frame.repaint();
    }

    private void openGame(String gameName, Boolean isAdmin){
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel empty = new JPanel();
        frame.add(empty, BorderLayout.CENTER);

        JButton back = new JButton("Back");
        back.addActionListener(e -> openCatalog(isAdmin));

        JPanel bot = new JPanel();
        bot.add(back);
        frame.add(bot, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(FrontEnd::new);
    }
}
