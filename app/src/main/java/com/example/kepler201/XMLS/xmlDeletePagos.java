package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlDeletePagos extends SoapSerializationEnvelope {
    String Usuario = "";
    String Password = "";
    String Fecha= "";
    String Hora= "";
    String Clave= "";

    public xmlDeletePagos(int version) {
        super(version);
    }

    public void xmlDeletePag(String usuario, String password, String fecha, String hora, String clave) {

        Usuario = usuario;
        Password = password;
        Fecha = fecha;
        Hora = hora;
        Clave = clave;
    }

    public void write(XmlSerializer writer) throws IOException {
        env = "http://schemas.xmlsoap.org/soap/envelope/";
        String tem = "";
        writer.startDocument("UTF-8", true);
        writer.setPrefix("soap", env);

        writer.setPrefix("", tem);

        writer.startTag(env, "Envelope");

        writer.startTag(env, "Body");

        writer.startTag(tem, "DeletePagrequest");

        writer.startTag(tem, "Login");

        writer.startTag(tem, "user");
        writer.text(Usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(Password);
        writer.endTag(tem, "pass");

        writer.endTag(tem, "Login");

        writer.startTag(tem, "DeletPay");


        writer.startTag(tem, "k_Fecha");
        writer.text(Fecha);
        writer.endTag(tem, "k_Fecha");

        writer.startTag(tem, "k_Hora");
        writer.text(Hora);
        writer.endTag(tem, "k_Hora");

        writer.startTag(tem, "k_Clave");
        writer.text(Clave);
        writer.endTag(tem, "k_Clave");

        writer.endTag(tem, "DeletPay");

        writer.endTag(tem, "DeletePagrequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
