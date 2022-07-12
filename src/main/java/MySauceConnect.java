import com.saucelabs.ci.sauceconnect.AbstractSauceTunnelManager;
import com.saucelabs.ci.sauceconnect.SauceConnectFourManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MySauceConnect {
    public static void main(String[] args) {
        SauceConnectFourManager manager = new SauceConnectFourManager();
        final String SAUCE_USERNAME = System.getenv("SAUCE_USERNAME");
        final String SAUCE_ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");

        String options = "-v -i mytunnel";
        try {
            System.out.println("OS: " + System.getProperty("os.name"));
            System.out.println("Arch: " + System.getProperty("os.arch"));

            String SAUCE_CONNECT_PATH = System.getenv("SAUCE_CONNECT_PATH");

            manager.openConnection(SAUCE_USERNAME, SAUCE_ACCESS_KEY, 0, null, options, null, false, SAUCE_CONNECT_PATH);
            manager.closeTunnelsForPlan(SAUCE_USERNAME, options, null);
            File sauceConnectLogFile = manager.getSauceConnectLogFile(options);

            if (sauceConnectLogFile != null) {
                System.out.println("Sauce Connect log file: " + sauceConnectLogFile.getAbsolutePath());
                BufferedReader br = Files.newBufferedReader(sauceConnectLogFile.toPath());
                br.lines().forEach(System.out::println);
            } else {
                System.out.println("Sauce Connect log file does not exist");
            }


        } catch (AbstractSauceTunnelManager.SauceConnectException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        manager.closeTunnelsForPlan(SAUCE_USERNAME, options, null);
    }
}
