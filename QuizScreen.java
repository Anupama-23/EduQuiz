import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class QuizScreen extends JPanel {
    private JPanel questionPanel;
    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionGroup;
    private JButton nextButton, prevButton, submitButton;
    private JLabel timerLabel;

    private CardLayout layout;
    private JPanel parentPanel;
    private Runnable onQuizComplete;

    private Quiz quiz;
    private User user;
    private int currentIndex = 0;
    private Timer quizTimer;
    private int timeLeft = 180;
    private boolean warningShown = false;

    public QuizScreen(CardLayout layout, JPanel parentPanel) {
        setName("quiz");
        this.layout = layout;
        this.parentPanel = parentPanel;

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Top panel with back button and timer
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.addActionListener(e -> {
            if (quizTimer != null) quizTimer.stop();
            layout.show(parentPanel, "home");
        });

        timerLabel = new JLabel("Time Left: 180s", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(timerLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Question panel
        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setBackground(new Color(245, 245, 245));
        questionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        questionLabel = new JLabel("Question will appear here");
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        questionPanel.add(questionLabel);
        questionPanel.add(Box.createVerticalStrut(10));

        optionButtons = new JRadioButton[4];
        optionGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("SansSerif", Font.PLAIN, 14));
            optionButtons[i].setBackground(new Color(245, 245, 245));
            optionGroup.add(optionButtons[i]);
            questionPanel.add(optionButtons[i]);
            questionPanel.add(Box.createVerticalStrut(5));
        }

        add(questionPanel, BorderLayout.CENTER);

        // Navigation buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        prevButton = new JButton("Prev");
        nextButton = new JButton("Next");
        submitButton = new JButton("Submit");

        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(submitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        prevButton.addActionListener(e -> goToPrevious());
        nextButton.addActionListener(e -> goToNext());
        submitButton.addActionListener(e -> submitQuiz());
    }

    public void setOnQuizComplete(Runnable callback) {
        this.onQuizComplete = callback;
    }

    public void startQuiz(Quiz quiz, User user) {
        this.quiz = quiz;
        this.user = user;
        this.currentIndex = 0;
        this.warningShown = false;
        user.resetAnswers();
        showQuestion();
        startTimer();
    }

    private void showQuestion() {
        if (currentIndex >= quiz.totalQuestions()) {
            submitQuiz();
            return;
        }

        Question q = quiz.getQuestions().get(currentIndex);
        questionLabel.setText("Q" + (currentIndex + 1) + ": " + q.getText());

        List<String> options = q.getOptions();
        optionGroup.clearSelection();
        for (int i = 0; i < optionButtons.length; i++) {
            if (i < options.size()) {
                optionButtons[i].setText(options.get(i));
                optionButtons[i].setVisible(true);
                optionButtons[i].setSelected(user.getAnswers()[currentIndex] == i);
            } else {
                optionButtons[i].setVisible(false);
            }
        }

        prevButton.setEnabled(currentIndex > 0);
        nextButton.setEnabled(currentIndex < quiz.totalQuestions() - 1);
    }

    private void goToNext() {
        saveAnswer();
        currentIndex++;
        showQuestion();
    }

    private void goToPrevious() {
        saveAnswer();
        currentIndex--;
        showQuestion();
    }

    private void saveAnswer() {
        int selectedIndex = -1;
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) {
                selectedIndex = i;
                break;
            }
        }
        user.setAnswer(currentIndex, selectedIndex);
    }

    private void startTimer() {
        quizTimer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time Left: " + timeLeft + "s");

            if (timeLeft == 10 && !warningShown) {
                warningShown = true;
                JOptionPane.showMessageDialog(QuizScreen.this,
                        "⏳ Only 10 seconds left! Submitting soon...",
                        "Time Warning", JOptionPane.WARNING_MESSAGE);
            }

            if (timeLeft <= 0) {
                submitQuiz();
            }
        });
        quizTimer.start();
    }

    private void submitQuiz() {
        saveAnswer();
        if (quizTimer != null) quizTimer.stop();
        if (onQuizComplete != null) onQuizComplete.run();
    }
}