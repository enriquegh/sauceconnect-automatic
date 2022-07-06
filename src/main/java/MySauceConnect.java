import com.saucelabs.ci.sauceconnect.AbstractSauceTunnelManager;
import com.saucelabs.ci.sauceconnect.SauceConnectFourManager;

import java.io.File;

public class MySauceConnect {
    public static void main(String[] args) {
        SauceConnectFourManager manager = new SauceConnectFourManager();
        final String SAUCE_USERNAME = System.getenv("SAUCE_USERNAME");
        final String SAUCE_ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");

        String options = "-v -i mytunnel";
        try {
            manager.openConnection(SAUCE_USERNAME, SAUCE_ACCESS_KEY, 0, null, options, null, false, null);
            manager.closeTunnelsForPlan(SAUCE_USERNAME, options, null);
            File sauceConnectLogFile = manager.getSauceConnectLogFile(options);

            if (sauceConnectLogFile.exists()) {
                System.out.println("Sauce Connect log file: " + sauceConnectLogFile.getAbsolutePath());
            } else {
                System.out.println("Sauce Connect log file does not exist");
            }


        } catch (AbstractSauceTunnelManager.SauceConnectException e) {
            e.printStackTrace();
        }
//        manager.closeTunnelsForPlan(SAUCE_USERNAME, options, null);
    }
}
