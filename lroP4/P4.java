package lroP4;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

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

public class P3 extends HttpServlet {

    //TvmlReader TvGuide;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        TvmlTransformer transformer = new TvmlTransformer();
        String outputHtml = transformer.transform();

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println(outputHtml);
    }
}

public class TvmlTransformer {
    String transform () throws IOException, TransformerException {
        String outputHTML;

        URL urlTvml = new URL("http://localhost:8024/lro24/tvml-ok.xml");
        URLConnection conTvml = urlTvml.openConnection();
        StreamSource tvmlStream = new StreamSource(conTvml.getInputStream());

        URL urlXSL = new URL("http://localhost:8024/lro24/tvml.xsl");
        URLConnection conXSL = urlXSL.openConnection();
        StreamSource xslStream = new StreamSource(conXSL.getInputStream());

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xslStream);
        StreamResult out = new StreamResult(outputHTML);
        transformer.transform(tvmlStream, out);
        return outputHTML;

    }
}