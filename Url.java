import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Url {
    public static double getPrice(String link) throws IOException{
        URLConnection url;
        InputStream is = null;
        BufferedReader br;
        String line;

        try {
            url = new URL(link).openConnection();
            url.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");

            is = url.getInputStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder=new StringBuilder();

            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
            }
            String content=stringBuilder.toString();

            int indexOf=content.indexOf("instrument-price-last");
            String s=content.substring(indexOf+23);
            s=s.split("<")[0];
            s=s.replace(',','.');
            return Double.parseDouble(s);
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
            return -2;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return -3;
            }
        }
    }
}