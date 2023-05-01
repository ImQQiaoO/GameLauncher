import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import static java.awt.Color.black;

public class DefaultPage extends JPanel {

    boolean isChose = false;
    String selectedGame = "";

    public DefaultPage() {
        this.setFocusable(true);
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
        String basePath = ".\\GameInfo";
        String[] list;
        list = new File(basePath).list();
        String contentName;
        String contentTime;
        HashMap<String, Double> playTimeMap = new HashMap<>();
        for (int i = 0; i < Objects.requireNonNull(list).length; i++) {
            contentName = list[i].substring(0,list[i].lastIndexOf("."));
            try {
                contentTime = new String(Files.readAllBytes(Paths.get(".\\GameInfo\\" + list[i])));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            playTimeMap.put(contentName, Double.parseDouble(contentTime));
        }
        List<Map.Entry<String, Double>> newTimeMapList = new ArrayList<>(playTimeMap.entrySet());
        newTimeMapList.sort((o1, o2) -> (o2.getValue().compareTo(o1.getValue())));
        for (int i = 0; i < Objects.requireNonNull(list).length; i++) {
            content.add(newTimeMapList.get(i).getKey() + "    -    " + new Formatter().format("%.2f", Double.parseDouble
                    (String.valueOf(newTimeMapList.get(i).getValue()))/60000/60) + " hours");
        }
        gameList.setBounds(10, 10, 200, 200);
        gameList.setCellRenderer(new GameListRenderer());
        JScrollPane scrollPane = new JScrollPane(gameList);
        scrollPane.setBounds(10, 38, 300, 400);
        this.add(scrollPane);
    }

    public static class GameListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setOpaque(true); // 设置标签为不透明
            if (isSelected) {
                label.setBackground(new Color(149, 184, 197)); // 设置选中项背景颜色
                label.setForeground(new Color(57, 65, 79)); // 设置选中项前景颜色
            } else {
                label.setBackground(new Color(121, 156, 173, 179)); // 设置非选中项背景颜色
                label.setForeground(new Color(46, 61, 72)); // 设置非选中项前景颜色
            }
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
