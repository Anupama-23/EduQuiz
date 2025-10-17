import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizApp {
    private JFrame frame;
    private JPanel parentPanel;
    private CardLayout layout;

    private LoginScreen loginScreen;
    private SignupScreen signupScreen;
    private ForgotPasswordScreen forgotScreen;
    private HomeScreen homeScreen;
    private QuizScreen quizScreen;
    private ResultScreen resultScreen;
    private ReviewScreen reviewScreen;
    private LeaderboardScreen leaderboardScreen;

    private LeaderboardManager leaderboardManager = new LeaderboardManager();
    private Map<String, String> userDatabase = new HashMap<>();
    private Quiz quiz = new Quiz();
    private User currentUser;

    public QuizApp() {
        frame = new JFrame("EduQuiz");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 650);
        frame.setLocationRelativeTo(null);

        layout = new CardLayout();
        parentPanel = new JPanel(layout);

        loadUserDatabase();

        loginScreen = new LoginScreen(layout, parentPanel, userDatabase, this);
        signupScreen = new SignupScreen(layout, parentPanel, userDatabase);
        forgotScreen = new ForgotPasswordScreen(layout, parentPanel, userDatabase);
        homeScreen = new HomeScreen(new String[]{"Physics", "Maths", "Biology", "Chemistry"}, this::startQuiz);
        quizScreen = new QuizScreen(layout, parentPanel);
        resultScreen = new ResultScreen(layout, parentPanel);
        reviewScreen = new ReviewScreen(layout, parentPanel);
        leaderboardScreen = new LeaderboardScreen(layout, parentPanel, leaderboardManager);

        parentPanel.add(loginScreen, "login");
        parentPanel.add(signupScreen, "signup");
        parentPanel.add(forgotScreen, "forgot");
        parentPanel.add(homeScreen, "home");
        parentPanel.add(quizScreen, "quiz");
        parentPanel.add(resultScreen, "result");
        parentPanel.add(reviewScreen, "review");
        parentPanel.add(leaderboardScreen, "leaderboard");

        quizScreen.setOnQuizComplete(() -> {
            int score = calculateScore();
            leaderboardManager.addEntry(currentUser.getUsername(), score, quiz.totalQuestions());
            leaderboardScreen.refresh();
            resultScreen.showResult(currentUser, quiz);
            layout.show(parentPanel, "result");
        });

        frame.add(parentPanel);
        layout.show(parentPanel, "login");
        frame.setVisible(true);
    }

    private void loadUserDatabase() {
        File file = new File("data/users.csv");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    userDatabase.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    public void startHome(String username) {
        currentUser = new User(username, userDatabase.get(username));
        layout.show(parentPanel, "home");
    }

    private void startQuiz(String subject) {
        try {
            String fileName = "data/questions/" + subject.trim().toLowerCase() + ".csv";
            quiz.loadFromCSV(fileName);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to load quiz: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        quizScreen.startQuiz(quiz, currentUser);
        layout.show(parentPanel, "quiz");
    }

    private int calculateScore() {
        int score = 0;
        int[] answers = currentUser.getAnswers();
        List<Question> questions = quiz.getQuestions();

        for (int i = 0; i < questions.size(); i++) {
            int correct = questions.get(i).getCorrectIndex();
            int selected = (i < answers.length) ? answers[i] : -1;
            if (selected == correct) score++;
        }

        reviewScreen.showReview(currentUser, quiz);
        return score;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizApp::new);
    }
}