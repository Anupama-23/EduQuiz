import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReviewScreen extends JPanel {
    private JPanel reviewPanel;
    private JScrollPane scrollPane;

    private CardLayout layout;
    private JPanel parentPanel;

    public ReviewScreen(CardLayout layout, JPanel parentPanel) {
        setName("review");
        this.layout = layout;
        this.parentPanel = parentPanel;

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // 🔹 Top panel with back arrow and title
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton back = createBackButton("result");
        topPanel.add(back, BorderLayout.WEST);

        JLabel title = new JLabel("📋 Your Responses", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // 🔹 Scrollable review panel
        reviewPanel = new JPanel();
        reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(reviewPanel);
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

    public void showReview(User user, Quiz quiz) {
        reviewPanel.removeAll();
        int[] answers = user.getAnswers();
        List<Question> questions = quiz.getQuestions();

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            int correct = q.getCorrectIndex();
            int selected = (i < answers.length) ? answers[i] : -1;

            JPanel questionBlock = new JPanel();
            questionBlock.setLayout(new BoxLayout(questionBlock, BoxLayout.Y_AXIS));
            questionBlock.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            questionBlock.setBackground(new Color(245, 245, 245));

            JLabel qLabel = new JLabel("Q" + (i + 1) + ": " + q.getText());
            qLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
            questionBlock.add(qLabel);
            questionBlock.add(Box.createVerticalStrut(5));

            List<String> options = q.getOptions();
            for (int j = 0; j < options.size(); j++) {
                String prefix = (j == selected) ? "Your Answer → " : "";
                String suffix = (j == correct && j != selected) ? " (Correct Answer)" : "";
                JLabel optLabel = new JLabel("  • " + prefix + options.get(j) + suffix);
                optLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

                if (j == selected && j != correct) {
                    optLabel.setForeground(Color.RED); // wrong answer
                } else if (j == correct) {
                    optLabel.setForeground(new Color(0, 128, 0)); // correct answer
                }

                questionBlock.add(optLabel);
            }

            reviewPanel.add(questionBlock);
            reviewPanel.add(Box.createVerticalStrut(10));
        }

        revalidate();
        repaint();
    }
}