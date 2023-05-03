import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.*;

import static java.awt.Color.black;

public class DefaultPage extends JPanel {

    ArrayList<GameData> dataList;
//    List<Map.Entry<String, Double>> newTimeMapList;
    boolean isChose = false;
    String selectedGame = "";

    public DefaultPage() {
        this.setFocusable(true);

        String filePath = "_playedGameList_.txt";
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Created A New File");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String[] data;
        dataList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                data = line.split("=");
                dataList.add(new GameData(data[0], Integer.parseInt(data[1]), data[2].charAt(0)));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFirstPage() {
        //frame items
        JFrame frame = new JFrame();
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));
        frame.setSize(880, 550);
        frame.setLocation(200, 200);
        frame.setTitle("Games Launcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setIconImage(Data.frameIcon.getImage());

        //game list
        showGameList();

        //buttons
        JButton addButton = new JButton("+");
        this.add(addButton);
        addButton.setBackground(new Color(27, 80, 104));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBounds(10, 450, 50, 50);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JButton startButton = new JButton("Start");
        this.add(startButton);
        startButton.setBackground(new Color(27, 80, 104));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBounds(750, 230, 100, 50);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

//        JButton showListButton = new JButton("Show My Game Time List");
//        this.add(showListButton);
//        showListButton.setBackground(new Color(27, 80, 104));
//        showListButton.setForeground(Color.WHITE);
//        showListButton.setFocusPainted(false);
//        showListButton.setBounds(94, 260, 306, 50);
//        showListButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });
        setLayout(null);
    }

    private void showGameList() {
        Vector<Object> content = new Vector<>();
        JList<Object> gameList = new JList<>(content);
        Comparator<GameData> gameDataComparator = (o1, o2) -> Integer.compare(o2.getPlayTime(), o1.getPlayTime());
        dataList.sort(gameDataComparator);
        for (GameData gameData : dataList) {
            String gameName = gameData.getGamePosition().substring(gameData.getGamePosition().lastIndexOf("\\") + 1, gameData.getGamePosition().indexOf(".exe"));
            content.add("<html><table width='250'><tr><td align='left'>" + gameName + "</td>" +
                    "<td align='right'>" +
                    new Formatter().format("%.2f", Double.parseDouble(String.valueOf(gameData.getPlayTime())) / 60000 / 60) +
                    " hours" + "</td></tr></table></html>");
        }
        gameList.setBounds(10, 10, 200, 200);
        gameList.setCellRenderer(new GameListRenderer());
        JScrollPane scrollPane = new JScrollPane(gameList);
        scrollPane.setBounds(10, 38, 300, 400);
        this.add(scrollPane);
    }


    public class GameListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setOpaque(true);
            if (isSelected) {
                label.setBackground(new Color(27, 80, 104)); //  new Color(149, 184, 197)
                label.setForeground(Color.WHITE);
            } else {
                label.setBackground(new Color(121, 156, 173, 179));
                label.setForeground(new Color(46, 61, 72));
            }
            label.setVerticalAlignment(SwingConstants.TOP); // 设置垂直对齐方式为上部对齐
            label.setHorizontalTextPosition(SwingConstants.LEFT); // 设置水平文本位置为左侧
            label.setHorizontalAlignment(SwingConstants.LEFT); // 设置水平对齐方式为左对齐
            label.setPreferredSize(new Dimension(200, label.getPreferredSize().height)); // 固定JLabel的宽度为300，高度不变
            label.setMaximumSize(new Dimension(500, Short.MAX_VALUE)); // 设置JLabel的最大高度为整个单元格的高度

            int leftPadding = 30;//左边距为10个像素
            int rightPadding = -10;//右边距为10个像素
            int topPadding = 0;
            int bottomPadding = 0;
            label.setBorder(BorderFactory.createCompoundBorder(label.getBorder(),
                    BorderFactory.createEmptyBorder(topPadding, leftPadding, bottomPadding, rightPadding)));
            ArrayList<Integer> deleteIndex = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).getStatus() == '0') {
                    deleteIndex.add(i);
                }
            }
            for (Integer integer : deleteIndex) {
                if (index == integer) {
                    label.setForeground(Color.RED);
                }
            }
            label.setText(value.toString());
            return label;
        }
    }


    public void changeBackgroundColor(Graphics g, Color c) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(c);
    }

    public void changeBackgroundColor(Graphics g, int red, int green, int blue) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(new Color(red, green, blue));
    }

    public void clearBackground(Graphics g, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, width, height);
    }

    public void changeColor(Graphics g, Color c) {
        g.setColor(c);
    }

    public void changeColor(Graphics g, int red, int green, int blue) {
        g.setColor(new Color(red, green, blue));
    }

    public void drawText(Graphics g, int x, int y, String s, int size) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("Arial", Font.BOLD, size));
        g2d.drawString(s, x, y);
    }

    @Override
    public void paintComponent(Graphics g) {
        changeBackgroundColor(g, new Color(182, 206, 202));
        clearBackground(g, 880, 550);
        changeColor(g, new Color(27, 80, 104));
        drawText(g, 10, 30, "Your Game List:", 25);
        drawLine(g, 330, 0, 330, 550);
        drawLine(g, 330, 220, 880, 220);
        drawText(g, 75, 482, "Add A New Game", 15);
        changeColor(g, black);
        changeColor(g, new Color(27, 80, 104));
        drawText(g, 210, 120, selectedGame, 15);
    }

    void drawLine(Graphics g, int x1, int y1, int x2, int y2) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(new Line2D.Double(x1, y1, x2, y2));
    }

    void drawRectangle(Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(new Rectangle2D.Double(x, y, width, height));
    }

    void drawSolidRectangle(Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.fill(new Rectangle2D.Double(x, y, width, height));
    }

    void drawCircle(Graphics g, int x, int y, double radius) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2));
    }

    void drawSolidCircle(Graphics g, int x, int y, double radius) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.fill(new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2));
    }

}
