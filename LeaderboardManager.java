import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class LeaderboardManager {
    private final String filePath = "data/leaderboard.csv";

    public void addEntry(String name, int score, int total) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            String timestamp = new SimpleDateFormat("MMM_dd_hh:mm_a").format(new Date());
            writer.write(name + "," + score + "," + total + "," + timestamp);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing leaderboard: " + e.getMessage());
        }
    }

    public List<LeaderboardEntry> getTopEntries(int limit) {
        List<LeaderboardEntry> entries = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return entries;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    int total = Integer.parseInt(parts[2]);
                    String timestamp = parts[3];
                    entries.add(new LeaderboardEntry(name, score, total, timestamp));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading leaderboard: " + e.getMessage());
        }

        entries.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        return entries.subList(0, Math.min(limit, entries.size()));
    }
}