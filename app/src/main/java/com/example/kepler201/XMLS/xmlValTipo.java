package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlValTipo extends SoapSerializationEnvelope {

    String usuario = "";
    String clave = "";


    public xmlValTipo(int version) {
        super(version);
    }


    public void xmlValTipo(String usuario, String clave) {
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

        writer.startTag(tem, "ValTipoUserRequest");

        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");

        writer.startTag(tem, "ValUser");

        writer.startTag(tem, "k_usr");
        writer.text(usuario);
        writer.endTag(tem, "k_usr");

        writer.endTag(tem, "ValUser");





        writer.endTag(tem, "ValTipoUserRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
