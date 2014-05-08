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

public class P3 extends HttpServlet {

    TvmlReader TvGuide;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Servicio de TV</title>");
        out.println("</head><body>");
        out.println("<h1>Servicio de consulta de la programaci&oacute;n</h1>");
        out.println("<h2>Bienvenido a este servicio</h2>");
        out.println("<h3>Selecciona lo que quieres buscar:</h3>");
        out.println("<form method='POST' action='?step=1'>");
        out.println("<input type='radio' name='query' value='shows' checked> Consulta series<br>");
        out.println("<input type='radio' name='query' value='movies'> Consulta Pel&iacute;culas<br>");
        out.println("<p><input type='submit' value='Enviar'>");
        out.println("</form>");
        out.println("</body></html>");

    }
}

public class TvmlTransformer {
    String transform () throws IOException, ParserConfigurationException, SAXException, TransformerException {
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