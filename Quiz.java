import java.io.*;
import java.util.*;

public class Quiz {
    private List<Question> questions = new ArrayList<>();

    public void loadFromCSV(String filePath) throws IOException {
        questions.clear();
        File file = new File(filePath);

        if (!file.exists()) {
            throw new IOException("File not found: " + filePath);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                String[] parts = line.split(",");
                if (parts.length != 6) {
                    throw new IOException("Line " + lineNumber + " is malformed: " + line);
                }

                String text = parts[0].trim();
                List<String> options = Arrays.asList(
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    parts[4].trim()
                );

                int correctIndex;
                try {
                    correctIndex = Integer.parseInt(parts[5].trim());
                } catch (NumberFormatException e) {
                    throw new IOException("Line " + lineNumber + " has invalid correct index: " + parts[5]);
                }

                if (correctIndex < 0 || correctIndex >= options.size()) {
                    throw new IOException("Line " + lineNumber + " has out-of-range correct index: " + correctIndex);
                }

                questions.add(new Question(text, options, correctIndex));
            }
        }

    }

    public List<Question> getQuestions() {
        return questions;
    }

    public int totalQuestions() {
        return questions.size();
    }
}