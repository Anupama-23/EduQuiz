import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LeaderboardScreen extends JPanel {
    private JPanel listPanel;
    private LeaderboardManager manager;
    private CardLayout layout;
    private JPanel parentPanel;

    public LeaderboardScreen(CardLayout layout, JPanel parentPanel, LeaderboardManager manager) {
        setName("leaderboard");
        this.layout = layout;
        this.parentPanel = parentPanel;
        this.manager = manager;

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // 🔹 Top panel with back arrow and title
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton back = createBackButton("result");
        topPanel.add(back, BorderLayout.WEST);

        JLabel title = new JLabel("🏆 Leaderboard", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // 🔹 Scrollable list panel
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
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

    public void refresh() {
    listPanel.removeAll();
    List<LeaderboardEntry> entries = manager.getTopEntries(1); // Only fetch top 1

    if (entries.isEmpty()) {
        JLabel emptyLabel = new JLabel("No scores yet. Be the first to shine!", SwingConstants.CENTER);
        emptyLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
        listPanel.add(emptyLabel);
    } else {
        LeaderboardEntry top = entries.get(0);
        JPanel topBox = new JPanel();
        topBox.setLayout(new BoxLayout(topBox, BoxLayout.Y_AXIS));
        topBox.setBackground(new Color(255, 250, 205)); // light gold
        topBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 165, 32), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel topLabel = new JLabel("🥇 Top Scorer");
        topLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        topLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel("Name: " + top.getName());
        JLabel scoreLabel = new JLabel("Score: " + top.getScore() + "/" + top.getTotal());
        JLabel timeLabel = new JLabel("Time: " + top.getTimestamp());

        for (JLabel label : new JLabel[]{nameLabel, scoreLabel, timeLabel}) {
            label.setFont(new Font("SansSerif", Font.PLAIN, 16));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        topBox.add(topLabel);
        topBox.add(Box.createVerticalStrut(8));
        topBox.add(nameLabel);
        topBox.add(scoreLabel);
        topBox.add(timeLabel);
        topBox.add(Box.createVerticalStrut(10));

        listPanel.add(topBox);
    }

    revalidate();
    repaint();
}
}