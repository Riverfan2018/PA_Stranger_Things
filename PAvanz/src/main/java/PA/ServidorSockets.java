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
            try (DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                String comando;
                while (true) {
                    comando = in.readUTF();
                    String respuesta = procesarComando(comando);
                    out.writeUTF(respuesta);
                    out.flush();
                    if (comando.equalsIgnoreCase("SALIR")) break;
                }
            } catch (IOException e) {
                System.err.println("Error con cliente: " + e.getMessage());
            }
        }
        
        private String procesarComando(String comando) {
            switch (comando.toUpperCase()) {
                case "RANKING":
                    return generarRanking();
                case "EVENTO":
                    return generarEvento();
                case "NINOS_HAWKINS":
                    return String.valueOf(
                        sistema.callePrincipal.getCantidadNinos() +
                        sistema.sotanoByers.getCantidadNinos() +
                        sistema.radioWSQK.getCantidadNinos()
                    );
                case "PORTALES":
                    int bosque = sistema.portalBosque.getCantidadEsperandoSalida() + sistema.portalBosque.getCantidadEsperandoRegreso();
                    int lab = sistema.portalLaboratorio.getCantidadEsperandoSalida() + sistema.portalLaboratorio.getCantidadEsperandoRegreso();
                    int centro = sistema.portalCentroComercial.getCantidadEsperandoSalida() + sistema.portalCentroComercial.getCantidadEsperandoRegreso();
                    int alcan = sistema.portalAlcantarillado.getCantidadEsperandoSalida() + sistema.portalAlcantarillado.getCantidadEsperandoRegreso();
                    return String.format("Bosque:%d | Laboratorio:%d | Centro:%d | Alcantarillado:%d", bosque, lab, centro, alcan);
                case "UPSIDE_DOWN":
                    int ninosBosque = sistema.bosque.getCantidadNinos();
                    int ninosLab = sistema.laboratorio.getCantidadNinos();
                    int ninosCentro = sistema.centroComercial.getCantidadNinos();
                    int ninosAlcan = sistema.alcantarillado.getCantidadNinos();
                    int ninosColmena = sistema.colmena.getCantidadNinos();

                    int demosBosque = contarDemogorgonsEn(sistema.bosque, sistema);
                    int demosLab = contarDemogorgonsEn(sistema.laboratorio, sistema);
                    int demosCentro = contarDemogorgonsEn(sistema.centroComercial, sistema);
                    int demosAlcan = contarDemogorgonsEn(sistema.alcantarillado, sistema);

                    return String.format("%d|%d|%d|%d|%d|%d|%d|%d|%d", 
                        ninosBosque, ninosLab, ninosCentro, ninosAlcan, ninosColmena,
                        demosBosque, demosLab, demosCentro, demosAlcan);
                case "PAUSAR":
                    sistema.pausar();
                    return "SISTEMA PAUSADO";
                case "REANUDAR":
                    sistema.reanudar();
                    return "SISTEMA REANUDADO";
                case "ESTA_PAUSADO":
                    return sistema.isPausado() ? "PAUSADO" : "EJECUTANDO";
                default:
                    return "COMANDO DESCONOCIDO. Comandos: RANKING, EVENTO, NINOS_HAWKINS, PORTALES, UPSIDE_DOWN, PAUSAR, REANUDAR, SALIR";
            }
        }
        
        private String generarRanking() {
            List<Demogorgon> ranking = new ArrayList<>(sistema.getDemogorgons());
            ranking.sort((a, b) -> Integer.compare(b.getContadorCapturas(), a.getContadorCapturas()));

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Math.min(3, ranking.size()); i++) {
                Demogorgon d = ranking.get(i);
                if (i > 0) sb.append(" | ");
                sb.append(d.getDemogorgonId()).append("(").append(d.getContadorCapturas()).append(")");
            }
            return ranking.isEmpty() ? "---" : sb.toString();
        }
        
        private String generarEvento() {
            StringBuilder sb = new StringBuilder();

            if (sistema.hayApagon) {
                sb.append("APAGÓN|").append(sistema.getTiempoRestanteEvento());
            } else if (sistema.tormentaon()) {
                sb.append("TORMENTA|").append(sistema.getTiempoRestanteEvento());
            } else if (sistema.eleveen_enfadada) {
                sb.append("ELEVEN|").append(sistema.getTiempoRestanteEvento());
            } else if (sistema.red_mental_on) {
                sb.append("RED_MENTAL|").append(sistema.getTiempoRestanteEvento());
            } else {
                long tiempoProximoEvento = sistema.getTiempoProximoEvento();
                sb.append("NINGUNO|").append(tiempoProximoEvento);
            }

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