package backend;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.nio.channels.ScatteringByteChannel;

public class FrontEnd {

    private JFrame frame;
    private CatalogService catalogService = new CatalogService();

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

        JTextField searchbar = new JTextField("Search games...");
        frame.add(searchbar, BorderLayout.NORTH);

        JTextArea catalogArea = new JTextArea();
        catalogArea.setEditable(false);

        catalogArea.setText("game 1\n Game 2\n Game 3\n");

        frame.add(new JScrollPane(catalogArea), BorderLayout.CENTER);

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

    public static void main(String[] args){
        SwingUtilities.invokeLater(FrontEnd::new);
    }
}
