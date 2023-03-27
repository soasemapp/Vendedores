package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlVersiones extends SoapSerializationEnvelope {
    String usuario = "";
    String clave = "";
    String ClaveVersion = "";

    public xmlVersiones(int version) {
        super(version);
    }

    public void xmlVersiones(String usuario, String clave, String ClaveVersion ) {
        this.usuario = usuario;
        this.clave = clave;
        this.ClaveVersion = ClaveVersion;

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

        writer.startTag(tem, "VersionesRequest");

        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");

        writer.startTag(tem, "Versiones");


        writer.startTag(tem, "Clave");
        writer.text(ClaveVersion);
        writer.endTag(tem, "Clave");

        writer.endTag(tem, "Versiones");
        writer.endTag(tem, "VersionesRequest");


        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
