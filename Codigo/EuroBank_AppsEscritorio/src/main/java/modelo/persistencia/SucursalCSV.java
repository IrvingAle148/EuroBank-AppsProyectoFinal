package modelo.persistencia;

import modelo.entidades.Sucursal;
import modelo.entidades.Cuenta;
import modelo.entidades.Empleado;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SucursalCSV {

    public static void guardar(List<Sucursal> sucursales, String rutaArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            for (Sucursal s : sucursales) {
                writer.println(String.join(",",
                        s.getId(),
                        s.getNombre(),
                        s.getDireccion(),
                        s.getTelefono(),
                        s.getCorreo(),
                        s.getNombreGerente(),
                        s.getPersonaContacto()
                ));
            }
        } catch (IOException e) {
            System.err.println("Error guardando sucursales: " + e.getMessage());
        }
    }

    public static List<Sucursal> cargar(String rutaArchivo) {
        List<Sucursal> sucursales = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 7) {
                    Sucursal sucursal = new Sucursal(
                            datos[0], datos[1], datos[2], datos[3],
                            datos[4], datos[5], datos[6]
                    );
                    sucursales.add(sucursal);
                }
            }
        } catch (IOException e) {
            System.err.println("Error cargando sucursales: " + e.getMessage());
        }

        return sucursales;
    }
}