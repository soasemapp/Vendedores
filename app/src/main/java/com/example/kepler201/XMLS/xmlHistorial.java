package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlHistorial extends SoapSerializationEnvelope {

    String user;
    String pass;
    String vendedor;
    String cliente;
    String linea;
    String tipo;
    String fecha;

    public xmlHistorial(int version) {
        super(version);
    }

    public void xmlHistorial(String user, String pass, String vendedor, String cliente, String linea, String tipo, String fecha) {
        this.user = user;
        this.pass = pass;
        this.vendedor = vendedor;
        this.cliente = cliente;
        this.linea = linea;
        this.tipo = tipo;
        this.fecha = fecha;
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

        writer.startTag(tem, "HistRequest");

        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(user);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(pass);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");

        writer.startTag(tem, "Historial");

        writer.startTag(tem, "k_Vendedor");
        writer.text(vendedor);
        writer.endTag(tem, "k_Vendedor");

        writer.startTag(tem, "k_Cliente");
        writer.text(cliente);
        writer.endTag(tem, "k_Cliente");

        writer.startTag(tem, "k_Linea");
        writer.text(linea);
        writer.endTag(tem, "k_Linea");

        writer.startTag(tem, "k_Tipo");
        writer.text(tipo);
        writer.endTag(tem, "k_Tipo");


        writer.startTag(tem, "k_Fecha");
        writer.text(fecha);
        writer.endTag(tem, "k_Fecha");

        writer.endTag(tem, "Historial");


        writer.endTag(tem, "HistRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
