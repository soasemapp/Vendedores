package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlPresopuestoLinea extends SoapSerializationEnvelope {
    String usuario = "";
    String clave = "";
    String Fecha="";
    String Vendedor ="";
    String Mes="";
    String Año="";

    public xmlPresopuestoLinea(int version) {
        super(version);
    }

    public void xmlPresopuesto( String usuario, String clave, String fecha, String vendedor, String mes, String año) {

        this.usuario = usuario;
        this.clave = clave;
        Fecha = fecha;
        Vendedor = vendedor;
        Mes = mes;
        Año = año;
    }

    @Override
    public void write(XmlSerializer writer) throws IOException {
        env = "http://schemas.xmlsoap.org/soap/envelope/";
        String tem = "";
        writer.startDocument("UTF-8", true);
        writer.setPrefix("soap", env);

        writer.setPrefix("", tem);

        writer.startTag(env, "Envelope");

        writer.startTag(env, "Body");

        writer.startTag(tem, "RVPLYVRequest");

        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");

        writer.startTag(tem, "RVPLYV");

        writer.startTag(tem, "k_Fecha");
        writer.text(Fecha);
        writer.endTag(tem, "k_Fecha");

        writer.startTag(tem, "k_Vendedor");
        writer.text(Vendedor);
        writer.endTag(tem, "k_Vendedor");


        writer.startTag(tem, "k_MES");
        writer.text(Mes);
        writer.endTag(tem, "k_MES");


        writer.startTag(tem, "k_AÑO");
        writer.text(Año);
        writer.endTag(tem, "k_AÑO");

        writer.endTag(tem, "RVPLYV");

        writer.endTag(tem, "RVPLYVRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
