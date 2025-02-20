package com.example.kepler201.XMLS;

import com.example.kepler201.SetterandGetter.DetallCotiSANDG;
import com.example.kepler201.SetterandGetter.valExiPedSANDG;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class xmlPedido extends SoapSerializationEnvelope {
    String Comentario = "";
    String ClaveVendedor = "";
    String NombreCliente = "";
    String ClaveCliente = "";
    String FechaActual = "";
    String FechaVencimiento = "";
    String Sucursal = "";
    String usuario = "";
    String clave = "";
    String rfcCliente = "";
    String plazo = "";
    String Montototal = "";
    String Iva = "";
    String Descuento = "";
    String DescuentoPro = "";
    String Desc1 = "";
    String Calle = "";
    String Colonia = "";
    String Poblacion = "";
    String FolioPrev="";
    String Via="";
    String stridEnvio="";
    double DescProstr = 0;
    double Descuentorec = 0;
    String DescuentoStr;
    ArrayList<DetallCotiSANDG> listasearch2 = new ArrayList<>();

    double cantidad;
    double precio;
    double descuento;
    double monto;
    double monto2;
    double monto3;
    String Server;





    public xmlPedido(int version) {
        super(version);
    }


    public void xmlPedido( String comentario, String claveVendedor, String nombreCliente, String claveCliente, String fechaActual, String fechaVencimiento,
                     String sucursal, String usuario, String clave, String rfcCliente, String plazo, String montototal, String iva, String descuento, String descuentoPro,
                     String Desc1,String calle, String colonia, String poblacion, String folioPrev,String Via,String stridEnvio,ArrayList<DetallCotiSANDG> listasearch2, String StrServer) {
        this.Comentario = comentario;
        this.ClaveVendedor = claveVendedor;
        this.NombreCliente = nombreCliente;
        this.ClaveCliente = claveCliente;
        this.FechaActual = fechaActual;
        this.FechaVencimiento = fechaVencimiento;
        this.Sucursal = sucursal;
        this.usuario = usuario;
        this.clave = clave;
        this.rfcCliente = rfcCliente;
        this.plazo = plazo;
        this.Montototal = montototal;
        this.Iva = iva;
        this.Descuento = descuento;
        this.DescuentoPro = descuentoPro;
        this.Desc1 = Desc1;
        this.Calle = calle;
        this.Colonia = colonia;
        this.Poblacion = poblacion;
        this.FolioPrev = folioPrev;
        this.Via = Via;
        this.stridEnvio = stridEnvio;
        this.listasearch2 = listasearch2;
        this.Server = StrServer;

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

        writer.startTag(tem, "newDocRequest");

        writer.startTag(tem, "Login");

        writer.startTag(tem, "user");
        writer.text(usuario);
        writer.endTag(tem, "user");

        writer.startTag(tem, "pass");
        writer.text(clave);
        writer.endTag(tem, "pass");

        writer.endTag(tem, "Login");

        writer.startTag(tem, "Doc");

        writer.startTag(tem, "k_sucursal");
        writer.text(Sucursal);
        writer.endTag(tem, "k_sucursal");

        writer.startTag(tem, "k_almacen");
        writer.text("1");
        writer.endTag(tem, "k_almacen");

        writer.startTag(tem, "k_genero");
        writer.text("U");
        writer.endTag(tem, "k_genero");

        writer.startTag(tem, "k_naturaleza");
        writer.text("D");
        writer.endTag(tem, "k_naturaleza");

        writer.startTag(tem, "k_grupo");
        writer.text("41");
        writer.endTag(tem, "k_grupo");

        writer.startTag(tem, "k_tipo");
        writer.text("1");
        writer.endTag(tem, "k_tipo");

        writer.startTag(tem, "k_fecha");
        writer.text(FechaActual);
        writer.endTag(tem, "k_fecha");

        writer.startTag(tem, "k_clave");
        writer.text(ClaveCliente);
        writer.endTag(tem, "k_clave");

        writer.startTag(tem, "k_rfc");
        writer.text(rfcCliente);
        writer.endTag(tem, "k_rfc");

        writer.startTag(tem, "k_nombre");
        writer.text(NombreCliente);
        writer.endTag(tem, "k_nombre");


        writer.startTag(tem, "k_calle");
        writer.text(Calle);
        writer.endTag(tem, "k_calle");


        writer.startTag(tem, "k_colonia");
        writer.text(Colonia);
        writer.endTag(tem, "k_colonia");


        writer.startTag(tem, "k_poblacion");
        writer.text(Poblacion);
        writer.endTag(tem, "k_poblacion");

        writer.startTag(tem, "k_desc1");
        writer.text(Desc1);
        writer.endTag(tem, "k_desc1");

        writer.startTag(tem, "k_agente");
        writer.text(ClaveVendedor);
        writer.endTag(tem, "k_agente");

        writer.startTag(tem, "k_moneda");
        writer.text("PESOS");
        writer.endTag(tem, "k_moneda");

        writer.startTag(tem, "k_paridad");
        writer.text("1");
        writer.endTag(tem, "k_paridad");

        writer.startTag(tem, "k_refer");
        writer.text("");
        writer.endTag(tem, "k_refer");

        writer.startTag(tem, "k_plazo");
        writer.text(plazo);
        writer.endTag(tem, "k_plazo");

        writer.startTag(tem, "k_vence");
        writer.text(FechaVencimiento);
        writer.endTag(tem, "k_vence");

        writer.startTag(tem, "k_cond");
        writer.text("");
        writer.endTag(tem, "k_cond");

        writer.startTag(tem, "k_coment");
        writer.text(Comentario);
        writer.endTag(tem, "k_coment");

        double Subtotal = 0;
        for (int i = 0; i < listasearch2.size(); i++) {
            precio = Double.valueOf(listasearch2.get(i).getPrecio());
            cantidad = Double.valueOf(listasearch2.get(i).getSurtido());
            descuento = Double.valueOf(listasearch2.get(i).getDesc());
            monto = precio * cantidad;
            monto2 = monto * (descuento / 100);
            monto3 = monto - monto2;

            Subtotal = Subtotal + monto3;

        }





        DescProstr=Double.parseDouble(Desc1)/100;
        Descuentorec = Subtotal*DescProstr;
        DescuentoStr = String.valueOf(Descuentorec);

        double Subtotal2;
        Subtotal2 =Subtotal-Descuentorec;


        double ivaCal;
        double MontoTotal;
        double IvaPorce=((!Server.equals("vazlocolombia.dyndns.org:9085"))? 0.16 : 0.19);

        ivaCal = Subtotal2 * IvaPorce;;
        MontoTotal = Subtotal2 + ivaCal;

        Iva = String.valueOf(ivaCal);
        Montototal = String.valueOf(MontoTotal);



        writer.startTag(tem, "k_desc");
        writer.text(formatNumberCurrency(DescuentoStr));
        writer.endTag(tem, "k_desc");


        writer.startTag(tem, "k_87");
        writer.text(DescuentoPro);
        writer.endTag(tem, "k_87");

        writer.startTag(tem, "k_39");
        writer.text(FolioPrev);
        writer.endTag(tem, "k_39");


        writer.startTag(tem, "k_38");
        writer.text("1");
        writer.endTag(tem, "k_38");


        writer.startTag(tem, "k_37");
        writer.text("35");
        writer.endTag(tem, "k_37");



        writer.startTag(tem, "k_36");
        writer.text("D");
        writer.endTag(tem, "k_36");



        writer.startTag(tem, "k_iva");
        writer.text(formatNumberCurrency(Iva));
        writer.endTag(tem, "k_iva");


        writer.startTag(tem, "k_monto");
        writer.text(formatNumberCurrency(Montototal));
        writer.endTag(tem, "k_monto");

        writer.startTag(tem, "k_70");
        writer.text(Via);
        writer.endTag(tem, "k_70");



        writer.startTag(tem, "k_74");
        writer.text(stridEnvio);
        writer.endTag(tem, "k_74");
        

        writer.startTag(tem, "k_81");
        writer.text("V");
        writer.endTag(tem, "k_81");

        writer.startTag(tem, "k_63");
        writer.text("UD4101-");
        writer.endTag(tem, "k_63");


        writer.startTag(tem, "k_97");
        writer.text("UD4101-");         //Eagle
        writer.endTag(tem, "k_97");

        writer.startTag(tem, "k_98");
        writer.text("UD4101-");         //Track
        writer.endTag(tem, "k_98");

        writer.startTag(tem, "k_99");
        writer.text("UD4101-");         //Rodatech
        writer.endTag(tem, "k_99");

        writer.startTag(tem, "k_100");
        writer.text("UD4101-");         //Partech
        writer.endTag(tem, "k_100");

        writer.startTag(tem, "k_101");
        writer.text("UD4101-");         //Shark
        writer.endTag(tem, "k_101");

        writer.startTag(tem, "k_102");
        writer.text("UD4101-");         //Descuento documento
        writer.endTag(tem, "k_102");





        /*Items*/
        writer.startTag(tem, "k_items");
        for (int i = 0; i < listasearch2.size(); i++) {

            writer.startTag(tem, "item");

            writer.startTag(tem, "k_parte");
            writer.text(listasearch2.get(i).getClaveP());
            writer.endTag(tem, "k_parte");

            writer.startTag(tem, "k_Q");
            writer.text(listasearch2.get(i).getSurtido());
            writer.endTag(tem, "k_Q");

            writer.startTag(tem, "k_desc1");
            writer.text(listasearch2.get(i).getDesc());
            writer.endTag(tem, "k_desc1");

            writer.startTag(tem, "k_descr");
            writer.text(listasearch2.get(i).getDescripcion());
            writer.endTag(tem, "k_descr");

            writer.startTag(tem, "k_unidad");
            writer.text(listasearch2.get(i).getUnidad());
            writer.endTag(tem, "k_unidad");

            writer.startTag(tem, "k_precio");
            writer.text(formatNumberCurrency(listasearch2.get(i).getPrecio()));
            writer.endTag(tem, "k_precio");



            writer.startTag(tem, "k_monto");
            writer.text(formatNumberCurrency(listasearch2.get(i).getPrecioNuevo()));
            writer.endTag(tem, "k_monto");



            writer.startTag(tem, "k_iva");
            writer.text(((!Server.equals("vazlocolombia.dyndns.org:9085")?"16" : "19")));
            writer.endTag(tem, "k_iva");

            writer.startTag(tem, "k_ieps");
            writer.text("0");
            writer.endTag(tem, "k_ieps");

            writer.startTag(tem, "k_sucursal");
            writer.text(Sucursal);
            writer.endTag(tem, "k_sucursal");

            writer.startTag(tem, "k_genero");
            writer.text("U");
            writer.endTag(tem, "k_genero");

            writer.startTag(tem, "k_natPrv");
            writer.text("D");
            writer.endTag(tem, "k_natPrv");

            writer.startTag(tem, "k_grpPrv");
            writer.text("35");
            writer.endTag(tem, "k_grpPrv");

            writer.startTag(tem, "k_tipPrv");
            writer.text("1");
            writer.endTag(tem, "k_tipPrv");

            writer.startTag(tem, "k_folPrv");
            writer.text(FolioPrev);
            writer.endTag(tem, "k_folPrv");

            writer.startTag(tem, "k_parPrv");
            writer.text(listasearch2.get(i).getPartida());
            writer.endTag(tem, "k_parPrv");

            writer.startTag(tem, "k_QO");
            writer.text(listasearch2.get(i).getCant());
            writer.endTag(tem, "k_QO");


            writer.endTag(tem, "item");

        }
        writer.endTag(tem, "k_items");
        writer.endTag(tem, "Doc");

        writer.endTag(tem, "newDocRequest");
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        writer.endDocument();

    }

}