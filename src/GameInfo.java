public class GameInfo {
    private int order;
    private String gamePosition;
    private long playTime;
    private char status;
    private String imagePosition;

    public GameInfo() {
    }

    public GameInfo(int order, String gamePosition, long playTime, char status, String imagePosition) {
        this.order = order;
        this.gamePosition = gamePosition;
        this.playTime = playTime;
        this.status = status;
        this.imagePosition = imagePosition;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getGamePosition() {
        return gamePosition;
    }

    public void setGamePosition(String gamePosition) {
        this.gamePosition = gamePosition;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public String getImagePosition() {
        return imagePosition;
    }

    public void setImagePosition(String imagePosition) {
        this.imagePosition = imagePosition;
    }

    public String toString() {
        return "GameInfo{order = " + order + ", gamePosition = " + gamePosition + ", playTime = " + playTime + ", status = " + status + ", imagePosition = " + imagePosition + "}";
    }
}
