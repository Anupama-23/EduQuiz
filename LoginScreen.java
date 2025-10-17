import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class LoginScreen extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton, forgotButton;

    private CardLayout layout;
    private JPanel parentPanel;
    private Map<String, String> userDatabase;
    private QuizApp app;

    public LoginScreen(CardLayout layout, JPanel parentPanel, Map<String, String> userDatabase, QuizApp app) {
        setName("login");
        this.layout = layout;
        this.parentPanel = parentPanel;
        this.userDatabase = userDatabase;
        this.app = app;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Login to EduQuiz", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        loginButton = new JButton("Login");
        add(loginButton, gbc);
        gbc.gridx = 1;
        signupButton = new JButton("Signup");
        add(signupButton, gbc);

        gbc.gridx = 0; gbc.gridy++;
        forgotButton = new JButton("Forgot Password?");
        add(forgotButton, gbc);

        loginButton.addActionListener(e -> attemptLogin());
        signupButton.addActionListener(e -> layout.show(parentPanel, "signup"));
        forgotButton.addActionListener(e -> layout.show(parentPanel, "forgot"));
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            app.startHome(username);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}