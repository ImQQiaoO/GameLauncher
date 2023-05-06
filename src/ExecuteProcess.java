import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class ExecuteProcess {
    private final int selectedGameIndex;
    private Process process;
    private final DefaultPage defaultPage;


    public ExecuteProcess(int selectedGameIndex, Process process, DefaultPage defaultPage) {
        this.selectedGameIndex = selectedGameIndex;
        this.process = process;
        this.defaultPage = defaultPage;
    }

    public void runProcess() throws IOException, InterruptedException {
        //Get the directory of the selected game
        File selectedDirectory = new File(DefaultPage.dataList.get(selectedGameIndex).getGamePosition()
                .substring(0, DefaultPage.dataList.get(selectedGameIndex).getGamePosition().lastIndexOf("\\")));
        process = Runtime.getRuntime().exec(DefaultPage.dataList.get(selectedGameIndex).getGamePosition(),
                null, selectedDirectory);
        long startTime = System.currentTimeMillis();
        long endTime;
        boolean shut = process.isAlive();
        while (shut) {
            shut = process.isAlive();
            Thread.sleep(5000);
            if (!shut) {
                endTime = System.currentTimeMillis();
                long playTimeMs = endTime - startTime;
                long totalPlatTimeMs = playTimeMs + DefaultPage.dataList.get(selectedGameIndex).getPlayTime();
                modifyGameList(selectedGameIndex, 2, String.valueOf(totalPlatTimeMs));
            }
        }

        defaultPage.remove(ListScrollPane.scrollPane);
        ListScrollPane listScrollPane = new ListScrollPane(defaultPage);
        listScrollPane.showGameList();
//        DefaultPage.gameList.updateUI();
        for (int i = 0; i < DefaultPage.dataList.size(); i++) {/*           */
            System.out.println(DefaultPage.dataList.get(i));
        }
    }

    public static void modifyGameList(int modifyIndex, int modifyItem, String modifyContent) throws IOException {

        switch (modifyItem) {
            case 0 -> DefaultPage.dataList.get(modifyIndex).setOrder(Integer.parseInt(modifyContent));
            case 1 -> DefaultPage.dataList.get(modifyIndex).setGamePosition(modifyContent);
            case 2 -> DefaultPage.dataList.get(modifyIndex).setPlayTime(Long.parseLong(modifyContent));
            case 3 -> DefaultPage.dataList.get(modifyIndex).setStatus(modifyContent.charAt(0));
            default -> throw new IOException("Invalid modifyItem");
        }
        //这是修改游戏时长之后的dataList
        DefaultPage.dataList.sort(Comparator.comparingLong(GameInfo::getPlayTime));
        //Copy the dataList to a new ArrayList
        ArrayList<GameInfo> newDataList = new ArrayList<>(DefaultPage.dataList);
        //Sort the dataList
        newDataList.sort(Comparator.comparingInt(GameInfo::getOrder));
        //写入文件
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(DefaultPage.filePath));
            for (GameInfo gameInfo : newDataList) {
                writer.write(gameInfo.getOrder() + "=" + gameInfo.getGamePosition() + "=" +
                        gameInfo.getPlayTime() + "=" + gameInfo.getStatus() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
