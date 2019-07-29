package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.NewsCollection;
public class NewsCollectionListHandler extends DefaultHandler {
	private List<NewsCollection> newsCollectionList = null;
	private NewsCollection newsCollection;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (newsCollection != null) { 
            String valueString = new String(ch, start, length); 
            if ("collectionId".equals(tempString)) 
            	newsCollection.setCollectionId(new Integer(valueString).intValue());
            else if ("newsObj".equals(tempString)) 
            	newsCollection.setNewsObj(new Integer(valueString).intValue());
            else if ("userObj".equals(tempString)) 
            	newsCollection.setUserObj(valueString); 
            else if ("collectTime".equals(tempString)) 
            	newsCollection.setCollectTime(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("NewsCollection".equals(localName)&&newsCollection!=null){
			newsCollectionList.add(newsCollection);
			newsCollection = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		newsCollectionList = new ArrayList<NewsCollection>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("NewsCollection".equals(localName)) {
            newsCollection = new NewsCollection(); 
        }
        tempString = localName; 
	}

	public List<NewsCollection> getNewsCollectionList() {
		return this.newsCollectionList;
	}
}
