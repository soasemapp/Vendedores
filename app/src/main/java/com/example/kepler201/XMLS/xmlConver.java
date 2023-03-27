package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlConver extends SoapSerializationEnvelope {
    String usuario = "";
    String clave = "";
    String Productcode = "";


    public xmlConver(int version) {
        super(version);
    }

    public void xmlConver(String usuario, String clave, String usercode) {
        this.usuario = usuario;
        this.clave = clave;
        this.Productcode = usercode;
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

        writer.startTag(tem, "ConverRequest");

        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");

        writer.startTag(tem, "Conversi");

        writer.startTag(tem, "k_Clacom");
        writer.text(Productcode);
        writer.endTag(tem, "k_Clacom");

        writer.endTag(tem, "Conversi");


        writer.endTag(tem, "ConverRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
