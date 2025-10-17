import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResultScreen extends JPanel {
    private JLabel scoreLabel;
    private JLabel messageLabel;
    private JTextArea feedbackArea;
    private JButton viewResponseButton;
    private JButton leaderboardButton;
    private JButton retakeButton;

    private CardLayout layout;
    private JPanel parentPanel;

    public ResultScreen(CardLayout layout, JPanel parentPanel) {
        setName("result");
        this.layout = layout;
        this.parentPanel = parentPanel;

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // 🔹 Top panel with back arrow, score, and message
        JPanel topPanel = new JPanel(new BorderLayout());

        JButton back = createBackButton("home");
        topPanel.add(back, BorderLayout.WEST);

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));

        scoreLabel = new JLabel("Your Score: ", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scorePanel.add(scoreLabel);
        scorePanel.add(Box.createVerticalStrut(10));
        scorePanel.add(messageLabel);

        topPanel.add(scorePanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // 🔹 Center panel with feedback box
        JPanel centerPanel = new JPanel(new BorderLayout());
        JLabel feedbackLabel = new JLabel("Your Feedback:");
        feedbackLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        feedbackArea = new JTextArea(4, 50);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(feedbackArea);

        centerPanel.add(feedbackLabel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JButton submitFeedbackButton = new JButton("Submit Feedback");
        submitFeedbackButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
        centerPanel.add(submitFeedbackButton, BorderLayout.SOUTH);

        submitFeedbackButton.addActionListener(e -> {
            String feedback = feedbackArea.getText().trim();
            if (!feedback.isEmpty()) {
                feedbackArea.setText("");
                JOptionPane.showMessageDialog(ResultScreen.this,
                        "Thanks for your valuable feedback!",
                        "Feedback Submitted", JOptionPane.INFORMATION_MESSAGE);
                // Optional: save feedback to file
            }
        });

        add(centerPanel, BorderLayout.CENTER);

        // 🔹 Bottom panel with buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        viewResponseButton = new JButton("View Response");
        leaderboardButton = new JButton("Leaderboard");
        retakeButton = new JButton("Retake Quiz");

        buttonPanel.add(viewResponseButton);
        buttonPanel.add(leaderboardButton);
        buttonPanel.add(retakeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        viewResponseButton.addActionListener(e -> layout.show(parentPanel, "review"));
        leaderboardButton.addActionListener(e -> layout.show(parentPanel, "leaderboard"));
        retakeButton.addActionListener(e -> layout.show(parentPanel, "home"));
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

    public void showResult(User user, Quiz quiz) {
        int score = 0;
        int[] answers = user.getAnswers();
        List<Question> questions = quiz.getQuestions();
        boolean allSkipped = true;

        for (int i = 0; i < questions.size(); i++) {
            int correct = questions.get(i).getCorrectIndex();
            int selected = (i < answers.length) ? answers[i] : -1;
            if (selected == correct) score++;
            if (selected != -1) allSkipped = false;
        }

        scoreLabel.setText("Your Score: " + score + " out of 20");

        if (score == 20) {
            messageLabel.setText("🎯 Perfect score! You're a quiz legend!");
        } else if (allSkipped) {
            messageLabel.setText("😅 You skipped everything... Were you meditating instead of answering?");
        } else {
            messageLabel.setText("💪 Good effort! Keep practicing and you'll get there!");
        }
    }

    public String getUserFeedback() {
        return feedbackArea.getText().trim();
    }
}