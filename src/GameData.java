public class GameData {
    private String gamePosition;
    private int playTime;
    private char status;

    public GameData() {
    }

    public GameData(String gameName, int playTime, char status) {
        this.gamePosition = gameName;
        this.playTime = playTime;
        this.status = status;
    }

    public String getGamePosition() {
        return gamePosition;
    }

    public void setGamePosition(String gamePosition) {
        this.gamePosition = gamePosition;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public String toString() {
        return "GameData{gameName = " + gamePosition + ", playTime = " + playTime + ", status = " + status + "}";
    }
}
