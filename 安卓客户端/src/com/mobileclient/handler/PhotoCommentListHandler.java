package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.PhotoComment;
public class PhotoCommentListHandler extends DefaultHandler {
	private List<PhotoComment> photoCommentList = null;
	private PhotoComment photoComment;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (photoComment != null) { 
            String valueString = new String(ch, start, length); 
            if ("photoCommentId".equals(tempString)) 
            	photoComment.setPhotoCommentId(new Integer(valueString).intValue());
            else if ("photoObj".equals(tempString)) 
            	photoComment.setPhotoObj(new Integer(valueString).intValue());
            else if ("content".equals(tempString)) 
            	photoComment.setContent(valueString); 
            else if ("userInfoObj".equals(tempString)) 
            	photoComment.setUserInfoObj(valueString); 
            else if ("commentTime".equals(tempString)) 
            	photoComment.setCommentTime(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("PhotoComment".equals(localName)&&photoComment!=null){
			photoCommentList.add(photoComment);
			photoComment = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		photoCommentList = new ArrayList<PhotoComment>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("PhotoComment".equals(localName)) {
            photoComment = new PhotoComment(); 
        }
        tempString = localName; 
	}

	public List<PhotoComment> getPhotoCommentList() {
		return this.photoCommentList;
	}
}
