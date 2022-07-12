import com.saucelabs.ci.sauceconnect.AbstractSauceTunnelManager;
import com.saucelabs.ci.sauceconnect.SauceConnectFourManager;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.ZipOutputStream;

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
//                BufferedReader br = Files.newBufferedReader(sauceConnectLogFile.toPath());
//                br.lines().forEach(System.out::println);

//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                File zipFileName = Paths.get("/var/jenkins_home/sc-automatic-file.zip").toFile();
                FileOutputStream fos = new FileOutputStream(zipFileName);
                ZipOutputStream zipOutputStream = new ZipOutputStream(fos);
                zipOutputStream.setLevel(ZipOutputStream.STORED);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_kk-mm");
                addFileToZipStream(zipOutputStream, "".getBytes("UTF-8"), "generated_" + df.format(Calendar.getInstance().getTime()));
                addFileToZipStream(zipOutputStream, FileUtils.readFileToByteArray(sauceConnectLogFile), "sc.log");

                zipOutputStream.finish();
                zipOutputStream.flush();

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

    private static void addFileToZipStream(ZipOutputStream zipOutputStream, byte[] bytes, String filename) throws IOException {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(filename);
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(bytes);
        zipOutputStream.flush();
        zipOutputStream.closeEntry();
    }

}
