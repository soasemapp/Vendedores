package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlBackOrders extends SoapSerializationEnvelope {
    String usuario = "";
    String clave = "";
    String cliente = "";
    String fechaEntrada = "";
    String fechaSalida = "";
    String Sucursal = "";

    public xmlBackOrders(int version) {
        super(version);
    }

    public void xmlBackOrdersdatos(String usuario, String clave, String cliente, String fechaEntrada, String fechaSalida, String Sucursal) {
        this.usuario = usuario;
        this.clave = clave;
        this.cliente = cliente;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.Sucursal =Sucursal ;
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

        writer.startTag(tem, "backOrdeRequest");

        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");

        writer.startTag(tem, "BackOrders");
        writer.startTag(tem, "cliente");
        writer.text(cliente);
        writer.endTag(tem, "cliente");

        writer.startTag(tem, "fechaE");
        writer.text(fechaEntrada);
        writer.endTag(tem, "fechaE");


        writer.startTag(tem, "fechaS");
        writer.text(fechaSalida);
        writer.endTag(tem, "fechaS");

        writer.startTag(tem, "k_Sucursal");
        writer.text(Sucursal);
        writer.endTag(tem, "k_Sucursal");

        writer.endTag(tem, "BackOrders");
        writer.endTag(tem, "backOrdeRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
