package com.example.kepler201.Adapter;


import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.text.Html;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.AgendaSANDG;
import com.example.kepler201.SetterandGetter.Envio2SANDG;

import com.example.kepler201.activities.Agenda.ActivityAgenda;
import com.example.kepler201.includes.HttpHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;

@SuppressWarnings("deprecation")
public class AdapterAgenda extends RecyclerView.Adapter<AdapterAgenda.ViewHolderAgenda> {
    private ArrayList<AgendaSANDG> listaAgenda;
    private ArrayList<Envio2SANDG> listaUbicaciones;
    private Context context;
    private String strcode, StrServer, strusr, strpass;
    private HashMap<String, Boolean> proximidadClientes = new HashMap<>();
    private SharedPreferences visitasTerminadas;


    public AdapterAgenda(ArrayList<AgendaSANDG> listaAgenda, ArrayList<Envio2SANDG> listaUbicaciones,
                         Context context, String strcode, String StrServer, String strusr, String strpass) {
        this.listaAgenda = listaAgenda;
        this.listaUbicaciones = listaUbicaciones;
        this.context = context;
        this.strcode = strcode;
        this.StrServer = StrServer;
        this.strusr = strusr;
        this.strpass = strpass;
        this.visitasTerminadas = context.getSharedPreferences("visitas_terminadas", Context.MODE_PRIVATE);
    }

