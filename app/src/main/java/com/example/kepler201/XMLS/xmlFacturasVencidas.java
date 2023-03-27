package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlFacturasVencidas extends SoapSerializationEnvelope {

    String usuario = "";
    String clave = "";
    String usercode = "";
    String cliente = "";

    public xmlFacturasVencidas(int version) {
        super(version);
    }

    public void xmlFacturasV(String usuario, String clave, String usercode, String cliente) {
        this.usuario = usuario;
        this.clave = clave;
        this.usercode = usercode;
        this.cliente = cliente;
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

        writer.startTag(tem, "factRequest");

        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");

        writer.startTag(tem, "GetfactList");
        writer.startTag(tem, "usercode");
        writer.text(usercode);
        writer.endTag(tem, "usercode");

        writer.startTag(tem, "cliente");
        writer.text(cliente);
        writer.endTag(tem, "cliente");

        writer.endTag(tem, "GetfactList");
        writer.endTag(tem, "factRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }

}
