package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlProductosNuevos extends SoapSerializationEnvelope {

    String usuario = "";
    String clave = "";
    String sucursal="";

    public xmlProductosNuevos(int version) {
        super(version);
    }

    public void xmlProductosNuevos(String usuario, String clave,String sucursal) {
        this.usuario = usuario;
        this.clave = clave;
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
        writer.startTag(tem, "ProdcutosNuevosRequest");
        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");
        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");


        writer.startTag(tem, "ProductosNuevos");

        writer.startTag(tem, "k_sucursal");
        writer.text(sucursal);
        writer.endTag(tem, "k_sucursal");

        writer.endTag(tem, "ProductosNuevos");


        writer.endTag(tem, "ProdcutosNuevosRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}