<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <html>
            <head><title>Servicio de TV</title></head>
            <body>
                <h1>Servicio de consulta de la programación</h1>
                <h2>Programación</h2>
                <table border="1">
                    <tr bgcolor="#9acd32">
                        <th>Canal</th>
                        <th>Hora</th>
                        <th>Programa</th>
                        <th>Categoria</th>
                        <th>Edad</th>
                        <th>Idiomas</th>
                    </tr>
                    <xsl:for-each select="Programacion/Canal">
                        <xsl:sort select="Programa"/>
                        <xsl:for-each select="Programa">
                            <xsl:sort select="Intervalo/HoraInicio"/>
                            <tr>
                                <td><xsl:value-of select="../NombreCanal"/></td>
                                <td><xsl:value-of select="Intervalo/HoraInicio"/></td>
                                <td><xsl:value-of select="NombrePrograma"/></td>
                                <td><xsl:value-of select="Categoria"/></td>
                                <td><xsl:value-of select="@edadminima"/></td>
                                <td><xsl:choose>
                                    <xsl:when test="@langs">
                                        <xsl:value-of select="@langs"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="../@lang"/>
                                    </xsl:otherwise>
                                </xsl:choose></td>
                            </tr>
                        </xsl:for-each>
                    </xsl:for-each>
                </table>
                <br />
                <form>
                    <input type='submit' value='Atrás' onClick='document.forms[0].method="GET"'/>
                </form>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>