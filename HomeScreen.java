import javax.swing.*;
import java.awt.*;

public class HomeScreen extends JPanel {
    private JComboBox<String> subjectBox;
    private JButton startButton;

    public HomeScreen(String[] subjects, SubjectSelectionListener listener) {
        setName("home");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Select Subject", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        add(new JLabel("Subject:"), gbc);
        gbc.gridx = 1;
        subjectBox = new JComboBox<>(subjects);
        add(subjectBox, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        startButton = new JButton("Start Quiz");
        add(startButton, gbc);

        startButton.addActionListener(e -> {
            String selected = (String) subjectBox.getSelectedItem();
            listener.onSubjectSelected(selected);
        });
    }

    public interface SubjectSelectionListener {
        void onSubjectSelected(String subject);
    }
}