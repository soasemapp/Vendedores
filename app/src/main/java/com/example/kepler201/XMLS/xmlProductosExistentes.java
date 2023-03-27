package com.example.kepler201.XMLS;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class xmlProductosExistentes extends SoapSerializationEnvelope {

    String usuario = "";
    String clave = "";
    String code = "";
    String branch = "";
    String store = "";

    public xmlProductosExistentes(int version) {
        super(version);
    }


    public void valoresProEx(String usuario, String clave, String code, String branch, String store) {
        this.usuario = usuario;
        this.clave = clave;
        this.code = code;
        this.branch = branch;
        this.store = store;
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

        writer.startTag(tem, "stockRequest");

        writer.startTag(tem, "Login");

        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");

        writer.endTag(tem, "Login");

        writer.startTag(tem, "GetStock");

        writer.startTag(tem, "k_code");
        writer.text(code);
        writer.endTag(tem, "k_code");

        writer.startTag(tem, "k_branch");
        writer.text(branch);
        writer.endTag(tem, "k_branch");

        writer.startTag(tem, "k_store");
        writer.text(store);
        writer.endTag(tem, "k_store");

        writer.endTag(tem, "GetStock");

        writer.endTag(tem, "stockRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
/*
<soap:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:wsk="http://192.168.1.72:9085/WSk75Items">
   <soap:Header/>
   <soap:Body>
      <stockRequest>
         <Login>
            <user>JARED</user>
            <pass>JARED</pass>
            <!--Optional:-->
            <datasource></datasource>
         </Login>
         <GetStock>
            <k_code>1000R</k_code>
            <!--Optional:-->
            <k_branch></k_branch>
            <!--Optional:-->
            <k_store></k_store>
         </GetStock>
      </stockRequest>
   </soap:Body>
</soap:Envelope>
*/