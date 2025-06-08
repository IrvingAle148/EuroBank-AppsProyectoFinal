package modelo.persistencia;

import modelo.entidades.Cuenta;
import modelo.entidades.Cliente;
import modelo.entidades.Sucursal;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.time.LocalDate;

public class CuentaCSV {

    // Leer todas las cuentas desde el archivo CSV
    public List<Cuenta> cargar(String ruta) {
        List<Cuenta> cuentas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                // númeroCuenta, tipo, saldoActual, limiteCredito, rfcCliente, idSucursal
                if (datos.length >= 6) {
                    // OJO: Aquí necesitas mapear el cliente y la sucursal (puedes adaptar según tu arquitectura).
                    Cuenta cuenta = new Cuenta(
                            datos[0],                                 // número de cuenta
                            datos[1],                                 // tipo
                            Double.parseDouble(datos[2]),             // saldo actual
                            Double.parseDouble(datos[3]),             // límite de crédito
                            datos[4],                                 // rfcCliente (relacionar después)
                            datos[5]                                  // idSucursal (relacionar después)
                    );
                    cuentas.add(cuenta);
                }
            }
        } catch (IOException e) {
            // Si el archivo no existe, retorna lista vacía
        }
        return cuentas;
    }

    // Guardar una cuenta nueva
    public void guardarUno(Cuenta cuenta, String ruta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, true))) {
            bw.write(formatoCSV(cuenta));
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar la cuenta: " + e.getMessage());
        }
    }

    // Actualizar una cuenta existente (por número de cuenta)
    public void actualizar(Cuenta cuenta, String ruta) {
        List<Cuenta> cuentas = cargar(ruta);
        for (int i = 0; i < cuentas.size(); i++) {
            if (cuentas.get(i).getNumeroCuenta().equals(cuenta.getNumeroCuenta())) {
                cuentas.set(i, cuenta);
                break;
            }
        }
        guardarTodos(cuentas, ruta);
    }

    // Eliminar una cuenta existente
    public void eliminar(Cuenta cuenta, String ruta) {
        List<Cuenta> cuentas = cargar(ruta);
        cuentas.removeIf(c -> c.getNumeroCuenta().equals(cuenta.getNumeroCuenta()));
        guardarTodos(cuentas, ruta);
    }

    // Exportar todas las cuentas a una ruta dada
    public void exportarCuentasCSV(String rutaExportacion) {
        List<Cuenta> cuentas = cargar("src/main/resources/archivos/cuentas.csv");
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

    // Formato CSV para una cuenta (mismo orden que el constructor)
    private String formatoCSV(Cuenta cuenta) {
        return String.join(",",
                cuenta.getNumeroCuenta(),
                cuenta.getTipo(),
                String.valueOf(cuenta.getSaldoActual()),
                String.valueOf(cuenta.getLimiteCredito()),
                cuenta.getCliente() != null ? cuenta.getCliente().getRfcCurp() : cuenta.getRfcCliente(),
                cuenta.getSucursal() != null ? cuenta.getSucursal().getNumeroIdentificacion() : cuenta.getIdSucursal()
        );
    }
}
