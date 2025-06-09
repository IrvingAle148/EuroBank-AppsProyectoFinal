package modelo.persistencia;

import modelo.entidades.Cuenta;
import modelo.entidades.Cliente;
import modelo.entidades.Sucursal;
import java.io.*;
import java.util.*;

public class CuentaCSV {

    // Cargar cuentas desde el archivo CSV
    public List<Cuenta> cargar(String ruta, Map<String, Cliente> clientes, Map<String, Sucursal> sucursales) {
        List<Cuenta> cuentas = new ArrayList<>();
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            try {
                archivo.getParentFile().mkdirs();
                archivo.createNewFile();
                System.out.println("Archivo de cuentas creado vacío en: " + ruta);
            } catch (IOException e) {
                System.err.println("No se pudo crear el archivo de cuentas: " + e.getMessage());
                return cuentas;
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty() || linea.trim().startsWith("#")) continue;
                String[] datos = linea.split(",");
                if (datos.length >= 6) {
                    String numeroCuenta = datos[0].trim();
                    String tipo = datos[1].trim();
                    double saldoActual = Double.parseDouble(datos[2].trim());
                    double limiteCredito = Double.parseDouble(datos[3].trim());
                    String rfcCliente = datos[4].trim();
                    String idSucursal = datos[5].trim();

                    Cliente cliente = clientes.get(rfcCliente);
                    if (cliente == null && !rfcCliente.isEmpty()) {
                        System.out.println("ADVERTENCIA: No se encontró cliente con RFC " + rfcCliente + " para la cuenta " + numeroCuenta);
                    }
                    Sucursal sucursal = sucursales.get(idSucursal);
                    if (sucursal == null && !idSucursal.isEmpty()) {
                        System.out.println("ADVERTENCIA: No se encontró sucursal con ID " + idSucursal + " para la cuenta " + numeroCuenta);
                    }

                    Cuenta cuenta = new Cuenta(numeroCuenta, tipo, saldoActual, limiteCredito, cliente, sucursal);
                    cuentas.add(cuenta);
                } else {
                    System.err.println("Línea mal formateada en cuentas.csv: " + linea);
                }
            }
            System.out.println("Cargadas " + cuentas.size() + " cuentas desde " + ruta);
        } catch (Exception e) {
            System.err.println("Error leyendo cuentas: " + e.getMessage());
        }
        return cuentas;
    }

    // Guardar una cuenta nueva
    public void guardarUno(Cuenta cuenta, String ruta) {
        asegurarArchivoExiste(ruta);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, true))) {
            bw.write(formatoCSV(cuenta));
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar la cuenta: " + e.getMessage());
        }
    }

    // Actualizar una cuenta existente (por número de cuenta)
    public void actualizar(Cuenta cuenta, String ruta, Map<String, Cliente> clientes, Map<String, Sucursal> sucursales) {
        List<Cuenta> cuentas = cargar(ruta, clientes, sucursales);
        for (int i = 0; i < cuentas.size(); i++) {
            if (cuentas.get(i).getNumeroCuenta().equals(cuenta.getNumeroCuenta())) {
                cuentas.set(i, cuenta);
                break;
            }
        }
        guardarTodos(cuentas, ruta);
    }

    // Eliminar una cuenta existente
    public void eliminar(Cuenta cuenta, String ruta, Map<String, Cliente> clientes, Map<String, Sucursal> sucursales) {
        List<Cuenta> cuentas = cargar(ruta, clientes, sucursales);
        cuentas.removeIf(c -> c.getNumeroCuenta().equals(cuenta.getNumeroCuenta()));
        guardarTodos(cuentas, ruta);
    }

    // Exportar todas las cuentas a una ruta dada
    public void exportarCuentasCSV(String rutaExportacion, String rutaOriginal, Map<String, Cliente> clientes, Map<String, Sucursal> sucursales) {
        List<Cuenta> cuentas = cargar(rutaOriginal, clientes, sucursales);
        guardarTodos(cuentas, rutaExportacion);
    }

    // Guardar todas las cuentas en el archivo (sobrescribe)
    private void guardarTodos(List<Cuenta> cuentas, String ruta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Cuenta cuenta : cuentas) {
                bw.write(formatoCSV(cuenta));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el archivo de cuentas: " + e.getMessage());
        }
    }

    // Formato CSV para una cuenta
    private String formatoCSV(Cuenta cuenta) {
        String rfcCliente = cuenta.getCliente() != null ? cuenta.getCliente().getRfcCurp() : "";
        String idSucursal = cuenta.getSucursal() != null ? cuenta.getSucursal().getNumeroIdentificacion() : "";
        return String.join(",",
                cuenta.getNumeroCuenta(),
                cuenta.getTipo(),
                String.valueOf(cuenta.getSaldoActual()),
                String.valueOf(cuenta.getLimiteCredito()),
                rfcCliente,
                idSucursal
        );
    }

    // Método utilitario para crear archivo si no existe
    private void asegurarArchivoExiste(String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            try {
                archivo.getParentFile().mkdirs();
                archivo.createNewFile();
                System.out.println("Archivo de cuentas creado en: " + rutaArchivo);
            } catch (IOException e) {
                throw new RuntimeException("No se pudo crear el archivo: " + rutaArchivo);
            }
        }
    }
}
