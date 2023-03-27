package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlDispoSuc extends SoapSerializationEnvelope {
    String usuario = "";
    String clave = "";
    String producto = "";
    String sucursal = "";


    public xmlDispoSuc(int version) {
        super(version);
    }

    public void xmlDispoSuc(String usuario, String clave, String producto, String sucursal) {
        this.usuario = usuario;
        this.clave = clave;
        this.producto = producto;
        this.sucursal = sucursal;
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

        writer.startTag(tem, "DispoSucRequest");

        writer.startTag(tem, "Login");

        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");

        writer.endTag(tem, "Login");

        writer.startTag(tem, "DispoSucursal");

        writer.startTag(tem, "k_Productcode");
        writer.text(producto);
        writer.endTag(tem, "k_Productcode");

        writer.startTag(tem, "k_Sucursal");
        writer.text(sucursal);
        writer.endTag(tem, "k_Sucursal");


        writer.endTag(tem, "DispoSucursal");

        writer.endTag(tem, "DispoSucRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}