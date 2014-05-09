package lroP4;

import java.io.*;
import java.lang.Integer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


public class TvmlReader {
    List<dayStruct> days;
	
	private String url;

	void TvmlReader(){}

    List<dayStruct> Read(){
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.setErrorHandler(new TVML_ErrorHandler());
            days = new ArrayList<lroP4.dayStruct>();

            dayStruct day = new dayStruct();
            day.url = "http://localhost:8024/lro24/tvml-ok.xml";
            Document doc = db.parse(day.url);
            day.doc = doc;
            days.add(day);

			ListIterator<dayStruct> it = days.listIterator();
			int ii=0;
			do{
				it = days.listIterator(ii);
                day = it.next();
                day.day = doc.getDocumentElement().getElementsByTagName("Fecha").item(0).getTextContent();
                NodeList lChannels = day.doc.getElementsByTagName("Canal");
				
				for(int jj=0; jj<lChannels.getLength(); jj++){
					Element eChannel = (Element)lChannels.item(jj);

                    NodeList nlUrl = eChannel.getElementsByTagName("UrlTVML");
					if(nlUrl.getLength()>0){
						try{
                            dayStruct nDay = new dayStruct();
                            nDay.url = nlUrl.item(0).getTextContent();
                            doc = db.parse(nDay.url);
                            nDay.doc = doc;
                            days.add(nDay);
						}catch(Exception ex){
							ex.printStackTrace();
							url="no doc found";
						}
					}
				}
                ii++;
			}while(ii<days.size());
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
        return days;
	}
}

class TVML_ErrorHandler extends DefaultHandler {
	public TVML_ErrorHandler () {}
	public void warning(SAXParseException spe) {
		System.out.println("Warning: "+spe.toString());
	}
	public void error(SAXParseException spe) {
		System.out.println("Error: "+spe.toString());
	}
	public void fatalerror(SAXParseException spe) {
		System.out.println("Fatal Error: "+spe.toString());
	}
}

class dayStruct {
    String url;
    String day;
    Document doc;
}