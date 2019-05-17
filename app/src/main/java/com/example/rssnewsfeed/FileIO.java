package com.example.rssnewsfeed;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import android.content.Context;
import android.util.Log;

public class FileIO {

   // private final String URL_STRING = "https://sdtimes.com/feed/";
   // private final String URL_STRING = "https://www.nasa.gov/rss/dyn/breaking_news.rss";
    private final String URL_STRING = "www.trmilitarynews.com/feed";
    private final String FILENAME = "news_feed.xml";

    private Context context = null;

    public FileIO (Context context) {this.context = context; }

    public void downloadFile() {
        try{
            URL url = new URL(URL_STRING);
            InputStream in = url.openStream();
            FileOutputStream out = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);
            while (bytesRead != -1) {

                out.write(buffer, 0, bytesRead);
                bytesRead = in.read(buffer);


            }
            out.close();
            in.close();
        }
        catch (IOException e) {
            Log.e("News reader", e.toString());

        }

    }

    public RSSFeed readFile() {
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            RSSFeedHandler theRssHandler = new RSSFeedHandler();
            xmlreader.setContentHandler(theRssHandler);

            FileInputStream in = context.openFileInput(FILENAME);
            InputSource is = new InputSource(in);
            xmlreader.parse(is);

            RSSFeed feed = theRssHandler.getFeed();
            return feed;
        }
        catch (Exception e) {
            Log.e("News reader", e.toString());
            return null;
        }
    }
}