    public void actualizarProximidadClientes(HashMap<String, Boolean> proximidad) {
        this.proximidadClientes = proximidad;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderAgenda onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_agenda, parent, false);
        return new ViewHolderAgenda(view);
    }


    @Override
    public void onBindViewHolder(ViewHolderAgenda holder, int position) {
        AgendaSANDG agenda = listaAgenda.get(position);

        Log.d("ADAPTER_DEBUG", "Actualizando item: " + agenda.getCliente() +
                " - Proximidad: " + proximidadClientes.toString());



        String claveVisita = agenda.getCliente() + "_" + agenda.getFecha();
        boolean visitaTerminada = visitasTerminadas.getBoolean(claveVisita, false);
        boolean cerca = proximidadClientes.getOrDefault(agenda.getCliente(), false);
        boolean tieneUbicaciones = listaUbicaciones.stream()
                .anyMatch(u -> u.getId().equals(agenda.getCliente()));

        // Configuración de vistas
        holder.Fecha.setText(agenda.getFecha());
        holder.ClaveCl.setText(Html.fromHtml("Cliente: <font color='#000000'>" + agenda.getCliente() + "</font>"));
        holder.Nombre.setText(Html.fromHtml("Nombre: <font color='#000000'>" + agenda.getClienNom() + "</font>"));
        holder.Actividad.setText("Actividad: " + agenda.getActividad());
        holder.Estatus.setText("Estatus: " + agenda.getEstatus());
        holder.Comentario.setText("Comentario: " + agenda.getComentario());

    /*
        // ========== OPCIÓN 1: Botón visible solo cuando está cerca ==========
        holder.btnEmpezar.setVisibility(
                !visitaTerminada &&
                        "PENDIENTE".equalsIgnoreCase(agenda.getEstatus()) &&
                        tieneUbicaciones &&
                        cerca ? View.VISIBLE : View.GONE
        );

     */

        // ========== OPCIÓN 2: Botón siempre visible pero con validación ==========

        holder.btnEmpezar.setVisibility(
                !visitaTerminada &&
                        "PENDIENTE".equalsIgnoreCase(agenda.getEstatus()) ?
                        View.VISIBLE : View.GONE
        );
        holder.btnEmpezar.setEnabled(true);
        holder.btnEmpezar.setAlpha(1.0f);

        holder.btnTerminar.setVisibility(
                !visitaTerminada &&
                        "EN PROCESO".equalsIgnoreCase(agenda.getEstatus()) ?
                        View.VISIBLE : View.GONE
        );


        holder.txtEstado.setVisibility(visitaTerminada ? View.VISIBLE : View.GONE);
        holder.txtEstado.setText(visitaTerminada ? "VISITA COMPLETADA" : "");

        // Listeners
        holder.btnEmpezar.setOnClickListener(v -> {
            // Obtener ubicación actual
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permiso de ubicación no otorgado", Toast.LENGTH_SHORT).show();
                return;
            }

            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    boolean estaCerca = false;

                    for (Envio2SANDG ubicacion : listaUbicaciones) {
                        if (!ubicacion.getId().equals(agenda.getCliente())) continue;

                        String latStr = ubicacion.getLatitud();
                        String lonStr = ubicacion.getLongitud();

                        if (latStr == null || lonStr == null || latStr.trim().isEmpty() || lonStr.trim().isEmpty()) continue;

                        double lat = Double.parseDouble(latStr);
                        double lon = Double.parseDouble(lonStr);

                        float[] result = new float[1];
                        Location.distanceBetween(
                                location.getLatitude(), location.getLongitude(),
                                lat, lon,
                                result
                        );

                        if (result[0] <= 15.0) {
                            estaCerca = true;
                            break;
                        }
                    }

                    if (estaCerca) {
                        cambiarEstado(agenda.getCliente(), position, "EN PROCESO");
                    } else {
                        Toast.makeText(context, "Debes estar a menos de 15 metros de una ubicación del cliente.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "No se pudo obtener ubicación actual", Toast.LENGTH_SHORT).show();
                }
            });
        });


        holder.btnTerminar.setOnClickListener(v -> {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permiso de ubicación no otorgado", Toast.LENGTH_SHORT).show();
                return;
            }

            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    boolean estaCerca = false;

                    for (Envio2SANDG ubicacion : listaUbicaciones) {
                        if (!ubicacion.getId().equals(agenda.getCliente())) continue;

                        String latStr = ubicacion.getLatitud();
                        String lonStr = ubicacion.getLongitud();

                        if (latStr == null || lonStr == null || latStr.trim().isEmpty() || lonStr.trim().isEmpty()) continue;

                        double lat = Double.parseDouble(latStr);
                        double lon = Double.parseDouble(lonStr);

                        float[] result = new float[1];
                        Location.distanceBetween(
                                location.getLatitude(), location.getLongitude(),
                                lat, lon,
                                result
                        );

                        if (result[0] <= 15.0f) {
                            estaCerca = true;
                            break;
                        }
                    }

                    if (estaCerca) {
                        // Mostrar AlertDialog con campo para comentario
                        EditText input = new EditText(context);
                        input.setHint("Escribe un comentario...");

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Finalizar visita");
                        builder.setMessage("¿Deseas marcar esta visita como completada?");
                        builder.setView(input);
                        builder.setPositiveButton("Sí", (dialog, which) -> {
                            String comentario = input.getText().toString().trim();
                            visitasTerminadas.edit().putBoolean(claveVisita, true).apply();

                            // Enviar el comentario como parte del estado final
                            cambiarEstadoConComentario(agenda.getCliente(), position, "TERMINADO", comentario);
                        });
                        builder.setNegativeButton("No", null);
                        builder.show();
                    } else {
                        Toast.makeText(context, "Debes estar a menos de 15 metros de una ubicación del cliente para finalizar la visita.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "No se pudo obtener ubicación actual", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }

    private void cambiarEstadoConComentario(String clienteId, int position, String nuevoEstado, String comentario) {
        ActivityAgenda.EnviarStatusTask.setParametros(
                clienteId,
                listaAgenda.get(position).getFecha(),
                listaAgenda.get(position).getActividad(),
                listaAgenda.get(position).getEstatus(),
                strcode,
                StrServer,
                strusr,
                strpass
        );

        new ActivityAgenda.EnviarStatusTask(nuevoEstado, context, true) {
            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    listaAgenda.get(position).setEstatus(nuevoEstado);
                    listaAgenda.get(position).setComentario(comentario);
                    notifyItemChanged(position);
                }
            }

            @Override
            protected Boolean doInBackground(Void... voids) {

                HttpHandler sh = new HttpHandler();
                String parametros = "vendedor=" + strcode +
                        "&fecha=" + listaAgenda.get(position).getFecha() +
                        "&cliente=" + clienteId +
                        "&actividad=" + listaAgenda.get(position).getActividad() +
                        "&status=" + listaAgenda.get(position).getEstatus() +
                        "&statuscambio=" + nuevoEstado +
                        "&comentario=" + URLEncoder.encode(comentario);

                String url = "http://" + StrServer + "/statusagendaapp?" + parametros;
                String response = sh.makeServiceCall(url, strusr, strpass);
                return response != null && response.contains("Los Cambios se Realizaron Correctamente");
            }
        }.execute();
    }

    private void cambiarEstado(String clienteId, int position, String nuevoEstado) {
        ActivityAgenda.EnviarStatusTask.setParametros(
                clienteId,
                listaAgenda.get(position).getFecha(),
                listaAgenda.get(position).getActividad(),
                listaAgenda.get(position).getEstatus(),
                strcode,
                StrServer,
                strusr,
                strpass
        );

        new ActivityAgenda.EnviarStatusTask(nuevoEstado, context, true) {
            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    listaAgenda.get(position).setEstatus(nuevoEstado);
                    notifyItemChanged(position);
                }
            }
        }.execute();
    }

    @Override
    public int getItemCount() {
        return listaAgenda.size();
    }

    public static class ViewHolderAgenda extends RecyclerView.ViewHolder {
        TextView Fecha, ClaveCl, Nombre, Actividad, Estatus, txtEstado,Comentario;
        Button btnEmpezar, btnTerminar;

        public ViewHolderAgenda(View itemView) {
            super(itemView);
            Fecha = itemView.findViewById(R.id.Fecha);
            ClaveCl = itemView.findViewById(R.id.cCliente);
            Nombre = itemView.findViewById(R.id.Nombre);
            Actividad = itemView.findViewById(R.id.Actividad);
            Estatus = itemView.findViewById(R.id.Estatus);
            btnEmpezar = itemView.findViewById(R.id.btnEmpezar);
            btnTerminar = itemView.findViewById(R.id.btnTerminar);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            Comentario = itemView.findViewById(R.id.Comentario);
        }
    }
    public void actualizarUbicaciones(ArrayList<Envio2SANDG> nuevasUbicaciones) {
        this.listaUbicaciones = nuevasUbicaciones;
        notifyDataSetChanged(); // Esto fuerza la actualización de todas las vistas
    }

    }



