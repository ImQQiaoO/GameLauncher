import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class Data {
    public static URL frameIconURL;

    static {
        try {
            frameIconURL = Paths.get("images/icon1.jpg").toAbsolutePath().toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ImageIcon frameIcon = new ImageIcon(frameIconURL);
}
