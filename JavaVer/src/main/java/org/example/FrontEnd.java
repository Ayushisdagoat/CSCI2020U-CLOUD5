package org.example;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;

public class FrontEnd {

    private JFrame frame;
    private int[] gamesList = {730, 570, 440, 578080, 1091500, 1172470, 271590, 252490, 413150, 550, 620980};
    private JPanel gamesPanel;
    private String currentUser; // stores logged in username

    public FrontEnd(){
        createLoginScreen();
    }

    private void createLoginScreen() {
        frame = new JFrame("CLOUD5 Game Store");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("CLOUD5", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 48));
        frame.add(title, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new GridBagLayout());

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(5, 1, 10, 10));
        loginPanel.setPreferredSize(new Dimension(300, 200));

        JLabel loginLabel = new JLabel("Login", SwingConstants.CENTER);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginPanel.add(loginLabel);
        loginPanel.add(new JLabel("Username"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password"));
        loginPanel.add(passwordField);

        centerPanel.add(loginPanel);
        frame.add(centerPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(loginButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            AuthService auth = new AuthService();
            String role = auth.login(username, password);

            if (role.equals("admin")) {
                currentUser = username; // save username
                openCatalog(true);
            } else if (role.equals("user")) {
                currentUser = username; // save username
                openCatalog(false);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Username or Password");
            }
        });

        frame.setVisible(true);
    }

    private void openCatalog(boolean isAdmin) {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(18, 18, 18));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel logo = new JLabel("CLOUD5");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 28));
        header.add(logo, BorderLayout.WEST);

        JTextField searchbar = new JTextField();
        searchbar.setPreferredSize(new Dimension(500, 40));
        searchbar.setFont(new Font("Arial", Font.PLAIN, 28));
        searchbar.setBackground(new Color(40, 40, 40));
        searchbar.setForeground(Color.WHITE);
        searchbar.setCaretColor(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(18, 18, 18));
        searchPanel.add(searchbar);
        header.add(searchPanel, BorderLayout.CENTER);

        searchbar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterGames(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterGames(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterGames(); }

            private void filterGames() {
                String text = searchbar.getText().toLowerCase().trim();
                for (Component card : gamesPanel.getComponents()) {
                    if (card instanceof JPanel) {
                        boolean match = containsText((JPanel) card, text);
                        card.setVisible(text.isEmpty() || match);
                    }
                }
                gamesPanel.revalidate();
                gamesPanel.repaint();
            }

            private boolean containsText(JPanel panel, String text) {
                for (Component c : panel.getComponents()) {
                    if (c instanceof JLabel) {
                        String labelText = ((JLabel) c).getText();
                        if (labelText != null && labelText.toLowerCase().contains(text)) {
                            return true;
                        }
                    } else if (c instanceof JPanel) {
                        if (containsText((JPanel) c, text)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        JPanel rightpanel = new JPanel();
        rightpanel.setBackground(new Color(18, 18, 18));

        JButton cartButton = new JButton("Cart");
        cartButton.addActionListener(e -> {
            WishlistService ws = new WishlistService();
            String cartContents = ws.getWishlist(currentUser);
            JOptionPane.showMessageDialog(frame, cartContents, "My Cart", JOptionPane.INFORMATION_MESSAGE);
        });
        rightpanel.add(cartButton);

        if (isAdmin) {
            JButton add = new JButton("Add Game");
            JButton remove = new JButton("Remove Game");

            add.addActionListener(e -> {
                JTextField titleField = new JTextField();
                JTextField genreField = new JTextField();
                JTextField platformField = new JTextField();
                JTextField priceField = new JTextField();
                JTextField descField = new JTextField();
                JTextField trailerField = new JTextField();

                Object[] fields = {
                        "Title:", titleField,
                        "Genre:", genreField,
                        "Platform:", platformField,
                        "Price:", priceField,
                        "Description:", descField,
                        "Trailer URL:", trailerField
                };

                int result = JOptionPane.showConfirmDialog(frame, fields, "Add Game", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    GameService gs = new GameService();
                    boolean success = gs.addGame(
                            titleField.getText(),
                            genreField.getText(),
                            platformField.getText(),
                            Double.parseDouble(priceField.getText()),
                            descField.getText(),
                            trailerField.getText()
                    );
                    if (success) {
                        JOptionPane.showMessageDialog(frame, "Game added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to add game.");
                    }
                }
            });

            remove.addActionListener(e -> {
                String input = JOptionPane.showInputDialog(frame, "Enter the ID of the game to remove:");
                if (input != null) {
                    GameService gs = new GameService();
                    boolean success = gs.removeGame(Integer.parseInt(input));
                    if (success) {
                        JOptionPane.showMessageDialog(frame, "Game removed successfully!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to remove game.");
                    }
                }
            });

            rightpanel.add(add);
            rightpanel.add(remove);
        }

        header.add(rightpanel, BorderLayout.EAST);
        frame.add(header, BorderLayout.NORTH);

        gamesPanel = new JPanel();
        gamesPanel.setBackground(new Color(25, 25, 25));
        frame.getContentPane().setBackground(new Color(25, 25, 25));
        int rows = (int) Math.ceil(gamesList.length / 3.0);
        gamesPanel.setLayout(new GridLayout(rows, 3, 25, 25));
        gamesPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        for (int appID : gamesList) {
            try {
                String urlString = "https://store.steampowered.com/api/appdetails?appids=" + appID;
                URL url = new URL(urlString);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();

                JSONObject json = new JSONObject(result.toString());
                JSONObject game = json.getJSONObject(String.valueOf(appID)).getJSONObject("data");

                String name = game.getString("name");
                String description = game.getString("short_description");
                String imageURL = game.getString("header_image");

                String platform = "WEB";
                JSONObject platforms = game.getJSONObject("platforms");
                if (platforms.getBoolean("windows")) platform = "Windows";
                else if (platforms.getBoolean("mac")) platform = "Mac";
                else if (platforms.getBoolean("linux")) platform = "Linux";

                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(new Color(35, 35, 35));
                card.setPreferredSize(new Dimension(460, 330));
                card.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                JLabel image = new JLabel(new ImageIcon(new URL(imageURL)));
                card.add(image, BorderLayout.NORTH);

                JLabel title = new JLabel(name);
                title.setFont(new Font("Arial", Font.BOLD, 16));
                title.setForeground(Color.WHITE);

                JLabel platformTag = new JLabel(platform);
                platformTag.setOpaque(true);
                platformTag.setBackground(new Color(80, 80, 80));
                platformTag.setForeground(Color.WHITE);
                platformTag.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));

                JLabel hashtags = new JLabel("#Genre #Genre");
                hashtags.setForeground(new Color(255, 120, 120));

                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setBackground(new Color(35, 35, 35));
                infoPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

                JTextArea desc = new JTextArea(description);
                desc.setLineWrap(true);
                desc.setWrapStyleWord(true);
                desc.setEditable(false);
                desc.setForeground(Color.LIGHT_GRAY);
                desc.setBackground(new Color(35, 35, 35));
                desc.setRows(3);

                JPanel titleRow = new JPanel(new BorderLayout());
                titleRow.setBackground(new Color(35, 35, 35));
                titleRow.add(title, BorderLayout.WEST);
                titleRow.add(platformTag, BorderLayout.EAST);

                infoPanel.add(titleRow);
                infoPanel.add(hashtags);
                infoPanel.add(desc);
                card.add(infoPanel, BorderLayout.CENTER);

                card.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        openGame(appID, name, isAdmin);
                    }
                });

                gamesPanel.add(card);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JScrollPane scrollPane = new JScrollPane(gamesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel bottompanel = new JPanel();
        JButton logout = new JButton("Logout");
        bottompanel.add(logout);
        frame.add(bottompanel, BorderLayout.SOUTH);

        logout.addActionListener(e -> {
            frame.dispose();
            new FrontEnd();
        });

        frame.revalidate();
        frame.repaint();
    }

    private void openGame(int appID, String gameName, Boolean isAdmin) {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        try {
            String urlString = "https://store.steampowered.com/api/appdetails?appids=" + appID;
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();

            JSONObject json = new JSONObject(result.toString());
            JSONObject game = json.getJSONObject(String.valueOf(appID)).getJSONObject("data");
            String description = game.getString("short_description");
            String imageURL = game.getString("header_image");

            ImageIcon icon = new ImageIcon(new URL(imageURL));
            Image scaled = icon.getImage().getScaledInstance(460, 215, Image.SCALE_SMOOTH);
            JLabel image = new JLabel(new ImageIcon(scaled));
            image.setPreferredSize(new Dimension(460, 215));
            image.setMaximumSize(new Dimension(Integer.MAX_VALUE, 215));
            image.setAlignmentX(Component.LEFT_ALIGNMENT);

            JTextArea text = new JTextArea(description);
            text.setLineWrap(true);
            text.setWrapStyleWord(true);
            text.setEditable(false);
            frame.add(image, BorderLayout.NORTH);
            frame.add(new JScrollPane(text), BorderLayout.CENTER);

        } catch (Exception e) {
            e.printStackTrace();
        }

        JButton back = new JButton("Back");
        back.addActionListener(e -> openCatalog(isAdmin));

        JPanel bot = new JPanel();
        bot.add(back);

        if (!isAdmin) {
            JButton addToCart = new JButton("Add to Cart");
            addToCart.addActionListener(e -> {
                WishlistService ws = new WishlistService();
                boolean success = ws.addToWishlist(currentUser, appID, gameName);
                if (success) {
                    JOptionPane.showMessageDialog(frame, gameName + " added to cart!");
                } else {
                    JOptionPane.showMessageDialog(frame, gameName + " is already in your cart!");
                }
            });
            bot.add(addToCart);
        }

        frame.add(bot, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) {
        AuthService.initDB();
        GameService.initDB();
        ReviewService.initDB();
        WishlistService.initDB();
        SwingUtilities.invokeLater(FrontEnd::new);
    }
}