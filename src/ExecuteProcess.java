import javax.swing.*;
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
        new Thread(() -> {
            //Get the directory of the selected game
            String gameName = DefaultPage.dataList.get(selectedGameIndex).getGamePosition()
                    .substring(DefaultPage.dataList.get(selectedGameIndex).getGamePosition().lastIndexOf("\\") + 1,
                            DefaultPage.dataList.get(selectedGameIndex).getGamePosition().indexOf(".exe"));
            File selectedDirectory = new File(DefaultPage.dataList.get(selectedGameIndex).getGamePosition()
                    .substring(0, DefaultPage.dataList.get(selectedGameIndex).getGamePosition().lastIndexOf("\\")));
            try {
                process = Runtime.getRuntime().exec(DefaultPage.dataList.get(selectedGameIndex).getGamePosition(),
                        null, selectedDirectory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            long startTime = System.currentTimeMillis();
            long endTime;
            boolean alive = process.isAlive();
            while (alive) {
                alive = process.isAlive();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (!alive) {
                    endTime = System.currentTimeMillis();
                    long playTimeMs = endTime - startTime;
                    long totalPlatTimeMs = playTimeMs + DefaultPage.dataList.get(selectedGameIndex).getPlayTime();
                    try {
                        modifyGameList(selectedGameIndex, 2, String.valueOf(totalPlatTimeMs));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    JOptionPane.showMessageDialog(null,
                            "You have played " + gameName +
                                    " for " + Math.round((double) playTimeMs / 1000 / 60) + " minutes",
                            "Play Happily!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            SwingUtilities.invokeLater(() -> {
                // 在 EDT 线程中执行修改操作
                defaultPage.remove(ListScrollPane.scrollPane);
            });
            ListScrollPane listScrollPane = new ListScrollPane(defaultPage);
            listScrollPane.showGameList();
            System.out.println("Game Over");
        }).start();
    }

    public static synchronized void modifyGameList(int modifyIndex, int modifyItem, String modifyContent)
            throws IOException {

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
