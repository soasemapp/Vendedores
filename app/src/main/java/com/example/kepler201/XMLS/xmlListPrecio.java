package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlListPrecio  extends SoapSerializationEnvelope {

    String usuario = "";
    String clave = "";
    String Linea="";
    String Tipo ="";
    String Sucursal = "";



    public xmlListPrecio(int version) {
        super(version);
    }


    public  void  xmlListPrecio(String usuario, String clave, String linea, String tipo,String Sucursal) {

        this.usuario = usuario;
        this.clave = clave;
        this.Linea = linea;
        this.Tipo = tipo;
        this.Sucursal = Sucursal ;
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

        writer.startTag(tem, "ListPrecRequest");

        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");

        writer.startTag(tem, "ListPrec");

        writer.startTag(tem, "k_CodLinea");
        writer.text(Linea);
        writer.endTag(tem, "k_CodLinea");

        writer.startTag(tem, "k_codType");
        writer.text(Tipo);
        writer.endTag(tem, "k_codType");

        writer.startTag(tem, "k_Sucursal");
        writer.text(Sucursal);
        writer.endTag(tem, "k_Sucursal");

        writer.endTag(tem, "ListPrec");

        writer.endTag(tem, "ListPrecRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
