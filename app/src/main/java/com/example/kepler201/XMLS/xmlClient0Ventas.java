package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlClient0Ventas extends SoapSerializationEnvelope {

    String usuario = "";
    String clave = "";
    String usercode = "";
    String dias = "";

    public xmlClient0Ventas(int version) {

        super(version);
    }

    public void xmlClient0V(String usuario, String clave, String usercode, String dias) {
        this.usuario = usuario;
        this.clave = clave;
        this.usercode = usercode;
        this.dias = dias;
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

        writer.startTag(tem, "clientRequest");

        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");

        writer.startTag(tem, "GetClientList");
        writer.startTag(tem, "usercode");
        writer.text(usercode);
        writer.endTag(tem, "usercode");

        writer.startTag(tem, "fechalimit");
        writer.text(dias);
        writer.endTag(tem, "fechalimit");

        writer.endTag(tem, "GetClientList");
        writer.endTag(tem, "clientRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }

}
