package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlListLine extends SoapSerializationEnvelope {

    String usuario = "";
    String clave = "";


    public xmlListLine(int version) {
        super(version);
    }


    public void xmlListLine(String usuario, String clave) {
        this.usuario = usuario;
        this.clave = clave;
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

        writer.startTag(tem, "ListLiPreRequest");

        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");

        writer.endTag(tem, "ListLiPreRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
