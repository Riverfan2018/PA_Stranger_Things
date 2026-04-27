package PA;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static Logger instancia;
    private PrintWriter writer;
    private final Object lock = new Object();
    
    private Logger() {
        try {
            writer = new PrintWriter(new FileWriter("hawkins.txt", true));
        } catch (IOException e) {
            System.err.println("Error al abrir el archivo de log: " + e.getMessage());
        }
    }
    
    public static Logger getInstance() {
        if (instancia == null) {
            instancia = new Logger();
        }
        return instancia;
    } // Para este hay que complicar el metodo :v
    
    public void log(String mensaje) {
        synchronized (lock) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
            String linea = timestamp + " - " + mensaje;
            System.out.println(linea);
            if (writer != null) {
                writer.println(linea);
                writer.flush();
            }
        }
    }
    
    public void cerrar() {
        synchronized (lock) {
            if (writer != null) {
                writer.close();
            }
        }
    }
}