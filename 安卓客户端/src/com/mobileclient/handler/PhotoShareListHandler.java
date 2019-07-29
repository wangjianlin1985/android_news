package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.PhotoShare;
public class PhotoShareListHandler extends DefaultHandler {
	private List<PhotoShare> photoShareList = null;
	private PhotoShare photoShare;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (photoShare != null) { 
            String valueString = new String(ch, start, length); 
            if ("sharePhotoId".equals(tempString)) 
            	photoShare.setSharePhotoId(new Integer(valueString).intValue());
            else if ("photoTitle".equals(tempString)) 
            	photoShare.setPhotoTitle(valueString); 
            else if ("sharePhoto".equals(tempString)) 
            	photoShare.setSharePhoto(valueString); 
            else if ("userInfoObj".equals(tempString)) 
            	photoShare.setUserInfoObj(valueString); 
            else if ("shareTime".equals(tempString)) 
            	photoShare.setShareTime(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("PhotoShare".equals(localName)&&photoShare!=null){
			photoShareList.add(photoShare);
			photoShare = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		photoShareList = new ArrayList<PhotoShare>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("PhotoShare".equals(localName)) {
            photoShare = new PhotoShare(); 
        }
        tempString = localName; 
	}

	public List<PhotoShare> getPhotoShareList() {
		return this.photoShareList;
	}
}
