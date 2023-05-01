import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.List;

public class OldMain {

    static Process process;
    static long totalPlayTime = 0;
    static ArrayList<String> playedGameList = new ArrayList<>();
    static String finChoice = "";

    public static void main(String[] args) throws InterruptedException {

        final String[] path = {""};
        JFrame jFrame = new JFrame("VillvCotton's Pirate Games Launcher");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setLayout(null);
        jFrame.setSize(508, 300);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        JLabel textLabel = new JLabel("Please Select Your Game:");
        textLabel.setBounds(175, 50, 150, 50);
        JButton jButton = new JButton("Select");
        JButton jButtonGame = new JButton("Start");
        JButton jShowList = new JButton("Show My Game Time List");
        jButton.setBounds(94, 125, 100, 50);
        jButtonGame.setBounds(300, 125, 100, 50);
        jShowList.setBounds(94,185, 306,50);
        JPanel textPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        textPanel.add(textLabel);
        buttonPanel.add(jButton);
        buttonPanel.add(jButtonGame);
        buttonPanel.add(jShowList);
        jFrame.add(textLabel);
        jFrame.add(jButton);
        jFrame.add(jButtonGame);
        jFrame.add(jShowList);
        JLabel gameName = new JLabel("You Have Selected: ...");
        gameName.setBounds(175, 70, 300, 50);
        textPanel.add(gameName);
        jFrame.add(gameName);
        jButton.addActionListener(new ActionListener() { //selected button
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame jFrameSelecting = new JFrame("Please Select Your Game");
                jFrameSelecting.setLocationRelativeTo(null);
                jFrameSelecting.setLayout(null);
                jFrameSelecting.setSize(380, 252);
                jFrameSelecting.setVisible(true);
                jFrameSelecting.setResizable(false);
                JButton chooseButton = new JButton("Choose");
                JButton confirmButton = new JButton("Confirm");
                JButton deleteButton = new JButton("Delete");
                JLabel choosingTips = new JLabel("If you have ever opened a game, please select it below.");
                JComboBox<String> gameList = new JComboBox<>();
                choosingTips.setBounds(30, 30, 400, 50);
                chooseButton.setBounds(75, 125, 85, 50);
                confirmButton.setBounds(207, 125, 85, 50);
                gameList.setBounds(80, 80, 130, 25);
                deleteButton.setBounds(210, 80, 75, 25);
                JPanel choosingTextPanel = new JPanel();
                JPanel choosingButtonPanel = new JPanel();
                JPanel choosingListPanel = new JPanel();
                choosingTextPanel.add(choosingTips);
                choosingButtonPanel.add(chooseButton);
                choosingButtonPanel.add(confirmButton);
                choosingButtonPanel.add(deleteButton);
                choosingListPanel.add(gameList);
                jFrameSelecting.add(choosingTips);
                jFrameSelecting.add(chooseButton);
                jFrameSelecting.add(confirmButton);
                jFrameSelecting.add(deleteButton);
                jFrameSelecting.add(gameList);

                int folderFinder = 0;
                String basePath = "..\\StatisticsOfPiratedGameTime";
                String[] list = new String[]{};
                list = new File(basePath).list();
                for (int i = 0; i < Objects.requireNonNull(list).length; i++) {
                    if (list[i].equals("GameInfo")){
                        folderFinder ++;
                    }
                }

                if (folderFinder == 0){
                    File dir = new File("..\\StatisticsOfPiratedGameTime\\GameInfo\\");
                    dir.mkdir();
                }

                int cnt = 0;
                int cntListFinder = 0;
                for (int i = 0; i < Objects.requireNonNull(list).length; i++) {
                    if (list[i].equals("_playedGameList_.txt")) {
                        cntListFinder++;
                    }
                }
                if (cntListFinder == 0) {
                    String fileName = "..\\StatisticsOfPiratedGameTime\\_playedGameList_.txt";
                    Path path = Paths.get(fileName);
                    try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
                        bufferedWriter.write("");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                ArrayList<String> playedGameList0 = new ArrayList<>();
                FileInputStream FIS = null;
                try {
                    FIS = new FileInputStream("..\\StatisticsOfPiratedGameTime\\_playedGameList_.txt");
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                InputStreamReader ISR = new InputStreamReader(FIS);
                BufferedReader BR = new BufferedReader(ISR);
                String readGameListLine;
                while (true) {
                    try {
                        if ((readGameListLine = BR.readLine()) != null) {
                            playedGameList0.add(readGameListLine.substring(readGameListLine.lastIndexOf("\\") + 1));
                        }else {
                            break;
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                for (String s : playedGameList0) {
                    System.out.println(s);
                    gameList.addItem(s);
                }

                chooseButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
//                        chooseCounter ++;
                        JFileChooser fileChooser = new JFileChooser();
                        int dialog = fileChooser.showOpenDialog(null);
                        if (dialog == JFileChooser.APPROVE_OPTION) {
                            // Get the path of the selected file
                            path[0] = fileChooser.getSelectedFile().getAbsolutePath();
                            System.out.println(path[0]);
                            String selectedGame = path[0].substring(path[0].lastIndexOf("\\") + 1);
                            System.out.println(selectedGame);
                            String basePath = "..\\StatisticsOfPiratedGameTime\\GameInfo";
                            String[] list = new File(basePath).list();
                            int cnt = 0;
                            int cntListFinder = 0;
                            for (int i = 0; i < Objects.requireNonNull(list).length; i++) {
                                if (list[i].equals(selectedGame + ".txt")) {
                                    cnt++;
                                }

                            }
                            if (cnt == 0) {
                                String fileName = "..\\StatisticsOfPiratedGameTime\\GameInfo\\" + selectedGame + ".txt";
                                Path path = Paths.get(fileName);
                                try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
                                    bufferedWriter.write("0");
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                            FileInputStream fileInputStream = null;
                            try {
                                fileInputStream = new FileInputStream("..\\StatisticsOfPiratedGameTime\\_playedGameList_.txt");
                            } catch (FileNotFoundException ex) {
                                throw new RuntimeException(ex);
                            }
                            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            String strLine = "";
                            int lineCnt = 0;
                            int cntNotSame = 0;
                            while (true) {
                                try {
                                    strLine = bufferedReader.readLine();
                                    lineCnt ++;
//                                    System.out.println("read " + strLine + " 0");
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                                if ((strLine) == null) {
                                    System.out.println("列表内容为空");
                                    String fileName = "..\\StatisticsOfPiratedGameTime\\_playedGameList_.txt";
                                    Path path = Paths.get(fileName);
                                    try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
                                        bufferedWriter.write(fileChooser.getSelectedFile().getAbsolutePath() + "\n");
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    break;//
                                    //todo
                                }
                                if (strLine.equals(fileChooser.getSelectedFile().getAbsolutePath())) {//
                                    System.out.println("列表存在相同内容");
                                    break;
                                }

                            }
                            FileInputStream FIS = null;
                            try {
                                FIS = new FileInputStream("..\\StatisticsOfPiratedGameTime\\_playedGameList_.txt");
                            } catch (FileNotFoundException ex) {
                                throw new RuntimeException(ex);
                            }
                            InputStreamReader ISR = new InputStreamReader(FIS);
                            BufferedReader BR = new BufferedReader(ISR);
                            String readGameListLine;
                            gameList.removeAllItems();
                            playedGameList.clear();
                            while (true) {
                                try {
                                    if ((readGameListLine = BR.readLine()) != null) {
                                        playedGameList.add(readGameListLine.substring(readGameListLine.lastIndexOf("\\") + 1));
                                    } else {
                                        break;
                                    }
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                            for (String s : playedGameList) {
                                System.out.println(s);
                                gameList.addItem(s);//todo
                            }
                        }
                    }
                });


                confirmButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jFrameSelecting.dispose();
                        ArrayList <String> finPlayedGameList = new ArrayList<>();
                        System.out.println(gameList.getSelectedIndex());
                        FileInputStream FIS = null;
                        try {
                            FIS = new FileInputStream("..\\StatisticsOfPiratedGameTime\\_playedGameList_.txt");
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        InputStreamReader ISR = new InputStreamReader(FIS);
                        BufferedReader BR = new BufferedReader(ISR);
                        String readGameListLine;
                        while (true) {
                            try {
                                if ((readGameListLine = BR.readLine()) != null) {
                                    finPlayedGameList.add(readGameListLine);
                                } else {
                                    break;
                                }
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        finChoice = finPlayedGameList.get(gameList.getSelectedIndex());
                        System.out.println(finChoice);
                        gameName.setText("You Have Selected: " + finChoice);

                    }
                });

                ArrayList<String> deletedList = new ArrayList<>();
                deleteButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        FileInputStream FIS = null;
                        try {
                            FIS = new FileInputStream("..\\StatisticsOfPiratedGameTime\\_playedGameList_.txt");
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        InputStreamReader ISR = new InputStreamReader(FIS);
                        BufferedReader BR = new BufferedReader(ISR);
                        String readGameListLine;
                        deletedList.clear();
                        while (true) {
                            try {
                                if ((readGameListLine = BR.readLine()) != null) {
                                    deletedList.add(readGameListLine);
                                } else {
                                    break;
                                }
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        deletedList.remove(gameList.getSelectedIndex());
                        gameList.removeItem(gameList.getSelectedIndex());
                        gameList.removeItemAt(gameList.getSelectedIndex());
                        String fileName = "..\\StatisticsOfPiratedGameTime\\_playedGameList_.txt";
                        Path path = Paths.get(fileName);
                        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
                            bufferedWriter.write("");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        System.out.println(deletedList.size());
                        for (int i = 0; i < deletedList.size(); i++) {
                            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path,StandardOpenOption.APPEND)) {
                                bufferedWriter.write(deletedList.get(i) + "\n");
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        System.out.println(deletedList.size());
                    }
                });
            }
        });

        jButtonGame.addActionListener(new ActionListener() {//start button
            @Override
            public void actionPerformed (ActionEvent e){
                if (finChoice.equals("")){
                    JOptionPane.showMessageDialog(null, "You Didn't select anything");
                }else{
                jFrame.dispose();
                try {

//                process = Runtime.getRuntime().exec("cmd /c "+ finChoice.charAt(0) + ": && cd " +
//                        finChoice.substring(0, finChoice.lastIndexOf("\\")) + " && start " + finChoice);
//                Process p = Runtime.getRuntime().exec(finChoice);
                    File f = new File(finChoice.substring(0, finChoice.lastIndexOf("\\")));
                    process = Runtime.getRuntime().exec(finChoice,null,f);

//                System.out.println(process.info());
//                System.out.println(p.info());
                    long startTime = System.currentTimeMillis();
                    long endTime = startTime;
                    //调用应用进程
                    boolean shut = process.isAlive();
                    while (shut) {
                        shut = process.isAlive();
                        Thread.sleep(10000);
                        if (!shut) {
                            endTime = System.currentTimeMillis();
                            long playTimeMs = endTime - startTime;
                            String playTime = String.valueOf(playTimeMs);
                            float playTimeMin = Float.parseFloat(playTime) / 60000;

                            int m = finChoice.lastIndexOf("\\");
                            String nameOfGame = finChoice.substring(m + 1);
                            FileInputStream fileInputStream = new FileInputStream("..\\StatisticsOfPiratedGameTime\\GameInfo\\"
                                    + nameOfGame + ".txt");
                            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            String strLine;
                            long lastTime = 0;
                            while ((strLine = bufferedReader.readLine()) != null) {
                                lastTime = Long.parseLong(strLine);
                            }

                            String fileName = "..\\StatisticsOfPiratedGameTime\\GameInfo\\" + nameOfGame + ".txt";
                            Path path = Paths.get(fileName);

                            totalPlayTime = lastTime + playTimeMs;
                            float totalPlayTimeMin = Float.parseFloat(String.valueOf(totalPlayTime)) / 60000;

                            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
                                bufferedWriter.write(String.valueOf(totalPlayTime));
                            }

                            JOptionPane.showMessageDialog(null,
                                    "You played for " + new Formatter().format("%.1f", Float.parseFloat(String.valueOf(playTimeMin))) +
                                            " Minutes This Time.\n" + "You played for " +
                                            new Formatter().format("%.1f", Float.parseFloat(String.valueOf(totalPlayTimeMin))) +
                                            " Minutes In Total.\n",
                                    "Play Happily!", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    System.out.println(endTime - startTime);
                    System.out.println(totalPlayTime);
                } catch (IOException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

            }
            }
        });

        jShowList.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("GameTime List");
//                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                JPanel panel = new JPanel();
//        JTextArea textArea = new JTextArea();
                Vector<Object> contents = new Vector<>();
                JList<Object> listGame = new JList<>(contents);
                String basePath = "..\\StatisticsOfPiratedGameTime\\GameInfo";
                String[] list = new String[]{};
                list = new File(basePath).list();
                String contentName;
                String contentTime;
                HashMap<String, Double> playTimeMap = new HashMap<String, Double>();

                for (int i = 0; i < Objects.requireNonNull(list).length; i++) {

                    contentName = list[i].substring(0,list[i].lastIndexOf("."));
                    System.out.println(list[i]);
                    //Read the number in every txt file and calculate the number, save the result as hours
                    try {
                        contentTime = new String(Files.readAllBytes(Paths.get("..\\StatisticsOfPiratedGameTime\\GameInfo\\" + list[i])));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.out.println(contentTime);
                    playTimeMap.put(contentName, Double.parseDouble(contentTime));
//                    contents.add(contentName + "      "  + new Formatter().format("%.2f", Double.parseDouble(contentTime)/60000/60) + " hours");
                }
                System.out.println("排序前:" + playTimeMap);
                List<Map.Entry<String, Double>> newTimeMapList = new ArrayList<Map.Entry<String, Double>>(playTimeMap.entrySet());
                Collections.sort(newTimeMapList, (o1, o2) -> (o2.getValue().compareTo(o1.getValue())));
                System.out.println("排序后:" + newTimeMapList);
                for (int i = 0; i < Objects.requireNonNull(list).length; i++) {
                    contents.add(newTimeMapList.get(i).getKey() + "    -    " + new Formatter().format("%.2f", Double.parseDouble
                            (String.valueOf(newTimeMapList.get(i).getValue()))/60000/60) + " hours");
                }


                panel.setLayout(new GridLayout());
                panel.add(new JScrollPane(listGame));
                frame.add(panel);

                frame.setSize(400, 500);
                frame.setVisible(true);
            }
        });
    }
}