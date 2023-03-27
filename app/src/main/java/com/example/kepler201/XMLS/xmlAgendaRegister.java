package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlAgendaRegister extends SoapSerializationEnvelope {
    String usuario = "";
    String clave = "";
    String FechaAgenda = "";
    String ClaVendedor = "";
    String ClaCliente = "";
    String Actividad = "";
    String Partech = "";
    String Eagle = "";
    String Rodatech = "";
    String TG = "";
    String Trackone = "";
    String Fechavis = "";
    String comentantario = "";
    String agendo = "";


    public xmlAgendaRegister(int version) {
        super(version);
    }

    public void xmlAgendaRegister( String usuario, String clave, String fechaAgenda, String claVendedor, String claCliente, String actividad, String partech, String eagle, String rodatech, String TG, String trackone, String fechavis, String comentantario, String agendo) {

        this.usuario = usuario;
        this.clave = clave;
        this.FechaAgenda = fechaAgenda;
        this.ClaVendedor = claVendedor;
        this.ClaCliente = claCliente;
        this.Actividad = actividad;
        this.Partech = partech;
        this.Eagle = eagle;
        this.Rodatech = rodatech;
        this.TG = TG;
        this.Trackone = trackone;
        this.Fechavis = fechavis;
        this.comentantario = comentantario;
        this.agendo = agendo;
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

        writer.startTag(tem, "AgendaRegisterRequest");

        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");

        writer.startTag(tem, "AgendaRe");


        writer.startTag(tem, "k_FechaAgenda");
        writer.text(FechaAgenda);
        writer.endTag(tem, "k_FechaAgenda");

        writer.startTag(tem, "k_ClaveVe");
        writer.text(ClaVendedor);
        writer.endTag(tem, "k_ClaveVe");

        writer.startTag(tem, "k_ClaCliente");
        writer.text(ClaCliente);
        writer.endTag(tem, "k_ClaCliente");

        writer.startTag(tem, "k_Actividad");
        writer.text(Actividad);
        writer.endTag(tem, "k_Actividad");

        writer.startTag(tem, "k_Partech");
        writer.text(Partech);
        writer.endTag(tem, "k_Partech");

        writer.startTag(tem, "k_Eagle");
        writer.text(Eagle);
        writer.endTag(tem, "k_Eagle");

        writer.startTag(tem, "k_Rodatech");
        writer.text(Rodatech);
        writer.endTag(tem, "k_Rodatech");

        writer.startTag(tem, "k_TG");
        writer.text(TG);
        writer.endTag(tem, "k_TG");

        writer.startTag(tem, "k_TRACKONE");
        writer.text(Trackone);
        writer.endTag(tem, "k_TRACKONE");

        writer.startTag(tem, "k_FECHAVIS");
        writer.text(Fechavis);
        writer.endTag(tem, "k_FECHAVIS");

        writer.startTag(tem, "k_COMENTARIO");
        writer.text(comentantario);
        writer.endTag(tem, "k_COMENTARIO");

        writer.startTag(tem, "k_AGENDO");
        writer.text(agendo);
        writer.endTag(tem, "k_AGENDO");


        writer.endTag(tem, "AgendaRe");
        writer.endTag(tem, "AgendaRegisterRequest");


        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
/*<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:wsk="http://jacve.dyndns.org:9085/WSk75Branch">
   <soap:Header/>
   <soap:Body>
      <AgendaRegisterRequest>
         <Login>
            <user></user>
            <pass></pass>
            <!--Optional:-->
            <datasource></datasource>
         </Login>
         <AgendaRe>
            <k_FechaAgenda></k_FechaAgenda>
            <k_ClaCliente></k_ClaCliente>
            <k_Actividad></k_Actividad>
            <k_Partech></k_Partech>
            <k_Eagle></k_Eagle>
            <k_Rodatech></k_Rodatech>
            <k_TG></k_TG>
            <k_TRACKONE></k_TRACKONE>
            <k_FECHAVIS></k_FECHAVIS>
            <k_COMENTARIO></k_COMENTARIO>
            <k_AGENDO></k_AGENDO>
         </AgendaRe>
      </AgendaRegisterRequest>
   </soap:Body>
</soap:Envelope>*/