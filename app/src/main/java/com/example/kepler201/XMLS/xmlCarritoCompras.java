package com.example.kepler201.XMLS;

import com.example.kepler201.SetterandGetter.CarritoBD;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class xmlCarritoCompras extends SoapSerializationEnvelope {
    String Comentario="";
    String ClaveVendedor="";
    String NombreCliente ="";
    String ClaveCliente ="";
    String FechaActual = "";
    String FechaVencimiento = "";
    String Sucursal="";
    String usuario = "";
    String clave = "";
    String rfcCliente="";
    String plazo="";
    String Montototal ="";
    String Iva ="";
    String Descuento = "" ;
    String DescuentoPro = "" ;
    String Desc1 = "";
    String Calle = "";
    String Colonia = "";
    String Poblacion = "";
    String Server ;
    String Clave;
    ArrayList<CarritoBD> listaCarShoping = new ArrayList<>();
    String descuEagle,descuRodatech,descuPartec, descuShark, descuTrackone,Comentario1="",Comentario2="",Comentario3="";



    public xmlCarritoCompras(int version) {
        super(version);
    }



    public void xmlCarritoCompras(String comentario, String claveVendedor, String nombreCliente, String claveCliente, String fechaActual,
                                  String VechaFencimiento, String sucursal, String usuario, String clave, String rfcCliente, String plazo,
                                  String montototal, String iva, String Descuento, String DescuentoPro, String Desc1, String Calle , String Colonia,
                                  String Poblacion , ArrayList<CarritoBD> listaCarShoping, String StrServer, String Clave, String descuEagle, String descuRodatech,
                                  String descuPartec, String descuShark, String descuTrackone) {

        this.Comentario = comentario;
        this.ClaveVendedor = claveVendedor;
        this.NombreCliente = nombreCliente;
        this.ClaveCliente = claveCliente;
        this.FechaActual = fechaActual;
        this.FechaVencimiento=VechaFencimiento;
        this.Sucursal = sucursal;
        this.usuario = usuario;
        this.clave = clave;
        this.rfcCliente = rfcCliente;
        this.plazo =plazo;
        this.Montototal = montototal;
        this.Iva = iva;
        this.Descuento =Descuento;
        this.DescuentoPro =DescuentoPro;
        this.Desc1 =Desc1;
        this.Calle = Calle;
        this.Colonia = Colonia;
        this.Poblacion = Poblacion;
        this.listaCarShoping = listaCarShoping;
        this.Server=StrServer;
        this.Clave=Clave;
        this.descuEagle = descuEagle;
        this.descuRodatech = descuRodatech;
        this.descuPartec = descuPartec;
        this.descuShark = descuShark;
        this.descuTrackone = descuTrackone;
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
        writer.text("35");
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
        writer.text("0");
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

        int tamaño=Comentario.length();
        if (tamaño <=60) {
            if (tamaño == 60) {
                Comentario1 = Comentario.substring(0, 59);
            } else {
                Comentario1 = Comentario.substring(0, tamaño);
            }
        }else if(tamaño <=120 && tamaño > 60){
            if (tamaño == 120) {
                Comentario1 = Comentario.substring(0, 59);
                Comentario2 = Comentario.substring(60, 119);
            } else {
                Comentario1 = Comentario.substring(0,59);
                Comentario2 = Comentario.substring(60, tamaño);
            }
        }else if (tamaño <=180 && tamaño > 120){
            if (tamaño == 180) {
                Comentario1 = Comentario.substring(0, 59);
                Comentario2 = Comentario.substring(60, 119);
                Comentario3 = Comentario.substring(120, 179);
            } else {
                Comentario1 = Comentario.substring(0,60);
                Comentario2 = Comentario.substring(61, 120);
                Comentario2 = Comentario.substring(61,tamaño);
            }
        }else{
            if (tamaño == 180) {
                Comentario1 = Comentario.substring(0, 59);
                Comentario2 = Comentario.substring(60, 119);
                Comentario3 = Comentario.substring(120, 179);
            } else {
                Comentario1 = Comentario.substring(0,59);
                Comentario2 = Comentario.substring(60, 119);
                Comentario3 = Comentario.substring(120,tamaño);
            }
        }






        writer.startTag(tem, "k_coment");
        writer.text(Comentario1);
        writer.endTag(tem, "k_coment");

        writer.startTag(tem, "k_desc");
        writer.text("0");
        writer.endTag(tem, "k_desc");

        writer.startTag(tem, "k_87");
        writer.text(DescuentoPro);
        writer.endTag(tem, "k_87");

        writer.startTag(tem, "k_63");
        writer.text("UD3501-");
        writer.endTag(tem, "k_63");

        writer.startTag(tem, "k_97");
        writer.text(descuEagle);         //Eagle
        writer.endTag(tem, "k_97");

        writer.startTag(tem, "k_98");
        writer.text(descuTrackone);         //Track
        writer.endTag(tem, "k_98");

        writer.startTag(tem, "k_99");
        writer.text(descuRodatech);         //Rodatech
        writer.endTag(tem, "k_99");

        writer.startTag(tem, "k_100");
        writer.text(descuRodatech);         //Partech
        writer.endTag(tem, "k_100");

        writer.startTag(tem, "k_101");
        writer.text(descuShark);         //Shark
        writer.endTag(tem, "k_101");

        writer.startTag(tem, "k_102");
        writer.text(Desc1);         //Descuento documento
        writer.endTag(tem, "k_102");



        writer.startTag(tem, "k_iva");
        writer.text(formatNumberCurrency(Iva));
        writer.endTag(tem, "k_iva");

        writer.startTag(tem, "k_monto");
        writer.text(formatNumberCurrency(Montototal));
        writer.endTag(tem, "k_monto");
        writer.startTag(tem, "k_25");
        writer.text(Comentario2);
        writer.endTag(tem, "k_25");

        writer.startTag(tem, "k_26");
        writer.text(Comentario3);
        writer.endTag(tem, "k_26");


        /*Items*/
        writer.startTag(tem, "k_items");




        for (int i = 0; i<listaCarShoping.size();i++){



            writer.startTag(tem, "item");

            writer.startTag(tem, "k_parte");
            writer.text(listaCarShoping.get(i).getParte());
            writer.endTag(tem, "k_parte");

            writer.startTag(tem, "k_Q");
            writer.text(listaCarShoping.get(i).getCantidad());
            writer.endTag(tem, "k_Q");

            writer.startTag(tem, "k_desc1");
            writer.text("0");
            writer.endTag(tem, "k_desc1");

            writer.startTag(tem, "k_descr");
            writer.text(listaCarShoping.get(i).getDescr());
            writer.endTag(tem, "k_descr");

            writer.startTag(tem, "k_unidad");
            writer.text(listaCarShoping.get(i).getUnidad());
            writer.endTag(tem, "k_unidad");

            if(!Clave.equals("1")){
                double Desc1P;
                double Desc1D;
                double Precio;
                double NuevoImporte;

                Precio=Double.parseDouble(listaCarShoping.get(i).getPrecio());
                Desc1P =Double.parseDouble(listaCarShoping.get(i).getDesc1());
                Desc1D=Double.parseDouble(Desc1);


                Precio = (Precio-((Precio*Desc1P)/100));

                Precio =(Precio-((Precio*Desc1D)/100));


                writer.startTag(tem, "k_precio");
                writer.text(formatNumberCurrency(String.valueOf(Precio)));
                writer.endTag(tem, "k_precio");

                NuevoImporte = Precio * Integer.parseInt(listaCarShoping.get(i).getCantidad());

                writer.startTag(tem, "k_monto");
                writer.text(formatNumberCurrency(String.valueOf(NuevoImporte)));
                writer.endTag(tem, "k_monto");



                /*double Precio;
                double NuevoImporte;
                writer.startTag(tem, "k_precio");
                writer.text(formatNumberCurrency(listaCarShoping.get(i).getPrecio()));
                writer.endTag(tem, "k_precio");

                Precio=Double.parseDouble(listaCarShoping.get(i).getPrecio());
                NuevoImporte = Precio * Integer.parseInt(listaCarShoping.get(i).getCantidad());

                writer.startTag(tem, "k_monto");
                writer.text(formatNumberCurrency(String.valueOf(NuevoImporte)));
                writer.endTag(tem, "k_monto");*/


            }else{
                double Desc1P;
                double Desc1D;
                double Precio;
                double NuevoImporte;

                Precio=Double.parseDouble(listaCarShoping.get(i).getPrecio());
                Desc1P =Double.parseDouble(listaCarShoping.get(i).getDesc1());
                Desc1D=Double.parseDouble(Desc1);


                Precio = (Precio-((Precio*Desc1P)/100));

                Precio =(Precio-((Precio*Desc1D)/100));


                writer.startTag(tem, "k_precio");
                writer.text(formatNumberCurrency(String.valueOf(Precio)));
                writer.endTag(tem, "k_precio");

                NuevoImporte = Precio * Integer.parseInt(listaCarShoping.get(i).getCantidad());

                writer.startTag(tem, "k_monto");
                writer.text(formatNumberCurrency(String.valueOf(NuevoImporte)));
                writer.endTag(tem, "k_monto");


            }


            writer.startTag(tem, "k_iva");
            writer.text((!Server.equals("vazlocolombia.dyndns.org:9085")?"16" : "19"));
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
