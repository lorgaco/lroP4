package lroP4;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

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
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;



public class P4 extends HttpServlet {

    //TvmlReader TvGuide;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            TvmlTransformer transformer = new TvmlTransformer();
            String outputHtml = transformer.transform();

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
    String transform () throws IOException, TransformerException {
        String outputHTML = new String();

        URL urlTvml = new URL("http://localhost:8024/lro24/tvml-ok.xml");
        URLConnection conTvml = urlTvml.openConnection();
        //StreamSource tvmlStream = new StreamSource(conTvml.getInputStream());
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = docFactory.newDocumentBuilder();
        Document doc = builder.parse(conTvml.getInputStream());

        URL urlXSL = new URL("http://localhost:8024/lro24/tvml14.xsl");
        URLConnection conXSL = urlXSL.openConnection();
        StreamSource xslStream = new StreamSource(conXSL.getInputStream());

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xslStream);
        StreamResult out = new StreamResult(outputHTML);
        transformer.transform(new DOMSource(doc), out);
        return outputHTML;

    }
}