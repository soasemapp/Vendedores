package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlPros extends SoapSerializationEnvelope {
    String usuario = "";
    String clave = "";
    String Vendedor = "";
    String Fecha = "";

    public xmlPros(int version) {
        super(version);
    }

    public void xmlProspect(String usuario, String clave, String vendedor, String fecha) {
        this.usuario = usuario;
        this.clave = clave;
        Vendedor = vendedor;
        Fecha = fecha;
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

        writer.startTag(tem, "ProsRequest");

        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");

        writer.startTag(tem, "Prospect");


        writer.startTag(tem, "k_Vendedor");
        writer.text(Vendedor);
        writer.endTag(tem, "k_Vendedor");

        writer.startTag(tem, "k_Fecha");
        writer.text(Fecha);
        writer.endTag(tem, "k_Fecha");

        writer.endTag(tem, "Prospect");
        writer.endTag(tem, "ProsRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
