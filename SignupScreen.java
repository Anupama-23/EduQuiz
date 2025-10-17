import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Map;

public class SignupScreen extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signupButton;

    private CardLayout layout;
    private JPanel parentPanel;
    private Map<String, String> userDatabase;

    public SignupScreen(CardLayout layout, JPanel parentPanel, Map<String, String> userDatabase) {
        setName("signup");
        this.layout = layout;
        this.parentPanel = parentPanel;
        this.userDatabase = userDatabase;

        setLayout(new BorderLayout());

        // 🔹 Top panel with back arrow
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton back = createBackButton("login");
        topPanel.add(back, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        // 🔹 Center panel with form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Create Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Create Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        signupButton = new JButton("Signup");
        formPanel.add(signupButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        signupButton.addActionListener(e -> createAccount());
    }

    private JButton createBackButton(String targetCard) {
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.addActionListener(e -> layout.show(parentPanel, targetCard));
        return backButton;
    }

    private void createAccount() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields cannot be empty.");
            return;
        }

        if (userDatabase.containsKey(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists.");
            return;
        }

        userDatabase.put(username, password);
        saveUserToCSV(username, password);
        JOptionPane.showMessageDialog(this, "Account created! Please login.");
        layout.show(parentPanel, "login");
    }

    private void saveUserToCSV(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/users.csv", true))) {
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving user.");
        }
    }
}