package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.NewsTag;
public class NewsTagListHandler extends DefaultHandler {
	private List<NewsTag> newsTagList = null;
	private NewsTag newsTag;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (newsTag != null) { 
            String valueString = new String(ch, start, length); 
            if ("tagId".equals(tempString)) 
            	newsTag.setTagId(new Integer(valueString).intValue());
            else if ("newsObj".equals(tempString)) 
            	newsTag.setNewsObj(new Integer(valueString).intValue());
            else if ("userObj".equals(tempString)) 
            	newsTag.setUserObj(valueString); 
            else if ("newsState".equals(tempString)) 
            	newsTag.setNewsState(new Integer(valueString).intValue());
            else if ("tagTime".equals(tempString)) 
            	newsTag.setTagTime(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("NewsTag".equals(localName)&&newsTag!=null){
			newsTagList.add(newsTag);
			newsTag = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		newsTagList = new ArrayList<NewsTag>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("NewsTag".equals(localName)) {
            newsTag = new NewsTag(); 
        }
        tempString = localName; 
	}

	public List<NewsTag> getNewsTagList() {
		return this.newsTagList;
	}
}
