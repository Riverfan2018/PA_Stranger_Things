package PA;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServidorSockets {
    private final Sistemageneral sistema;
    private ServerSocket serverSocket;
    private boolean ejecutando = true;
    
    public ServidorSockets(Sistemageneral sistema, int puerto) throws IOException {
        this.sistema = sistema;
        this.serverSocket = new ServerSocket(puerto);
        System.out.println("Servidor Socket iniciado en puerto " + puerto);
    }
    
    public void iniciar() {
        new Thread(() -> {
            while (ejecutando) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    new ManejadorCliente(clientSocket, sistema).start();
                } catch (IOException e) {
                    if (ejecutando) e.printStackTrace();
                }
            }
        }).start();
    }
    
    public void detener() {
        ejecutando = false;
        try {
            serverSocket.close();
        } catch (IOException e) {}
    }
    
    // Clase interna para manejar cada cliente
    private static class ManejadorCliente extends Thread {
        private final Socket socket;
        private final Sistemageneral sistema;
        
        public ManejadorCliente(Socket socket, Sistemageneral sistema) {
            this.socket = socket;
            this.sistema = sistema;
        }
        
        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                
                String comando;
                while ((comando = in.readLine()) != null) {
                    String respuesta = procesarComando(comando);
                    out.println(respuesta);
                    if (comando.equalsIgnoreCase("SALIR")) break;
                }
            } catch (IOException e) {
                System.err.println("Error con cliente: " + e.getMessage());
            }
        }
        
        private String procesarComando(String comando) {
            switch (comando.toUpperCase()) {
                case "ESTADO":
                    return generarEstado();
                case "PAUSAR":
                    sistema.pausar();
                    return "SISTEMA PAUSADO";
                case "REANUDAR":
                    sistema.reanudar();
                    return "SISTEMA REANUDADO";
                default:
                    return "COMANDO DESCONOCIDO. Comandos: ESTADO, PAUSAR, REANUDAR, SALIR";
            }
        }
        
        private String generarEstado() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== ESTADO HAWKINS ===\n");
            
            // Niños en Hawkins
            int ninosHawkins = sistema.callePrincipal.getCantidadNinos() + 
                               sistema.sotanoByers.getCantidadNinos() + 
                               sistema.radioWSQK.getCantidadNinos();
            sb.append("Niños en Hawkins: ").append(ninosHawkins).append("\n");
            sb.append("Niños en Colmena: ").append(sistema.colmena.getCantidadNinos()).append("\n");
            sb.append("Sangre recolectada: ").append(sistema.get_sangre_recolectada()).append("\n");
            
            // Portales
            sb.append("\n--- PORTALES ---\n");
            sb.append("Bosque: ").append(sistema.portalBosque.getCantidadEsperandoSalida()).append(" salida, ")
              .append(sistema.portalBosque.getCantidadEsperandoRegreso()).append(" regreso\n");
            sb.append("Laboratorio: ").append(sistema.portalLaboratorio.getCantidadEsperandoSalida()).append(" salida, ")
              .append(sistema.portalLaboratorio.getCantidadEsperandoRegreso()).append(" regreso\n");
            sb.append("Centro: ").append(sistema.portalCentroComercial.getCantidadEsperandoSalida()).append(" salida, ")
              .append(sistema.portalCentroComercial.getCantidadEsperandoRegreso()).append(" regreso\n");
            sb.append("Alcantarillado: ").append(sistema.portalAlcantarillado.getCantidadEsperandoSalida()).append(" salida, ")
              .append(sistema.portalAlcantarillado.getCantidadEsperandoRegreso()).append(" regreso\n");
            
            // UpsideDown
            sb.append("\n--- UPSIDE DOWN ---\n");
            sb.append("Bosque: ").append(sistema.bosque.getCantidadNinos()).append(" niños, ")
              .append(contarDemogorgonsEn(sistema.bosque, sistema)).append(" demogorgons\n");
            sb.append("Laboratorio: ").append(sistema.laboratorio.getCantidadNinos()).append(" niños, ")
              .append(contarDemogorgonsEn(sistema.laboratorio, sistema)).append(" demogorgons\n");
            sb.append("Centro: ").append(sistema.centroComercial.getCantidadNinos()).append(" niños, ")
              .append(contarDemogorgonsEn(sistema.centroComercial, sistema)).append(" demogorgons\n");
            sb.append("Alcantarillado: ").append(sistema.alcantarillado.getCantidadNinos()).append(" niños, ")
              .append(contarDemogorgonsEn(sistema.alcantarillado, sistema)).append(" demogorgons\n");
            
            // Evento
            sb.append("\n--- EVENTO ---\n");
            if (sistema.hayApagon) sb.append("APAGÓN activo\n");
            else if (sistema.tormentaon()) sb.append("TORMENTA activa\n");
            else if (sistema.eleveen_enfadada) sb.append("ELEVEN activa\n");
            else if (sistema.red_mental_on) sb.append("RED MENTAL activa\n");
            else sb.append("Sin evento activo\n");
            
            // Ranking Top 3
            sb.append("\n--- TOP 3 DEMOGORGONS ---\n");
            List<Demogorgon> ranking = new ArrayList<>(sistema.getDemogorgons());
            ranking.sort((a, b) -> Integer.compare(b.getContadorCapturas(), a.getContadorCapturas()));
            for (int i = 0; i < Math.min(3, ranking.size()); i++) {
                sb.append((i+1)).append(". ")
                  .append(ranking.get(i).getDemogorgonId())
                  .append(" - ")
                  .append(ranking.get(i).getContadorCapturas())
                  .append(" capturas\n");
            }
            
            // Estado de pausa
            sb.append("\n--- SISTEMA ---\n");
            sb.append(sistema.isPausado() ? "PAUSADO" : "EJECUTANDO").append("\n");
            
            return sb.toString();
        }
        
        private int contarDemogorgonsEn(Zona zona, Sistemageneral sistema) {
            int count = 0;
            for (Demogorgon d : sistema.getDemogorgons()) {
                if (d.getZonaActual() == zona) count++;
            }
            return count;
        }
    }
}