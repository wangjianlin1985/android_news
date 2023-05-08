package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.NewsComment;
public class NewsCommentListHandler extends DefaultHandler {
	private List<NewsComment> newsCommentList = null;
	private NewsComment newsComment;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (newsComment != null) { 
            String valueString = new String(ch, start, length); 
            if ("commentId".equals(tempString)) 
            	newsComment.setCommentId(new Integer(valueString).intValue());
            else if ("newsObj".equals(tempString)) 
            	newsComment.setNewsObj(new Integer(valueString).intValue());
            else if ("userObj".equals(tempString)) 
            	newsComment.setUserObj(valueString); 
            else if ("content".equals(tempString)) 
            	newsComment.setContent(valueString); 
            else if ("commentTime".equals(tempString)) 
            	newsComment.setCommentTime(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("NewsComment".equals(localName)&&newsComment!=null){
			newsCommentList.add(newsComment);
			newsComment = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		newsCommentList = new ArrayList<NewsComment>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("NewsComment".equals(localName)) {
            newsComment = new NewsComment(); 
        }
        tempString = localName; 
	}

	public List<NewsComment> getNewsCommentList() {
		return this.newsCommentList;
	}
}
