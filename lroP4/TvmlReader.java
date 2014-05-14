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

    private boolean checkDay(String day){
        ListIterator<dayStruct> it = days.listIterator();
        for(int ii=0; ii<days.size(); ii++) {
            dayStruct sDay = it.next();
            if(sDay.day.equals(day)) return true;
        }
        return false;
    }

    String Read(){

        String errors = "All files ok";

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
            day.day = doc.getDocumentElement().getElementsByTagName("Fecha").item(0).getTextContent();
            days.add(day);

			ListIterator<dayStruct> it = days.listIterator();
			int ii=0;
			do{
				it = days.listIterator(ii);
                day = it.next();

                NodeList lChannels = day.doc.getElementsByTagName("Canal");
				
				for(int jj=0; jj<lChannels.getLength(); jj++){
					Element eChannel = (Element)lChannels.item(jj);

                    NodeList nlUrl = eChannel.getElementsByTagName("UrlTVML");
					if(nlUrl.getLength()>0){
						try{
                            dayStruct nDay = new dayStruct();
                            nDay.url = nlUrl.item(0).getTextContent();
                            doc = db.parse(nDay.url);
                            String Error = ErrorHandler.getError();
                            if(Error.equals("Ok")) {
                                nDay.doc = doc;
                                nDay.day = doc.getDocumentElement().getElementsByTagName("Fecha").item(0).getTextContent();
                                if (!checkDay(nDay.day)) days.add(nDay);
                            }
                            else {
                                if(errors.equals("All files ok")) errors = "";
                                errors = errors + Error + "<br />";
                            }
                        } catch (Exception ex) {
                            if(errors.equals("All files ok")) errors = "";
                            final StringWriter sw = new StringWriter();
                            final PrintWriter pw = new PrintWriter(sw, true);
                            ex.printStackTrace(pw);
                            errors = errors + "Error: " + ex.toString() + "<br />";
                        }
					}
				}
                ii++;
			}while(ii<days.size());

        }catch(Exception ex){
            //ex.printStackTrace();
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw, true);
            ex.printStackTrace(pw);
            return sw.getBuffer().toString();
        }

        return errors;
	}

    List<dayStruct> getDays() {
        return days;
    }

}

class TVML_ErrorHandler extends DefaultHandler {

    String Error;

    public TVML_ErrorHandler () {
        Error = "Ok";
    }
    public void warning(SAXParseException spe) {
        Error = "Warning: "+spe.toString();
    }
    public void error(SAXParseException spe) {
        Error = "Error: "+spe.toString();
    }
    public void fatalerror(SAXParseException spe) {
        Error = "Fatal Error: "+spe.toString();
    }
    public String getError() {
        String toReturn = new String(Error);
        Error = "Ok";
        return toReturn;
    }
}

class dayStruct {
    String url;
    String day;
    Document doc;
}