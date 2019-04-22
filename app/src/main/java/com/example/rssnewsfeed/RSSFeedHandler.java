package com.example.rssnewsfeed;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;

public class RSSFeedHandler extends DefaultHandler {

    private RSSFeed feed;
    private RSSItem item;

    private boolean feedTitleHasBeenRead = false;
    private boolean feedPubDateHasBeenRead = false;

    private boolean isTitle = false;
    private boolean isDescription = false;
    private boolean isLink = false;
    private boolean isPubDate = false;
    private boolean isOrigLink = false;

    public RSSFeed getFeed() {
        return feed;
    }

    @Override
    public void startDocument() throws SAXException {
        feed = new RSSFeed();
        item = new RSSItem();
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (qName.equals("item")){
            item = new RSSItem();
            return;
        }
        else if (qName.equals("title")){
            isTitle = true;
            return;
        }
        else if (qName.equals("description")){
            isDescription = true;
            return;
        }
        else if (qName.equals("link")){
            isLink = true;
            return;
        }
        else if (qName.equals("pubDate")){
            isPubDate = true;
            return;
        }
        else if (qName.equals("feedburner:origLink")){
            isOrigLink = true;
            return;
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (qName.equals("item")){
            feed.addItem(item);
            return;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) {
        String s = new String(ch, start, length);
        if (isTitle){
            if (feedTitleHasBeenRead == false) {
                feed.setTitle(s);
                feedTitleHasBeenRead = true;
            }
            else {
                if (s.startsWith("<")){
                    item.setTitle("No title available.");
                }
                else if (s.length() > 60) {
                    item.setDescription(s);
                    int endIndex = s.indexOf(" ", 50);
                    if (endIndex == -1) {
                        endIndex = 60;
                    }
                    String title = s.substring(0, endIndex);
                    title += "...";
                    item.setTitle(title);
                }
                else {
                    item.setTitle(s);
                    item.setDescription(s);
                }
            }
            isTitle = false;
        }
        else if (isLink) {
            item.setLink(s);
            isLink = false;
        }
        else if (isDescription) {
            if (!s.startsWith("<")){
                item.setDescription(s);
            }
            isDescription = false;
        }
        else if (isPubDate) {
            if (feedPubDateHasBeenRead == false) {
                feed.setPubDate(s);
                feedTitleHasBeenRead = true;
            }
            else {
                item.setPubDate(s);
            }
            isPubDate = false;
        }
        else if (isOrigLink) {
            String dateParts[] = s.split("/");
            String date;
            if (dateParts.length > 8) {
                String year = dateParts[3];
                String month = dateParts[4];
                String day = dateParts[5];
                if (s.startsWith("http://www.cnn.com/videos")) {
                    year = dateParts[5];
                    month = dateParts[6];
                    day = dateParts[7];
                }
                date = year + "-" + month + "-" + day;
            } else {
                date = "No publication date available.";
            }
            item.setPubDate(date);
            isOrigLink = false;
        }
    }
}