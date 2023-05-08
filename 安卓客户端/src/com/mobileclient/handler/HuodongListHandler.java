package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.Huodong;
public class HuodongListHandler extends DefaultHandler {
	private List<Huodong> huodongList = null;
	private Huodong huodong;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (huodong != null) { 
            String valueString = new String(ch, start, length); 
            if ("huodongId".equals(tempString)) 
            	huodong.setHuodongId(new Integer(valueString).intValue());
            else if ("title".equals(tempString)) 
            	huodong.setTitle(valueString); 
            else if ("content".equals(tempString)) 
            	huodong.setContent(valueString); 
            else if ("telephone".equals(tempString)) 
            	huodong.setTelephone(valueString); 
            else if ("personList".equals(tempString)) 
            	huodong.setPersonList(valueString); 
            else if ("userObj".equals(tempString)) 
            	huodong.setUserObj(valueString); 
            else if ("addTime".equals(tempString)) 
            	huodong.setAddTime(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("Huodong".equals(localName)&&huodong!=null){
			huodongList.add(huodong);
			huodong = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		huodongList = new ArrayList<Huodong>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("Huodong".equals(localName)) {
            huodong = new Huodong(); 
        }
        tempString = localName; 
	}

	public List<Huodong> getHuodongList() {
		return this.huodongList;
	}
}
