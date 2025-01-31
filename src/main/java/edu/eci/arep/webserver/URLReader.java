package edu.eci.arep.webserver;

import javax.imageio.IIOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class URLReader {
    public static void main(String[] args) throws Exception {
        String url = "https://www.google.com";
        URL siteURL = new URL(url);
        URLConnection urlConnection = siteURL.openConnection();
        Map<String, List<String>> headers = urlConnection.getHeaderFields();
        Set<Map.Entry<String, List<String>>> entrySet = headers.entrySet();
        for (Map.Entry<String, List<String>> entry : entrySet) {
            String headerName = entry.getKey();
            if (headerName != null) {
                System.out.println(headerName + ": " );
            }
            List<String> headerValues = entry.getValue();
            for (String value : headerValues) {
                System.out.println(value);
            }
            System.out.println("");

        }
        try {BufferedReader reader =
                new BufferedReader(new InputStreamReader(siteURL.openStream()));
            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
            }
        } catch (IOException x){
            System.err.println(x);
        }
    }
}
