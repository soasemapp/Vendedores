package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlSearchClientesG extends SoapSerializationEnvelope {

    String usuario = "";
    String clave = "";
    String usercode = "";


    public xmlSearchClientesG(int version) {
        super(version);
    }


    public void xmlSearchG(String usuario, String clave, String usercode) {

        this.usuario = usuario;
        this.clave = clave;
        this.usercode = usercode;
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

        writer.startTag(tem, "searchClientRequest");

        writer.startTag(tem, "Login");

        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");

        writer.endTag(tem, "Login");

        writer.startTag(tem, "SearchClient");

        writer.startTag(tem, "usercode");
        writer.text(usercode);
        writer.endTag(tem, "usercode");

        writer.endTag(tem, "SearchClient");

        writer.endTag(tem, "searchClientRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
