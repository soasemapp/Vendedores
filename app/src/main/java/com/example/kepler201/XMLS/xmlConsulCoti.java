package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlConsulCoti extends SoapSerializationEnvelope {

    String Usuario = "";
    String Password = "";
    String sucursal = "";
    String vendedor =" ";



    public xmlConsulCoti(int version) {
        super(version);
    }



    public  void xmlConsulCoti( String usuario, String password, String sucursal, String vendedor) {

        Usuario = usuario;
        Password = password;
        this.sucursal = sucursal;
        this.vendedor = vendedor;
    }

    public void write(XmlSerializer writer) throws IOException {
        env = "http://schemas.xmlsoap.org/soap/envelope/";
        String tem = "";
        writer.startDocument("UTF-8", true);
        writer.setPrefix("soap", env);

        writer.setPrefix("", tem);

        writer.startTag(env, "Envelope");

        writer.startTag(env, "Body");

        writer.startTag(tem, "ConsulCotRequest");

        writer.startTag(tem, "Login");

        writer.startTag(tem, "user");
        writer.text(Usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(Password);
        writer.endTag(tem, "pass");

        writer.endTag(tem, "Login");

        writer.startTag(tem, "ConsulCoti");


        writer.startTag(tem, "k_Sucursal");
        writer.text(sucursal);
        writer.endTag(tem, "k_Sucursal");


        writer.startTag(tem, "k_Vendedor");
        writer.text(vendedor);
        writer.endTag(tem, "k_Vendedor");


        writer.endTag(tem, "ConsulCoti");

        writer.endTag(tem, "ConsulCotRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }

}
