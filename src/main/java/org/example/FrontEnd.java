package org.example;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;
public class FrontEnd {

    //Main Window
    private JFrame frame;
    //List of Steam ID games
    private int[] gamesList = {730, 570, 440, 578080, 1091500, 1172470, 271590, 252490, 413150, 550, 620980, 2651280};
    private JPanel gamesPanel;
    private String currentUser;
    //Constructor
    public FrontEnd(){
        createLoginScreen();
    }

    private void createLoginScreen() {

        //Creates Login Screen
        frame = new JFrame("CLOUD5 Game Store");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        //Dark Theme
        frame.getContentPane().setBackground(new Color(25,25,25));

        //Logo + Title
        JLabel logo = new JLabel("CLOUD5", SwingConstants.CENTER);
        logo.setFont(new Font("Arial", Font.BOLD, 56));
        logo.setForeground(Color.WHITE);
        logo.setBorder(BorderFactory.createEmptyBorder(60,0,40,0));

        frame.add(logo, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(25,25,25));

        //Login Panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(6,1,10,10));
        loginPanel.setPreferredSize(new Dimension(350,280));
        loginPanel.setBackground(new Color(35,35,35));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel loginLabel = new JLabel("Login", SwingConstants.CENTER);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loginLabel.setForeground(Color.WHITE);

        //Username Input
        JTextField usernameField = new JTextField();
        usernameField.setBackground(new Color(60,60,60));
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(Color.WHITE);

        //Password Input
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBackground(new Color(60,60,60));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);

        //Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(70,130,180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        JLabel userLabel = new JLabel("Username");
        userLabel.setForeground(Color.LIGHT_GRAY);

        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(Color.LIGHT_GRAY);

        //Add everything
        loginPanel.add(loginLabel);
        loginPanel.add(userLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);

        centerPanel.add(loginPanel);

        frame.add(centerPanel, BorderLayout.CENTER);

