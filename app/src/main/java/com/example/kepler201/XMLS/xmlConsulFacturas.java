package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlConsulFacturas  extends SoapSerializationEnvelope {
    String usuario = "";
    String clave = "";
    String usercode= "";
    String cliente = "";
    String fechaEntrada = "";
    String fechaSalida = "";

    public xmlConsulFacturas(int version) {
        super(version);
    }

    public void xmlConsulFactur( String usuario, String clave, String usercode, String cliente, String fechaEntrada, String fechaSalida) {

        this.usuario = usuario;
        this.clave = clave;
        this.usercode = usercode;
        this.cliente = cliente;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
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

        writer.startTag(tem, "ConsulFactReques");

        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");

        writer.startTag(tem, "ConsulFactura");

        writer.startTag(tem, "k_usercode");
        writer.text(usercode);
        writer.endTag(tem, "k_usercode");

        writer.startTag(tem, "k_cliente");
        writer.text(cliente);
        writer.endTag(tem, "k_cliente");


        writer.startTag(tem, "k_fechaE");
        writer.text(fechaEntrada);
        writer.endTag(tem, "k_fechaE");

        writer.startTag(tem, "k_fechaS");
        writer.text(fechaSalida);
        writer.endTag(tem, "k_fechaS");

        writer.endTag(tem, "ConsulFactura");


        writer.endTag(tem, "ConsulFactReques");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
