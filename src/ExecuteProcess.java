import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class ExecuteProcess {
    private int selectedGameIndex;
    private Process process;

    public ExecuteProcess(int selectedGameIndex, Process process) {
        this.selectedGameIndex = selectedGameIndex;
        this.process = process;
    }

    public void runProcess() throws IOException, InterruptedException {
        //Get the directory of the selected game
        File selectedDirectory = new File(DefaultPage.dataList.get(selectedGameIndex).getGamePosition()
                .substring(0, DefaultPage.dataList.get(selectedGameIndex).getGamePosition().lastIndexOf("\\")));
        process = Runtime.getRuntime().exec(DefaultPage.dataList.get(selectedGameIndex).getGamePosition(),
                null, selectedDirectory);
        long startTime = System.currentTimeMillis();
        long endTime = startTime;
        boolean shut = process.isAlive();
        while (shut) {
            shut = process.isAlive();
            Thread.sleep(10000);
            if (!shut) {
                endTime = System.currentTimeMillis();
                long playTimeMs = endTime - startTime;
                long totalPlatTimeMs = playTimeMs + DefaultPage.dataList.get(selectedGameIndex).getPlayTime();
                modifyGameList(selectedGameIndex, 2, String.valueOf(totalPlatTimeMs));
            }
        }
        //TODO: Update the JList after the process is closed, But How?
//        DefaultPage.content.clear();
//        DefaultPage.gameList.removeAll();
//        DefaultPage.showTheList();
        DefaultPage.gameList.updateUI();
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
        //Copy the dataList to a new ArrayList
        ArrayList<GameData> newDataList = new ArrayList<>(DefaultPage.dataList);
        //Sort the dataList
        newDataList.sort(Comparator.comparingInt(GameData::getOrder));
        //写入文件
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(DefaultPage.filePath));
            for (GameData gameData : newDataList) {
                writer.write(gameData.getOrder() + "=" + gameData.getGamePosition() + "=" +
                        gameData.getPlayTime() + "=" + gameData.getStatus() + "\n");
            }
            writer.close();
            System.out.println("文件已写入");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
