import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Vector;

//java -cp .\out\production\GameLauncher "-Dfile.encoding=UTF-8" Main
public class DefaultPage extends JPanel {

    Process process;
    DefaultPage defaultPage;
    static JButton startButton;
    static int selectedIndex = -1;
    static Vector<Object> content;
    static JList<Object> gameList;
    static ArrayList<GameInfo> dataList; //The data in this ArrayList is sequential
    static ArrayList<Icon> iconList;
    static final String filePath = "_playedGameList_.txt";

    public DefaultPage() {
        this.setFocusable(true);

        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Created A New File");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gameDataReader();
    }

    public static void gameDataReader() {
        String[] data;
        dataList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                data = line.split("=@#");
                dataList.add(new GameInfo(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[2]), data[3].charAt(0), data[4]));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFirstPage(DefaultPage defaultPage) {

        this.defaultPage = defaultPage;

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


        //buttons
        JButton addButton = new JButton("+");
        this.add(addButton);
        addButton.setBackground(new Color(27, 80, 104));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBounds(10, 450, 50, 50);
        addButton.addActionListener(e -> {
            AddGame addGame = new AddGame();
            try {
                addGame.addNewGame();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        startButton = new JButton("Start");
        this.add(startButton);
        buttonColor();
        startButton.setFocusPainted(false);
        startButton.setBounds(750, 230, 100, 50);
        startButton.addActionListener(e -> {    //Add start button function.
            if (selectedIndex == -1) {  //If no game is selected, do nothing.
                JOptionPane.showMessageDialog(null, "No game selected.", "Error"
                        , JOptionPane.WARNING_MESSAGE);
                return;
            } else if (dataList.get(selectedIndex).getStatus() == '0') { //If the game is not installed, do nothing.
                JOptionPane.showMessageDialog(null, "Game is not installed", "Error"
                        , JOptionPane.WARNING_MESSAGE);
                return;
            }
            ExecuteProcess executeProcess = new ExecuteProcess(selectedIndex, process, defaultPage);
            try {
                executeProcess.runProcess();
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        //game list
        ListScrollPane listScrollPane = new ListScrollPane(defaultPage);
        listScrollPane.showGameList();

        setLayout(null);
    }

    public static void buttonColor() {
        if (selectedIndex == -1) {
            startButton.setBackground(new Color(75, 75, 75));
            startButton.setForeground(Color.WHITE);
        } else {
            if (dataList.get(selectedIndex).getStatus() == '0') {
                startButton.setBackground(new Color(75, 75, 75));
                startButton.setForeground(Color.WHITE);
            } else {
                startButton.setBackground(new Color(27, 80, 104));
                startButton.setForeground(Color.WHITE);
            }
        }
    }


    public static class GameListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setOpaque(true);
            if (index >= 0 && index < iconList.size()) { // 根据索引设置图标
                label.setIcon(iconList.get(index));
            } else {
                label.setIcon(null);
            }
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
            label.setHorizontalTextPosition(SwingConstants.RIGHT); // 将文本设置为右对齐
            label.setIconTextGap(3); // 设置图标和文本之间的间距为3像素

            int leftPadding = 10;//左边距为10个像素
            int rightPadding = -10;//右边距为10个像素
            int topPadding = 0;
            int bottomPadding = 0;

            if (label.getIcon() != null) {
                label.setHorizontalTextPosition(SwingConstants.RIGHT); // 图标在左侧，文本在右侧
                label.setIconTextGap(3); // 设置图标和文本之间的间距为3像素
                label.setBorder(BorderFactory.createCompoundBorder(label.getBorder(),
                        BorderFactory.createEmptyBorder(topPadding, leftPadding, bottomPadding, rightPadding)));
            } else {
                label.setHorizontalTextPosition(SwingConstants.LEFT); // 没有图标时，文本居中
                label.setBorder(BorderFactory.createCompoundBorder(label.getBorder(),
                        BorderFactory.createEmptyBorder(topPadding, 29, bottomPadding, rightPadding)));
            }
            ArrayList<Integer> deleteIndex = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).getStatus() == '0') {
                    deleteIndex.add(i);
                }
            }
            for (Integer integer : deleteIndex) {
                if (index == integer) {
                    label.setForeground(new Color(129, 138, 138));
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

    public void clearBackground(Graphics g, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, width, height);
    }

    public void changeColor(Graphics g, Color c) {
        g.setColor(c);
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
        drawText(g, 10, 30, "My Game List:", 25);
        drawLine(g, 330, 0, 330, 550);
        drawLine(g, 330, 220, 880, 220);
        drawText(g, 75, 482, "Add A New Game", 15);
        // Draw the game Image
        if (selectedIndex >= 0 && !dataList.get(selectedIndex).getImagePosition().equals("null")) {
            try {
                BufferedImage image = ImageIO.read(new File(dataList.get(selectedIndex).getImagePosition()));
                g.drawImage(image, 350, 10, 500, 200, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Show The Game Info
        if (selectedIndex >= 0) {
            String selectedPath = dataList.get(selectedIndex).getGamePosition();
            drawText(g, 350, 245, selectedPath.substring(selectedPath.lastIndexOf("\\") + 1,
                    selectedPath.indexOf(".exe")), 15);
            Double currGameTime = Double.parseDouble(String.valueOf(dataList.get(selectedIndex).getPlayTime())) / 60000 / 60;
            drawText(g, 350, 275, new Formatter().format("%.2f", currGameTime) + " Hours", 15);
        }
    }

    void drawLine(Graphics g, int x1, int y1, int x2, int y2) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(new Line2D.Double(x1, y1, x2, y2));
    }

}
