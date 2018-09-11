import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.apache.commons.net.util.SubnetUtils;

public class Main {

    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private List<String> facebookAddressList;
    private List<String> crawledIpList;

    public static void main(String[] args) {
        Main app = new Main();

        app.setFileReader("/Users/billyrichmond/Downloads/facebook-ips.txt");
        if (null == app.fileReader) {
            System.out.println("Cannot read file.");
        } else {
            app.setBufferedReader();
            app.setFacebookAddressList();
            app.doWork();
        }

        app.closeFileReader();
        app.closeBufferedReader();
    }

    private void closeBufferedReader() {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeFileReader() {
        try {
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    private void setBufferedReader() {
        bufferedReader = new BufferedReader(fileReader);
    }

    private void setFacebookAddressList() {
        facebookAddressList = new ArrayList<String>();
    }

    private void setFileReader(String fileName) {
        try {
            fileReader = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println(e.getStackTrace());
        }
    }

    private void doWork() {
        int count = 0;
        String line;
        try {
            while((line = this.getBufferedReader().readLine()) != null) {
                updateFacebookAddressList(new SubnetUtils(line));
            }
            Collections.sort(facebookAddressList);

            setFileReader("/Users/billyrichmond/Downloads/crawled-ips.txt");
            if (null == fileReader) {
                System.out.println("Cannot read file.");
            } else {
                setBufferedReader();
                while((line = getBufferedReader().readLine()) != null) {
                    validateCrawledAddress(line);
                    count++;
                }
            }
            System.out.println();
            System.out.println("Number of Facebook IPs: " + facebookAddressList.size());
            System.out.println("Number of IPs used: " + count);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateFacebookAddressList(SubnetUtils subnetUtils) {
        String[] addresses = subnetUtils.getInfo().getAllAddresses();
        facebookAddressList.addAll(Arrays.asList(addresses));
    }

    private void validateCrawledAddress(String crawledIp) {
        int response = Arrays.binarySearch(facebookAddressList.toArray(), crawledIp);
        if (!(response > 0)) {
            System.out.println(crawledIp + " is not a valid Facebook IP.");
        }
    }

}
