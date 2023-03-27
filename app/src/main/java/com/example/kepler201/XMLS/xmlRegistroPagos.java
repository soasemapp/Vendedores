package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlRegistroPagos extends SoapSerializationEnvelope {
    String Usuario = "";
    String Password = "";
    String Agente = "";
    String Cliente = "";
    String Date = "";
    String Facturas = "";
    String Importe = "";
    String Banco = "";
    String FormaP = "";
    String Comentarios1 = "";
    String Comentarios2 = "";
    String Comentarios3 = "";

    public xmlRegistroPagos(int version) {
        super(version);
    }

    public void xmlRegistroP(String usuario, String password, String agente, String cliente, String date, String facturas, String importe, String banco, String formaP, String comentarios1, String comentarios2, String comentarios3) {
        Usuario = usuario;
        Password = password;
        Agente = agente;
        Cliente = cliente;
        Date = date;
        Facturas = facturas;
        Importe = importe;
        Banco = banco;
        FormaP = formaP;
        Comentarios1 = comentarios1;
        Comentarios2 = comentarios2;
        Comentarios3 = comentarios3;
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

        writer.startTag(tem, "RegiPagrequest");

        writer.startTag(tem, "Login");

        writer.startTag(tem, "user");
        writer.text(Usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(Password);
        writer.endTag(tem, "pass");

        writer.endTag(tem, "Login");

        writer.startTag(tem, "Registro");

        writer.startTag(tem, "k_Agente");
        writer.text(Agente);
        writer.endTag(tem, "k_Agente");

        writer.startTag(tem, "k_Cliente");
        writer.text(Cliente);
        writer.endTag(tem, "k_Cliente");

        writer.startTag(tem, "k_Date");
        writer.text(Date);
        writer.endTag(tem, "k_Date");

        writer.startTag(tem, "k_Importe");
        writer.text(Importe);
        writer.endTag(tem, "k_Importe");

        writer.startTag(tem, "k_Facturas");
        writer.text(Facturas);
        writer.endTag(tem, "k_Facturas");

        writer.startTag(tem, "k_Banco");
        writer.text(Banco);
        writer.endTag(tem, "k_Banco");

        writer.startTag(tem, "k_FormaP");
        writer.text(FormaP);
        writer.endTag(tem, "k_FormaP");

        writer.startTag(tem, "k_Comentario1");
        writer.text(Comentarios1);
        writer.endTag(tem, "k_Comentario1");

        writer.startTag(tem, "k_Comentario2");
        writer.text(Comentarios2);
        writer.endTag(tem, "k_Comentario2");

        writer.startTag(tem, "k_Comentario3");
        writer.text(Comentarios3);
        writer.endTag(tem, "k_Comentario3");

        writer.endTag(tem, "Registro");

        writer.endTag(tem, "RegiPagrequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }

}
