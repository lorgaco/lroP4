<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <html>
            <body>
                <h2>Programación</h2>
                <table border="1">
                    <tr bgcolor="#9acd32">
                        <th>Canal</th>
                        <th>Hora</th>
                        <th>Programa</th>
                        <th>Categoria</th>
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
                            </tr>
                        </xsl:for-each>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>