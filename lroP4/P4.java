package lroP4;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import java.lang.Exception;
import java.lang.String;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class P4 extends HttpServlet {

    TvmlReader TvGuide;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        TvGuide = new TvmlReader();
        String errors = TvGuide.Read();

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Servicio de TV</title>");
        out.println("</head><body>");
        out.println("<h1>Servicio de consulta de la programaci&oacute;n</h1>");
        out.println("<h2>Bienvenido a este servicio</h2>");
        out.println("Estado de los TVML:<br />" + errors);
        out.println("<h3>Selecciona lo que quieres buscar:</h3>");
        out.println("<form method='POST' action='?step=1'>");
        List<dayStruct> days = TvGuide.getDays();
        ListIterator<dayStruct> it = days.listIterator();
        for(int ii=0; ii<days.size(); ii++){
            dayStruct day = it.next();
            if(ii==days.size()-1){
                out.println("<input type='radio' name='sURL' value='" + day.url + "' checked> " + day.day + "<BR>");
            }
            else{
                out.println("<input type='radio' name='sURL' value='" + day.url + "' > " + day.day + "<BR>");
            }
        }
        if (days.size()>0) {
            out.println("<p><input type='submit' value='Enviar'>");
        }
        out.println("</form>");
        out.println("</body></html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            String sURL = request.getParameter("sURL");

            TvmlTransformer transformer = new TvmlTransformer();
            String outputHtml = transformer.transform(sURL);

            out.println(outputHtml);
        } catch (Exception e){

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);

            out.println("<html><head><title>Exception</title></head><body>");
            out.println(sw.toString());
            out.println("</body></html>");
        }
    }
}

class TvmlTransformer {
    String transform (String sURL) throws IOException, TransformerException, SAXException, ParserConfigurationException {

        URL urlTvml = new URL(sURL);
        URLConnection conTvml = urlTvml.openConnection();
        //StreamSource tvmlStream = new StreamSource(conTvml.getInputStream());
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = docFactory.newDocumentBuilder();
        Document inDoc = builder.parse(conTvml.getInputStream());

        URL urlXSL = new URL("http://localhost:8024/lro24/tvml14.xsl");
        URLConnection conXSL = urlXSL.openConnection();
        StreamSource xslStream = new StreamSource(conXSL.getInputStream());

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xslStream);

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(new DOMSource(inDoc), result);
        writer.flush();

        return writer.toString();

    }
}