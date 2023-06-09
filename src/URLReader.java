import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class URLReader extends JPanel {

    String url = "null";
    String inputGameName = "*-";
    String fileName;
    JFrame frame;

    public URLReader(JFrame frame, String fileName) {
        this.frame = frame;
        this.fileName = fileName;
        createURLBox();
    }

    public void createURLBox() {
        // 创建一个面板来放置组件
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        // 创建一个标签和一个输入框，并将它们添加到面板上
        JLabel label = new JLabel("Please Enter image URL:");
        JTextField urlInputField = new JTextField();
        urlInputField.setPreferredSize(new Dimension(220, 20));
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = 0;
        gbcLabel.gridy = 0; // 将gridY设置为0，即第0行
        gbcLabel.insets = new Insets(5, -17, 5, 5);
        panel.add(label, gbcLabel);


        GridBagConstraints gbcField = new GridBagConstraints();
        gbcField.gridx = 1;
        gbcField.gridy = 0; // 将gridY设置为0，即第0行
        gbcField.fill = GridBagConstraints.HORIZONTAL;
        gbcField.insets = new Insets(5, 5, 5, 5);
        panel.add(urlInputField, gbcField);

        // 创建第二个标签和输入框，并将它们添加到面板上
        JLabel label1 = new JLabel("Please Enter the game name:");
        JTextField gameNameInputField = new JTextField();
        gameNameInputField.setPreferredSize(new Dimension(220, 20));
        GridBagConstraints gbcLabel1 = new GridBagConstraints();
        gbcLabel1.gridx = 0;
        gbcLabel1.gridy = 1; // 将gridy设置为1，即第1行
        gbcLabel1.insets = new Insets(-40, 5, 5, 5);
        panel.add(label1, gbcLabel1);

        GridBagConstraints gbcField2 = new GridBagConstraints();
        gbcField2.gridx = 1;
        gbcField2.gridy = 1; // 将gridy设置为1，即第1行
        gbcField2.fill = GridBagConstraints.HORIZONTAL;
        gbcField2.insets = new Insets(-40, 5, 5, 5);
        panel.add(gameNameInputField, gbcField2);

        // 创建一个面板来放置确认和取消按钮
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 40, 0)); // 水平间距为10，垂直间距为0
        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");
        confirmButton.setFocusPainted(false);
        confirmButton.setBackground(new Color(27, 80, 104));
        confirmButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBackground(new Color(27, 80, 104));
        cancelButton.setForeground(Color.WHITE);
        buttonsPanel.add(confirmButton);
        buttonsPanel.add(cancelButton);
        GridBagConstraints gbcButtons = new GridBagConstraints();
        gbcButtons.gridx = 1;
        gbcButtons.gridy = 1; // 将gridX设置为1，即第1行
        gbcButtons.fill = GridBagConstraints.HORIZONTAL;
        gbcButtons.insets = new Insets(40, -100, 10, 50); // 修改下方间距
        panel.add(buttonsPanel, gbcButtons);

        // 设置面板的大小并将其添加到窗口中
        panel.setPreferredSize(new Dimension(400, 150));
        add(panel);

        // If the confirm button is clicked, set url to the input text and close the window
        confirmButton.addActionListener(e -> {
            url = urlInputField.getText();
            inputGameName = gameNameInputField.getText();
            try {
                getImage(url);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            frame.dispose();
        });

        // If the cancel button is clicked, close the window
        cancelButton.addActionListener(e -> {
            try {
                getImage(url);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            frame.dispose();
        });

        // 设置面板的大小并将其添加到窗口中
        panel.setPreferredSize(new Dimension(400, 150));
        add(panel);
    }

    public void getImage(String url) throws IOException {

        // Check if the image depository exists, if not, create one
        boolean imageDepositoryExist = false;
        String basePath = "./";
        String[] basePathList = new File(basePath).list();
        for (int i = 0; i < Objects.requireNonNull(basePathList).length; i++) {
            if (basePathList[i].equals("GameImageDepository")) {
                imageDepositoryExist = true;
            }
        }
        if (!imageDepositoryExist) {
            File imageDepository = new File("./GameImageDepository");
            imageDepository.mkdir();
        }

        new Thread(() -> {  // Download the image, and save it to the image depository
            try {
                DownloadImage(fileName, url, inputGameName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public synchronized static void DownloadImage(String gameName, String urlString, String inputGameName) throws IOException {
        if (urlString.equals("null")) { //If click the cancel button, game image position is NULL, and return
            AddGame.newGameWriter(null, inputGameName);
            return;

        } else if (urlString.equals("")) {
            //Design Philosophy: Believing that users' actions have undergone profound thinking
            AddGame.newGameWriter(null, inputGameName);
            return;
        } else if (!urlString.contains("http")) {
            //If the input is not a URL, show the error message, input again
            JFrame frame = new JFrame("Image URL");
            JOptionPane.showMessageDialog(null, "Invalid URL.", "Please Enter Again"
                    , JOptionPane.ERROR_MESSAGE);
            frame.setContentPane(new URLReader(frame, gameName));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setResizable(false);
            return;
        }
        URL url = new URL(urlString);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
            InputStream input = conn.getInputStream();
            byte[] buf = new byte[1024];
            int length;
            while ((length = input.read(buf, 0, buf.length)) != -1) {
                output.write(buf, 0, length);
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileOutputStream out = new FileOutputStream("./GameImageDepository/" + gameName + ".png");
        out.write(output.toByteArray());
        out.close();
        // At that time, after the validity check, call the method to write the game data to the file
        AddGame.newGameWriter(new File("").getAbsolutePath() + "\\GameImageDepository\\" + gameName + ".png", inputGameName);
    }
}
