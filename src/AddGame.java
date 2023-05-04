import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Formatter;

public class AddGame {
    String chosenFilePath;
    public void addNewGame() throws IOException {
        chosenFilePath = fileChooser();
        if (chosenFilePath.equals("nullnull")) {
            return;
        } else if (!chosenFilePath.substring(chosenFilePath.lastIndexOf(".")).equals(".exe")) {
            JOptionPane.showMessageDialog(null, "You Didn't select a .exe file.", "Please Choose Again"
                    , JOptionPane.ERROR_MESSAGE);
            addNewGame();
            return;
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(DefaultPage.filePath, true));
        writer.write(chosenFilePath + "=0=1\n");
        writer.close();
        DefaultPage.gameDataReader();
        Comparator<GameData> gameDataComparator = (o1, o2) -> Integer.compare(o2.getPlayTime(), o1.getPlayTime());
        DefaultPage.dataList.sort(gameDataComparator);
        String gameName = chosenFilePath.substring(chosenFilePath.lastIndexOf("\\") + 1,
                chosenFilePath.indexOf(".exe"));
        DefaultPage.content.add("<html><table width='250'><tr><td align='left'>" + gameName + "</td>" +
                "<td align='right'>" +
                new Formatter().format("%.2f", Double.parseDouble("0"))
                + " hours </td></tr></table></html>");
        DefaultPage.gameList.updateUI();
    }

    public static String fileChooser() {
        Frame frame = new Frame();
        FileDialog fileDialog = new FileDialog(frame, "Please Choose ", FileDialog.LOAD);
        FilenameFilter filter = (dir, name) -> name.endsWith(".exe");
        fileDialog.setFilenameFilter(filter);
        fileDialog.setVisible(true);
        return fileDialog.getDirectory() + fileDialog.getFile();
    }
}
