package com.example.kepler201.XMLS;

import com.example.kepler201.SetterandGetter.CarritoBD;
import com.example.kepler201.SetterandGetter.FacturasnoregistradasSeleccionadasSANDG;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class xmlRegistroPagos2 extends SoapSerializationEnvelope {
    String usuario = "";
    String clave = "",Fecha="",Hora="";
    ArrayList<FacturasnoregistradasSeleccionadasSANDG> listaseleccionados;

    public xmlRegistroPagos2(int version) {
        super(version);
    }

    public void xmlRegistroPagos2(String usuario, String clave,String Fecha, String Hora, ArrayList<FacturasnoregistradasSeleccionadasSANDG> listaseleccionados) {
        this.usuario = usuario;
        this.clave = clave;
        this.Fecha =Fecha;
        this.Hora =Hora;
        this.listaseleccionados = listaseleccionados;

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

        writer.startTag(tem, "soapRequest");

        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");

        writer.startTag(tem, "Data");

        writer.startTag(tem, "k_items");

        for (int i = 0; i<listaseleccionados.size();i++){
            writer.startTag(tem, "item");

            writer.startTag(tem, "fecha");
            writer.text(Fecha);
            writer.endTag(tem, "fecha");

            writer.startTag(tem, "hora");
            writer.text(Hora);
            writer.endTag(tem, "hora");

            writer.startTag(tem, "cliente");
            writer.text(listaseleccionados.get(i).getCliente());
            writer.endTag(tem, "cliente");

            writer.startTag(tem, "vendedor");
            writer.text(listaseleccionados.get(i).getVendedor());
            writer.endTag(tem, "vendedor");

            writer.startTag(tem, "sucursal");
            writer.text(listaseleccionados.get(i).getSucursal());
            writer.endTag(tem, "sucursal");

            writer.startTag(tem, "folio");
            writer.text(listaseleccionados.get(i).getFolio());
            writer.endTag(tem, "folio");

            writer.startTag(tem, "monto");
            writer.text(listaseleccionados.get(i).getMonto());
            writer.endTag(tem, "monto");

            writer.startTag(tem, "montoapagar");
            writer.text(listaseleccionados.get(i).getMONTOAPAGAR());
            writer.endTag(tem, "montoapagar");

            writer.startTag(tem, "comentario");
            writer.text(listaseleccionados.get(i).getComentario());
            writer.endTag(tem, "comentario");


            writer.endTag(tem, "item");
        }


        writer.endTag(tem, "k_items");

        writer.endTag(tem, "Data");
        writer.endTag(tem, "soapRequest");


        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
