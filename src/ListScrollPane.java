import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Vector;

public class ListScrollPane {

    static JScrollPane scrollPane;

    private final DefaultPage defaultPage;

    public ListScrollPane(DefaultPage defaultPage) {
        this.defaultPage = defaultPage;
    }

    public void showGameList() {
        DefaultPage.content = new Vector<>();
        DefaultPage.gameList = new JList<>(DefaultPage.content);

        Comparator<GameInfo> gameDataComparator = (o1, o2) -> Long.compare(o2.getPlayTime(), o1.getPlayTime());
        DefaultPage.dataList.sort(gameDataComparator);
        DefaultPage.iconList = new ArrayList<>();
        for (GameInfo gameInfo : DefaultPage.dataList) {
            DefaultPage.iconList.add(FileSystemView.getFileSystemView().getSystemIcon(new File(gameInfo.getGamePosition())));
        }
        for (GameInfo gameInfo : DefaultPage.dataList) {
            String gameName = gameInfo.getGamePosition().substring(gameInfo.getGamePosition().lastIndexOf("\\") + 1, gameInfo.getGamePosition().indexOf(".exe"));
            DefaultPage.content.add("<html><table width='250'><tr><td align='left'>" + gameName + "</td>" +
                    "<td align='right'>" +
                    new Formatter().format("%.2f", Double.parseDouble(String.valueOf(gameInfo.getPlayTime())) / 60000 / 60) +
                    " hours" + "</td></tr></table></html>");
        }
        DefaultPage.gameList.revalidate();
        DefaultPage.gameList.setBounds(10, 10, 200, 200);
        DefaultPage.gameList.setCellRenderer(new DefaultPage.GameListRenderer());
        scrollPane = new JScrollPane(DefaultPage.gameList);
        scrollPane.setBounds(10, 38, 300, 400);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            private final Dimension d = new Dimension();

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
                g.setColor(new Color(121, 156, 173, 179));
                g.fillRect(r.x, r.y, r.width, r.height);
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                JScrollPane sp = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, c);
                g2.setColor(new Color(27, 80, 104));
                g2.fillRoundRect(r.x + 5, r.y, r.width - 10, r.height, 5, 5);
                g2.dispose();
            }
        });
        DefaultPage.gameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultPage.gameList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // 确保只在最后一次选择事件之后调用
                DefaultPage.selectedIndex = DefaultPage.gameList.getSelectedIndex();
                defaultPage.repaint();  // Update the game image
                System.out.println(DefaultPage.dataList.get(DefaultPage.selectedIndex)); // 输出选中的选项
                System.out.println(DefaultPage.selectedIndex);
            }
        });
        defaultPage.add(scrollPane);
    }
}
