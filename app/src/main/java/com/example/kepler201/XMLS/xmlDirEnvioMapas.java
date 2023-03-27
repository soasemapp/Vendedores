package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlDirEnvioMapas extends SoapSerializationEnvelope {
    String usuario = "";
    String clave = "";
    String cliente = "";


    public xmlDirEnvioMapas(int version) {
        super(version);
    }

    public void xmlDirEnvioMapas(String usuario, String clave, String cliente) {
        this.usuario = usuario;
        this.clave = clave;
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

        writer.startTag(tem, "EnvioMapaRequest");

        writer.startTag(tem, "Login");

        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");

        writer.endTag(tem, "Login");

        writer.startTag(tem, "EnvioMapa");

        writer.startTag(tem, "k_Cliente");
        writer.text(cliente);
        writer.endTag(tem, "k_Cliente");


        writer.endTag(tem, "EnvioMapa");

        writer.endTag(tem, "EnvioMapaRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