        //Login logic
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            AuthService auth = new AuthService();
            String role = auth.login(username, password);
            if (role.equals("admin")) {
                currentUser = username;
                openCatalog(true);
            }
            else if (role.equals("user")) {
                currentUser = username;
                openCatalog(false);
            }
            else {
                JOptionPane.showMessageDialog(frame,"Invalid Username or Password");
            }
        });
        frame.setVisible(true);
    }

    //Main catalog page
    private void openCatalog(boolean isAdmin) {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Top Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(18, 18, 18));
        header.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));

        JLabel logo = new JLabel("CLOUD5");

        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 28));
        header.add(logo, BorderLayout.WEST);

        //Search bar
        JTextField searchbar = new JTextField();
        searchbar.setPreferredSize(new Dimension(500, 40));
        searchbar.setFont(new Font("Arial", Font.PLAIN, 28));
        searchbar.setBackground(new Color(40, 40, 40));
        searchbar.setForeground(Color.WHITE);
        searchbar.setCaretColor(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10,10, 10));

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(18, 18, 18));
        searchPanel.add(searchbar);
        header.add(searchPanel, BorderLayout.CENTER);

        //Search filter
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
        //Cart + add remove
        JPanel rightpanel = new JPanel();
        rightpanel.setBackground(new Color(18,18,18));
        //Cart button
        JButton cartButton = new JButton("Cart");
        cartButton.addActionListener(e -> {
            WishlistService ws = new WishlistService();
            String cartContents = ws.getWishlist(currentUser);

            JOptionPane.showMessageDialog(
                    frame,
                    cartContents,
                    "My Cart",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
        rightpanel.add(cartButton);
        //Admin only
        if (isAdmin) {
            JButton add = new JButton("Add Game");
            JButton remove = new JButton("Remove Game");
            //Add game logic
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
            //Remove game logic
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
        //Grid layout for games
        int rows = (int) Math.ceil(gamesList.length / 3.0);
        gamesPanel.setLayout(new GridLayout(rows, 3, 25, 25));
        gamesPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        //Game data from Steam
        for (int appID : gamesList){
            try{
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

                //Platform detection
                String platform = "WEB";
                JSONObject platforms = game.getJSONObject("platforms");
                if (platforms.getBoolean("windows")) platform = "Windows";
                else if (platforms.getBoolean("mac")) platform = "Mac";
                else if (platforms.getBoolean("linux")) platform = "Linux";

                //Card panel
                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(new Color(35, 35, 35));
                card.setPreferredSize(new Dimension(460, 330));
                card.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                //Game image
                JLabel image = new JLabel(new ImageIcon(new URL(imageURL)));
                card.add(image, BorderLayout.NORTH);

                //Title of game
                JLabel title = new JLabel(name);
                title.setFont(new Font("Arial", Font.BOLD, 16));
                title.setForeground(Color.WHITE);

                JLabel platformTag = new JLabel(platform);
                platformTag.setOpaque(true);
                platformTag.setBackground(new Color(80,80,80));
                platformTag.setForeground(Color.WHITE);
                platformTag.setBorder(BorderFactory.createEmptyBorder(2,6,2,6));

                JLabel hashtags = new JLabel("#Genre #Genre");
                hashtags.setForeground(new Color(255,120,120));

                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setBackground(new Color(35, 35, 35));
                infoPanel.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

                JTextArea desc = new JTextArea(description);
                desc.setLineWrap(true);
                desc.setWrapStyleWord(true);
                desc.setEditable(false);
                desc.setForeground(Color.LIGHT_GRAY);
                desc.setBackground(new Color(35,35,35));
                desc.setRows(3);

                JPanel titleRow = new JPanel(new BorderLayout());
                titleRow.setBackground(new Color(35,35,35));
                titleRow.add(title, BorderLayout.WEST);
                titleRow.add(platformTag, BorderLayout.EAST);

                infoPanel.add(titleRow);
                infoPanel.add(hashtags);
                infoPanel.add(desc);
                card.add(infoPanel, BorderLayout.CENTER);

                //Click top open game
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

        //Scroll wheel
        JScrollPane scrollPane = new JScrollPane(gamesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        frame.add(scrollPane, BorderLayout.CENTER);

        //Logout bar plus button
        JPanel bottompanel = new JPanel();
        JButton logout = new JButton("Logout");
        bottompanel.setBackground(new Color(18, 18, 18));
        bottompanel.add(logout);

        frame.add(bottompanel, BorderLayout.SOUTH);

        //Logout action
        logout.addActionListener(e -> {
            frame.dispose();
            new FrontEnd();
        });


        frame.revalidate();
        frame.repaint();
    }

    //Open game
    private void openGame(int appID, String gameName, Boolean isAdmin){
        //Clear frame
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(18, 18, 18));

        try{
            //Steam API using link
            String urlString = "https://store.steampowered.com/api/appdetails?appids=" + appID;
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder result = new StringBuilder();
            String line;

            //Read entire JSON esponse from API
            while((line = reader.readLine()) != null){
                result.append(line);
            }
            reader.close();
            //Convert API response intoJSON
            JSONObject json = new JSONObject(result.toString());
            //Access game data
            JSONObject game = json.getJSONObject(String.valueOf(appID)).getJSONObject("data");
            //Get game info
            String description = game.getString("short_description");
            String name = game.getString("name");
            String imageURL = game.getString("header_image");

            //Game title at top
            JLabel title = new JLabel(name);
            title.setForeground(Color.WHITE);
            title.setFont(new Font("Arial", Font.BOLD, 32));
            title.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

            frame.add(title, BorderLayout.NORTH);

            JPanel mainpanel = new JPanel(new BorderLayout());
            mainpanel.setBackground(new Color(18, 18, 18));
            mainpanel.setBorder(BorderFactory.createEmptyBorder(20,30, 20, 30));
            //Trailer placeholder
            JPanel leftPanel = new JPanel();
            leftPanel.setPreferredSize(new Dimension(900,500));
            leftPanel.setBackground(new Color(10,10,10));
            leftPanel.setBorder(BorderFactory.createLineBorder(new Color(40,40,40)));

            JLabel placeholder = new JLabel("Trailer", SwingConstants.CENTER);
            placeholder.setForeground(Color.GRAY);
            placeholder.setFont(new Font("Arial", Font.PLAIN, 20));

            leftPanel.setLayout(new BorderLayout());
            leftPanel.add(placeholder, BorderLayout.CENTER);

            mainpanel.add(leftPanel, BorderLayout.CENTER);
            //Game info
            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setPreferredSize(new Dimension(400,500));
            rightPanel.setBackground(new Color(30,30,30));
            rightPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            //Game image from steam
            ImageIcon icon = new ImageIcon(new URL(imageURL));
            Image scaled = icon.getImage().getScaledInstance(380,180,Image.SCALE_SMOOTH);

            JLabel image = new JLabel(new ImageIcon(scaled));
            image.setAlignmentX(Component.CENTER_ALIGNMENT);

            JTextArea desc = new JTextArea(description);
            desc.setLineWrap(true);
            desc.setWrapStyleWord(true);
            desc.setEditable(false);
            desc.setForeground(Color.LIGHT_GRAY);
            desc.setBackground(new Color(30,30,30));
            desc.setFont(new Font("Arial", Font.PLAIN, 14));
            desc.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
            JLabel genres = new JLabel("#Genre   #Genre   #Genre");
            genres.setForeground(new Color(255,120,120));
            genres.setFont(new Font("Arial", Font.BOLD, 14));
            rightPanel.add(image);
            rightPanel.add(desc);
            rightPanel.add(genres);

            mainpanel.add(rightPanel, BorderLayout.EAST);
            frame.add(mainpanel, BorderLayout.CENTER);

        }
        catch(Exception e){
            e.printStackTrace();
        }

        JButton back = new JButton("Back");
        back.addActionListener(e -> openCatalog(isAdmin));

        JPanel bot = new JPanel();
        bot.setBackground(new Color(18,18,18));
        bot.add(back);

        //Only users can add games to cart
        if (!isAdmin) {
            JButton addToCart = new JButton("Add to Cart");
            addToCart.addActionListener(e -> {
                WishlistService ws = new WishlistService();
                boolean success = ws.addToWishlist(
                        currentUser,
                        appID,
                        gameName
                );
                if (success) {
                    JOptionPane.showMessageDialog(
                            frame,
                            gameName + " added to cart!"
                    );
                }
                else {
                    JOptionPane.showMessageDialog(
                            frame,
                            gameName + " is already in your cart!"
                    );
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