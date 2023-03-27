package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlExistClasif extends SoapSerializationEnvelope {

    String usuario = "";
    String clave = "";
    String Clasificacion = "";
    String Sucursal;
    String Linea ="";

    public xmlExistClasif(int version) {
        super(version);
    }

    public void xmlExistClas(String usuario, String clave, String clasificacion, String Sucursal, String Linea) {
        this.usuario = usuario;
        this.clave = clave;
        this.Clasificacion = clasificacion;
        this.Sucursal = Sucursal;
        this.Linea = Linea ;
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

        writer.startTag(tem, "ExistClasifRequest");

        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");

        writer.startTag(tem, "ExisteCla");

        writer.startTag(tem, "k_Clasificacion");
        writer.text(Clasificacion);
        writer.endTag(tem, "k_Clasificacion");

        writer.startTag(tem, "k_Sucursal");
        writer.text(Sucursal);
        writer.endTag(tem, "k_Sucursal");

        writer.startTag(tem, "k_CodLinea");
        writer.text(Linea);
        writer.endTag(tem, "k_CodLinea");

        writer.endTag(tem, "ExisteCla");




        writer.endTag(tem, "ExistClasifRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
