package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.Zambia;
public class ZambiaListHandler extends DefaultHandler {
	private List<Zambia> zambiaList = null;
	private Zambia zambia;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (zambia != null) { 
            String valueString = new String(ch, start, length); 
            if ("zambiaId".equals(tempString)) 
            	zambia.setZambiaId(new Integer(valueString).intValue());
            else if ("newsObj".equals(tempString)) 
            	zambia.setNewsObj(new Integer(valueString).intValue());
            else if ("userObj".equals(tempString)) 
            	zambia.setUserObj(valueString); 
            else if ("zambiaTime".equals(tempString)) 
            	zambia.setZambiaTime(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("Zambia".equals(localName)&&zambia!=null){
			zambiaList.add(zambia);
			zambia = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		zambiaList = new ArrayList<Zambia>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("Zambia".equals(localName)) {
            zambia = new Zambia(); 
        }
        tempString = localName; 
	}

	public List<Zambia> getZambiaList() {
		return this.zambiaList;
	}
}
