package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.VideoShare;
public class VideoShareListHandler extends DefaultHandler {
	private List<VideoShare> videoShareList = null;
	private VideoShare videoShare;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (videoShare != null) { 
            String valueString = new String(ch, start, length); 
            if ("videoShareId".equals(tempString)) 
            	videoShare.setVideoShareId(new Integer(valueString).intValue());
            else if ("videoTitle".equals(tempString)) 
            	videoShare.setVideoTitle(valueString); 
            else if ("videoFile".equals(tempString)) 
            	videoShare.setVideoFile(valueString); 
            else if ("userObj".equals(tempString)) 
            	videoShare.setUserObj(valueString); 
            else if ("shareTime".equals(tempString)) 
            	videoShare.setShareTime(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("VideoShare".equals(localName)&&videoShare!=null){
			videoShareList.add(videoShare);
			videoShare = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		videoShareList = new ArrayList<VideoShare>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("VideoShare".equals(localName)) {
            videoShare = new VideoShare(); 
        }
        tempString = localName; 
	}

	public List<VideoShare> getVideoShareList() {
		return this.videoShareList;
	}
}
