package com.example.kepler201.XMLS;

import com.example.kepler201.SetterandGetter.CarritoBD;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class xmlVerificacionPrecios extends SoapSerializationEnvelope {
    String usuario = "";
    String clave = "";
    String Cliente = "";
    String Desc1 ="";
    ArrayList<CarritoBD> listaCarShoping;

    public xmlVerificacionPrecios(int version) {
        super(version);
    }

    public void xmlVerificacionPrecios(String usuario, String clave, String Cliente,String Desc1, ArrayList<CarritoBD> listaCarShoping) {
        this.usuario = usuario;
        this.clave = clave;
        this.Cliente = Cliente;
        this.Desc1 = Desc1;
        this.listaCarShoping = listaCarShoping;
    }
    private static String formatNumberCurrency(String number) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        return formatter.format(Double.parseDouble(number));
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

        writer.startTag(tem, "VerificaPreciosRequest");

        writer.startTag(tem, "Login");
        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");
        writer.endTag(tem, "Login");

        writer.startTag(tem, "VerificaPrecios");


        writer.startTag(tem, "k_clientes");
        writer.text(Cliente);
        writer.endTag(tem, "k_clientes");
        writer.startTag(tem, "k_items");

        for (int i = 0; i<listaCarShoping.size();i++){
            writer.startTag(tem, "item");



            double Desc1P;
            double Desc1D;
            double Precio;

            Precio=Double.parseDouble(listaCarShoping.get(i).getPrecio());
            Desc1P =Double.parseDouble(listaCarShoping.get(i).getDesc1());
            Desc1D=Double.parseDouble(Desc1);


            Precio = (Precio-((Precio*Desc1P)/100));

            Precio =(Precio-((Precio*Desc1D)/100));


            writer.startTag(tem, "k_Precio");
            writer.text(String.valueOf(Precio));
            writer.endTag(tem, "k_Precio");

            writer.startTag(tem, "k_Producto");
            writer.text(listaCarShoping.get(i).getParte());
            writer.endTag(tem, "k_Producto");


            writer.endTag(tem, "item");
        }


        writer.endTag(tem, "k_items");

        writer.endTag(tem, "VerificaPrecios");
        writer.endTag(tem, "VerificaPreciosRequest");


        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }
}
