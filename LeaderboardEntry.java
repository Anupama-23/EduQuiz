public class LeaderboardEntry {
    private String name;
    private int score;
    private int total;
    private String timestamp;

    public LeaderboardEntry(String name, int score, int total, String timestamp) {
        this.name = name;
        this.score = score;
        this.total = total;
        this.timestamp = timestamp;
    }

    public String getName() { return name; }
    public int getScore() { return score; }
    public int getTotal() { return total; }
    public String getTimestamp() { return timestamp; }
}