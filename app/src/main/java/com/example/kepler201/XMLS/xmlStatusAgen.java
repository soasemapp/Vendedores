package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlStatusAgen extends SoapSerializationEnvelope {
    String usuario = "";
    String clave = "";
    String Fecha = "";
    String Vendedor = "";
    String ClaveCliente = "";
    String Actividad = "";
    String Status= "";
    String StatusGenerar= "";


    public xmlStatusAgen(int version) {
        super(version);
    }

    public void xmlStatusAgen(String usuario, String clave, String fecha, String vendedor, String claveCliente, String actividad, String status, String statusGenerar) {

        this.usuario = usuario;
        this.clave = clave;
        this.Fecha = fecha;
        this.Vendedor = vendedor;
        this.ClaveCliente = claveCliente;
        this.Actividad = actividad;
        this.Status = status;
        this.StatusGenerar = statusGenerar;
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

        writer.startTag(tem, "statusAgendaRequest");

        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");

        writer.startTag(tem, "statusAgen");


        writer.startTag(tem, "k_Fecha");
        writer.text(Fecha);
        writer.endTag(tem, "k_Fecha");

        writer.startTag(tem, "k_vendedor");
        writer.text(Vendedor);
        writer.endTag(tem, "k_vendedor");

        writer.startTag(tem, "k_ccliente");
        writer.text(ClaveCliente);
        writer.endTag(tem, "k_ccliente");

        writer.startTag(tem, "k_actividad");
        writer.text(Actividad);
        writer.endTag(tem, "k_actividad");


        writer.startTag(tem, "k_status");
        writer.text(Status);
        writer.endTag(tem, "k_status");

        writer.startTag(tem, "k_statuscambio");
        writer.text(StatusGenerar);
        writer.endTag(tem, "k_statuscambio");


        writer.endTag(tem, "statusAgen");
        writer.endTag(tem, "statusAgendaRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
