import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Map;

public class ForgotPasswordScreen extends JPanel {
    private JTextField usernameField;
    private JPasswordField newPasswordField;
    private JButton resetButton;

    private CardLayout layout;
    private JPanel parentPanel;
    private Map<String, String> userDatabase;

    public ForgotPasswordScreen(CardLayout layout, JPanel parentPanel, Map<String, String> userDatabase) {
        setName("forgot");
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
        formPanel.add(new JLabel("Enter Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        newPasswordField = new JPasswordField(15);
        formPanel.add(newPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        resetButton = new JButton("Reset Password");
        formPanel.add(resetButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        resetButton.addActionListener(e -> resetPassword());
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

    private void resetPassword() {
        String username = usernameField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword()).trim();

        if (!userDatabase.containsKey(username)) {
            JOptionPane.showMessageDialog(this, "Username not found.");
            return;
        }

        userDatabase.put(username, newPassword);
        updateCSV(username, newPassword);
        JOptionPane.showMessageDialog(this, "Password updated. Please login.");
        layout.show(parentPanel, "login");
    }

    private void updateCSV(String username, String newPassword) {
        try {
            File inputFile = new File("data/users.csv");
            File tempFile = new File("data/users_temp.csv");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    writer.write(username + "," + newPassword);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }

            reader.close();
            writer.close();

            inputFile.delete();
            tempFile.renameTo(inputFile);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating password.");
        }
    }
}