package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlConsultaPagos2 extends SoapSerializationEnvelope {


    String Usuario = "";
    String Password = "";
    String CodeAgente = "";
    String Cliente = "";
    String FechaInicio = "";
    String FechaFinal = "";
    String Confirmar = "";
    public xmlConsultaPagos2(int version) {
        super(version);
    }

    public void xmlConsultapago2( String usuario, String password, String codeAgente, String cliente, String fechaInicio, String fechaFinal, String confirmar) {

        Usuario = usuario;
        Password = password;
        CodeAgente = codeAgente;
        Cliente = cliente;
        FechaInicio = fechaInicio;
        FechaFinal = fechaFinal;
        Confirmar = confirmar;
    }
    public void write(XmlSerializer writer) throws IOException {
        env = "http://schemas.xmlsoap.org/soap/envelope/";
        String tem = "";
        writer.startDocument("UTF-8", true);
        writer.setPrefix("soap", env);

        writer.setPrefix("", tem);

        writer.startTag(env, "Envelope");

        writer.startTag(env, "Body");

        writer.startTag(tem, "ConsulPagrequest");

        writer.startTag(tem, "Login");

        writer.startTag(tem, "user");
        writer.text(Usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(Password);
        writer.endTag(tem, "pass");

        writer.endTag(tem, "Login");

        writer.startTag(tem, "ConsultaPa");


        writer.startTag(tem, "k_Agente");
        writer.text(CodeAgente);
        writer.endTag(tem, "k_Agente");

        writer.startTag(tem, "k_Cliente");
        writer.text(Cliente);
        writer.endTag(tem, "k_Cliente");

        writer.startTag(tem, "k_FechaInic");
        writer.text(FechaInicio);
        writer.endTag(tem, "k_FechaInic");


        writer.startTag(tem, "k_FechaFin");
        writer.text(FechaFinal);
        writer.endTag(tem, "k_FechaFin");


        writer.startTag(tem, "k_Confirmar");
        writer.text(Confirmar);
        writer.endTag(tem, "k_Confirmar");


        writer.endTag(tem, "ConsultaPa");

        writer.endTag(tem, "ConsulPagrequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
