package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlAsignaCordenadas extends SoapSerializationEnvelope {
    String usuario = "";
    String clave = "";
    String Cliente = "";
    String idDireccion = "";
    String Latitud = "";
    String Longitud = "";
    String valCon = "";

    public xmlAsignaCordenadas(int version) {
        super(version);
    }

    public void xmlAgenxmlAsignaCordenadasda(String usuario, String clave, String Cliente, String idDireccion ,String Latitud ,String Longitud, String valCon) {
        this.usuario = usuario;
        this.clave = clave;
        this.Cliente = Cliente;
        this.idDireccion = idDireccion;
        this.Latitud = Latitud;
        this.Longitud = Longitud;
        this.valCon = valCon;

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

        writer.startTag(tem, "AsignCordenadasRequest");

        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");

        writer.startTag(tem, "AsignCordenada");


        writer.startTag(tem, "k_Cliente");
        writer.text(Cliente);
        writer.endTag(tem, "k_Cliente");

        writer.startTag(tem, "k_idDireccion");
        writer.text(idDireccion);
        writer.endTag(tem, "k_idDireccion");


        writer.startTag(tem, "k_Latitud");
        writer.text(Latitud);
        writer.endTag(tem, "k_Latitud");


        writer.startTag(tem, "k_Longitud");
        writer.text(Longitud);
        writer.endTag(tem, "k_Longitud");

        writer.startTag(tem, "k_valCon");
        writer.text(valCon);
        writer.endTag(tem, "k_valCon");


        writer.endTag(tem, "AsignCordenada");
        writer.endTag(tem, "AsignCordenadasRequest");


        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
