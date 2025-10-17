import java.util.Arrays;

public class User {
    private String username;
    private String password;
    private int[] answers;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.answers = new int[20]; // assuming 20 questions
        Arrays.fill(answers, -1);   // -1 means unanswered
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public void setAnswer(int index, int value) {
        if (index >= 0 && index < answers.length) {
            answers[index] = value;
        }
    }

    public int[] getAnswers() {
        return answers;
    }

    public void resetAnswers() {
        Arrays.fill(answers, -1);
    }
}