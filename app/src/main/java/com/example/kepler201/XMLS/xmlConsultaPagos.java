package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlConsultaPagos extends SoapSerializationEnvelope {

    String Usuario = "";
    String Password = "";
    String CodeAgente = "";


    public xmlConsultaPagos(int version) {
        super(version);
    }

    public void xmlConsultaP(String usuario, String password, String codeAgente) {

        Usuario = usuario;
        Password = password;
        CodeAgente = codeAgente;
    }

    public void write(XmlSerializer writer) throws IOException {
        env = "http://schemas.xmlsoap.org/soap/envelope/";
        String tem = "";
        writer.startDocument("UTF-8", true);
        writer.setPrefix("soap", env);

        writer.setPrefix("", tem);

        writer.startTag(env, "Envelope");

        writer.startTag(env, "Body");

        writer.startTag(tem, "ConsulPagrequest");

        writer.startTag(tem, "Login");

        writer.startTag(tem, "user");
        writer.text(Usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(Password);
        writer.endTag(tem, "pass");

        writer.endTag(tem, "Login");

        writer.startTag(tem, "ConsultaPa");


        writer.startTag(tem, "k_Agente");
        writer.text(CodeAgente);
        writer.endTag(tem, "k_Agente");

        writer.endTag(tem, "ConsultaPa");

        writer.endTag(tem, "ConsulPagrequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
