package PA;

import java.io.*;
import java.net.*;

public class ModuloRemoto extends javax.swing.JFrame {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private final String servidorIP = "localhost";
    private final int puerto = 8080;
    private final Object socketLock = new Object();
    
    public ModuloRemoto() {
        initComponents();
        conectar();
        iniciarActualizador();
        
        setSize(1035, 670);
        setMinimumSize(new java.awt.Dimension(1024, 640));
    }
    
    private void conectar() {
        try {
            socket = new Socket(servidorIP, puerto);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            System.out.println("Conectado al servidor");
            // Consultar el estado actual de pausa
            out.writeUTF("ESTA_PAUSADO");
            out.flush();
            String respuesta = in.readUTF();
            pausado = respuesta.equals("PAUSADO");
            // Actualizar el texto del botón según el estado real
            if (pausado) {
                btnPausa.setText("Reanudar");
            } else {
                btnPausa.setText("Pausar");
            }
        } catch (IOException e) {
            System.out.println("ERROR: No se pudo conectar al servidor");
        }
    }
    
    private void iniciarActualizador() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    actualizarTodo();
                } catch (InterruptedException e) {}
            }
        }).start();
    }
    
    private void actualizarTodo() {
        synchronized (socketLock) {
            if (out == null) return;
            
            // Verificar estado de pausa del servidor
            try {
                out.writeUTF("ESTA_PAUSADO");
                out.flush();
                String respuesta = in.readUTF();
                boolean estadoServidor = respuesta.equals("PAUSADO");
                if (estadoServidor != pausado) {
                    pausado = estadoServidor;
                    if (pausado) {
                        btnPausa.setText("Reanudar");
                    } else {
                        btnPausa.setText("Pausar");
                    }
                }
            } catch (IOException e) {
                // Error, ignorar
            }
        
            DemogorgonDelMes();
            actualizarEvento();
            actualizarHawkins();
            actualizarPortales();
            actualizarUpsideDown();
        }
    }
    
    private boolean pausado = false;
    
    private void togglePausa() {
        if (out != null) {
            try {
                if (pausado) {
                    out.writeUTF("REANUDAR");
                    btnPausa.setText("Pausar");
                } else {
                    out.writeUTF("PAUSAR");
                    btnPausa.setText("Reanudar");
                }
                out.flush();
                String respuesta = in.readUTF();
                System.out.println("Respuesta: " + respuesta);
                pausado = !pausado;
            } catch (IOException e) {
                System.err.println("Error en togglePausa: " + e.getMessage());
            }
        }
    }
    
    private void DemogorgonDelMes() {
        if (out == null) return;
        try {
            out.writeUTF("RANKING");
            out.flush();
            String linea = in.readUTF();
            if (linea != null && !linea.isEmpty()) {
                String[] partes = linea.split("\\|");

                for (int i = 0; i < partes.length && i < 3; i++) {
                    String ranking = partes[i].trim();
                    switch (i) {
                        case 0:
                            lblEstadoRanking1.setText("1: " + ranking);
                            break;
                        case 1:
                            lblEstadoRanking2.setText("2: " + ranking);
                            break;
                        case 2:
                            lblEstadoRanking3.setText("3: " + ranking);
                            break;
                    }
                }
                // Si hay menos de 3 demogorgons, limpiar los restantes
                if (partes.length < 1) lblEstadoRanking1.setText("1: ---");
                if (partes.length < 2) lblEstadoRanking2.setText("2: ---");
                if (partes.length < 3) lblEstadoRanking3.setText("3: ---");
            } else {
                lblEstadoRanking1.setText("1: ---");
                lblEstadoRanking2.setText("2: ---");
                lblEstadoRanking3.setText("3: ---");
            }
        } catch (IOException e) {
            lblEstadoRanking1.setText("1: Error");
            lblEstadoRanking2.setText("2: Error");
            lblEstadoRanking3.setText("3: Error");
        }
    }
    
    private void actualizarEvento() {
        if (out == null) return;
        try {
            out.writeUTF("EVENTO");
            out.flush();
            String linea = in.readUTF();
            if (linea != null && !linea.isEmpty() && linea.contains("|")) {
                String[] partes = linea.split("\\|");
                if (partes.length >= 2) {
                    String tipoEvento = partes[0];
                    String tiempoRestante = partes[1];

                    // Mostrar tiempo restante
                    if (tipoEvento.equals("NINGUNO")) {
                        lblEstadoEventoActual.setText("Ninguno");
                        lblEstadoDescripcionActual.setText("Esperando...");
                        lblEstadoTemporizadorActual.setText(tiempoRestante + "s");
                    } else {
                        // Mostrar nombre del evento
                        String nombreEvento = "";
                        String descripcion = "";
                        switch (tipoEvento) {
                            case "APAGÓN":
                                nombreEvento = "Apagón del Laboratorio";
                                descripcion = "<html>Portales inutilizables, <br>Demogorgons no se <br>mueven<html>";
                                break;
                            case "TORMENTA":
                                nombreEvento = "Tormenta del Upside Down";
                                descripcion = "<html>Recolección de <br>sangre x2, Demogorgons <br>más rápidos<html>";
                                break;
                            case "ELEVEN":
                                nombreEvento = "Intervención de Eleven";
                                descripcion = "<html>Demogorgons paralizados, <br>rescata niños de <br>Colmena<html>";
                                break;
                            case "RED_MENTAL":
                                nombreEvento = "Red Mental";
                                descripcion = "<html>Demogorgons se <br>agrupan en zona <br>con más niños<html>";
                                break;
                            default:
                                nombreEvento = tipoEvento;
                                descripcion = "Evento activo";
                                break;
                        }
                        lblEstadoEventoActual.setText(nombreEvento);
                        lblEstadoDescripcionActual.setText(descripcion);
                        lblEstadoTemporizadorActual.setText(tiempoRestante + "s");
                    }
                }
            } else {
                // Si no hay datos, mostrar estado por defecto
                lblEstadoEventoActual.setText("Ninguno");
                lblEstadoDescripcionActual.setText("Esperando...");
                lblEstadoTemporizadorActual.setText("--");
            }
        } catch (IOException e) {
            lblEstadoEventoActual.setText("Error");
            lblEstadoDescripcionActual.setText("Error de conexión");
            lblEstadoTemporizadorActual.setText("--");
        }
    }
    
    private void actualizarHawkins() {
        if (out == null) return;
        try {
            out.writeUTF("NINOS_HAWKINS");
            out.flush();
            String total = in.readUTF();
            if (total != null) {
                lblEstadoHawkins.setText("Total Niños en Hawkins: " + total);
            }
        } catch (IOException e) {
            lblEstadoHawkins.setText("Error");
        }
    }
    
    private void actualizarPortales() {
        if (out == null) return;
        try {
            out.writeUTF("PORTALES");
            out.flush();
            String linea = in.readUTF();
            if (linea != null && !linea.isEmpty()) {
                String[] partes = linea.split("\\|");

                for (String parte : partes) {
                    parte = parte.trim();
                    if (parte.startsWith("Bosque:")) {
                        String numero = parte.substring(parte.indexOf(":") + 1).trim();
                        lblEstadoPortal1.setText("Portal 1: [ " + numero + " ] niños");
                    } else if (parte.startsWith("Laboratorio:")) {
                        String numero = parte.substring(parte.indexOf(":") + 1).trim();
                        lblEstadoPortal2.setText("Portal 2: [ " + numero + " ] niños");
                    } else if (parte.startsWith("Centro:")) {
                        String numero = parte.substring(parte.indexOf(":") + 1).trim();
                        lblEstadoPortal3.setText("Portal 3: [ " + numero + " ] niños");
                    } else if (parte.startsWith("Alcantarillado:")) {
                        String numero = parte.substring(parte.indexOf(":") + 1).trim();
                        lblEstadoPortal4.setText("Portal 4: [ " + numero + " ] niños");
                    }
                }
            }
        } catch (IOException e) {
            lblEstadoPortal1.setText("Portal 1: Error");
            lblEstadoPortal2.setText("Portal 2: Error");
            lblEstadoPortal3.setText("Portal 3: Error");
            lblEstadoPortal4.setText("Portal 4: Error");
        }
    }
    
    private void actualizarUpsideDown() {
        if (out == null) return;
        try {
            out.writeUTF("UPSIDE_DOWN");
            out.flush();
            String linea = in.readUTF();
            if (linea != null && !linea.isEmpty()) {
                String[] partes = linea.split("\\|");
                if (partes.length >= 9) {
                    // Niños por zona
                    lblEstadoBosqueNinos.setText("<html>Bosque: [ " + partes[0] + " ] <br>niños</html>");
                    lblEstadoLaboratorioNinos.setText("<html>Laboratorio: [ " + partes[1] + " ] <br>niños</html>");
                    lblEstadoCentroNinos.setText("<html>Centro: [ " + partes[2] + " ] <br>niños</html>");
                    lblEstadoAlcantarillaNinos.setText("<html>Alcantarilla: [ " + partes[3] + " ] <br>niños</html>");
                    lblEstadoColmenaNinos.setText("<html>Colmena: [ " + partes[4] + " ] <br>niños</html>");

                    // Demogorgons por zona
                    lblEstadoBosqueDemos.setText("<html>Bosque: [ " + partes[5] + " ] <br>demogorgones</html>");
                    lblEstadoLaboratorioDemos.setText("<html>Laboratorio: [ " + partes[6] + " ] <br>demogorgones</html>");
                    lblEstadoCentroDemos.setText("<html>Centro: [ " + partes[7] + " ] <br>demogorgones</html>");
                    lblEstadoAlcantarillaDemos.setText("<html>Alcantarilla: [ " + partes[8] + " ] <br>demogorgones</html>");
                }
            }
        } catch (IOException e) {
            lblEstadoBosqueNinos.setText("<html>Bosque: [ Error ] <br>niños</html>");
            lblEstadoLaboratorioNinos.setText("<html>Laboratorio: [ Error ] <br>niños</html>");
            lblEstadoCentroNinos.setText("<html>Centro: [ Error ] <br>niños</html>");
            lblEstadoAlcantarillaNinos.setText("<html>Alcantarilla: [ Error ] <br>niños</html>");
            lblEstadoColmenaNinos.setText("<html>Colmena: [ Error ] <br>niños</html>");

            lblEstadoBosqueDemos.setText("<html>Bosque: [ Error ] <br>demogorgones</html>");
            lblEstadoLaboratorioDemos.setText("<html>Laboratorio: [ Error ] <br>demogorgones</html>");
            lblEstadoCentroDemos.setText("<html>Centro: [ Error ] <br>demogorgones</html>");
            lblEstadoAlcantarillaDemos.setText("<html>Alcantarilla: [ Error ] <br>demogorgones</html>");
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelGeneral = new javax.swing.JPanel();
        PanelResumenHawkins = new javax.swing.JPanel();
        lblTituloResumenHawkins = new javax.swing.JLabel();
        lblEstadoHawkins = new javax.swing.JLabel();
        PanelEstadoPortales = new javax.swing.JPanel();
        lblTituloEstadoPortales = new javax.swing.JLabel();
        lblEstadoPortal4 = new javax.swing.JLabel();
        lblEstadoPortal1 = new javax.swing.JLabel();
        lblEstadoPortal2 = new javax.swing.JLabel();
        lblEstadoPortal3 = new javax.swing.JLabel();
        PanelEstadoUD = new javax.swing.JPanel();
        lblTituloEstadoUD = new javax.swing.JLabel();
        lblEstadoColmenaNinos = new javax.swing.JLabel();
        lblEstadoBosqueDemos = new javax.swing.JLabel();
        lblEstadoLaboratorioNinos = new javax.swing.JLabel();
        lblEstadoLaboratorioDemos = new javax.swing.JLabel();
        lblEstadoCentroNinos = new javax.swing.JLabel();
        lblEstadoCentroDemos = new javax.swing.JLabel();
        lblEstadoAlcantarillaNinos = new javax.swing.JLabel();
        lblEstadoAlcantarillaDemos = new javax.swing.JLabel();
        lblEstadoBosqueNinos = new javax.swing.JLabel();
        PanelRankingDemogorgones = new javax.swing.JPanel();
        lblTituloRanking2 = new javax.swing.JLabel();
        lblEstadoRanking3 = new javax.swing.JLabel();
        lblEstadoRanking1 = new javax.swing.JLabel();
        lblEstadoRanking2 = new javax.swing.JLabel();
        lblTituloRanking1 = new javax.swing.JLabel();
        PanelEvento = new javax.swing.JPanel();
        lblTituloEvento2 = new javax.swing.JLabel();
        lblTituloEvento1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblEstadoTemporizadorActual = new javax.swing.JLabel();
        lblEstadoEventoActual = new javax.swing.JLabel();
        lblEstadoDescripcionActual = new javax.swing.JLabel();
        LabelModuloRemoto = new javax.swing.JLabel();
        btnPausa = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        PanelGeneral.setBackground(new java.awt.Color(51, 51, 51));
        PanelGeneral.setLayout(null);

        PanelResumenHawkins.setBackground(new java.awt.Color(51, 51, 51));
        PanelResumenHawkins.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelResumenHawkins.setLayout(null);

        lblTituloResumenHawkins.setFont(new java.awt.Font("Stencil", 0, 24)); // NOI18N
        lblTituloResumenHawkins.setForeground(new java.awt.Color(204, 0, 0));
        lblTituloResumenHawkins.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloResumenHawkins.setText("Resumen Hawkins");
        PanelResumenHawkins.add(lblTituloResumenHawkins);
        lblTituloResumenHawkins.setBounds(10, 10, 250, 50);

        lblEstadoHawkins.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoHawkins.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoHawkins.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoHawkins.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstadoHawkins.setText("Total Niños en Hawkins: [n]");
        lblEstadoHawkins.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelResumenHawkins.add(lblEstadoHawkins);
        lblEstadoHawkins.setBounds(10, 68, 250, 90);

        PanelGeneral.add(PanelResumenHawkins);
        PanelResumenHawkins.setBounds(10, 20, 270, 170);

        PanelEstadoPortales.setBackground(new java.awt.Color(51, 51, 51));
        PanelEstadoPortales.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelEstadoPortales.setLayout(null);

        lblTituloEstadoPortales.setFont(new java.awt.Font("Stencil", 0, 24)); // NOI18N
        lblTituloEstadoPortales.setForeground(new java.awt.Color(204, 0, 0));
        lblTituloEstadoPortales.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloEstadoPortales.setText("ESTADO PORTALES");
        PanelEstadoPortales.add(lblTituloEstadoPortales);
        lblTituloEstadoPortales.setBounds(10, 10, 250, 50);

        lblEstadoPortal4.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoPortal4.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoPortal4.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoPortal4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstadoPortal4.setText("Portal 4: [ n ] niños");
        lblEstadoPortal4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelEstadoPortales.add(lblEstadoPortal4);
        lblEstadoPortal4.setBounds(10, 330, 250, 60);

        lblEstadoPortal1.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoPortal1.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoPortal1.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoPortal1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstadoPortal1.setText("Portal 1: [ n ] niños");
        lblEstadoPortal1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelEstadoPortales.add(lblEstadoPortal1);
        lblEstadoPortal1.setBounds(10, 68, 250, 60);

        lblEstadoPortal2.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoPortal2.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoPortal2.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoPortal2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstadoPortal2.setText("Portal 2: [ n ] niños");
        lblEstadoPortal2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelEstadoPortales.add(lblEstadoPortal2);
        lblEstadoPortal2.setBounds(10, 150, 250, 60);

        lblEstadoPortal3.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoPortal3.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoPortal3.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoPortal3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstadoPortal3.setText("Portal 3: [ n ] niños");
        lblEstadoPortal3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelEstadoPortales.add(lblEstadoPortal3);
        lblEstadoPortal3.setBounds(10, 240, 250, 60);

        PanelGeneral.add(PanelEstadoPortales);
        PanelEstadoPortales.setBounds(10, 210, 270, 410);

        PanelEstadoUD.setBackground(new java.awt.Color(51, 51, 51));
        PanelEstadoUD.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelEstadoUD.setLayout(null);

        lblTituloEstadoUD.setFont(new java.awt.Font("Stencil", 0, 24)); // NOI18N
        lblTituloEstadoUD.setForeground(new java.awt.Color(204, 0, 0));
        lblTituloEstadoUD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloEstadoUD.setText("UBICACIONES UPSIDE DOWN");
        PanelEstadoUD.add(lblTituloEstadoUD);
        lblTituloEstadoUD.setBounds(10, 10, 390, 50);

        lblEstadoColmenaNinos.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoColmenaNinos.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoColmenaNinos.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoColmenaNinos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEstadoColmenaNinos.setText("<html>Colmena: [ n ] <br>niños<html>");
        lblEstadoColmenaNinos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelEstadoUD.add(lblEstadoColmenaNinos);
        lblEstadoColmenaNinos.setBounds(10, 380, 200, 60);

        lblEstadoBosqueDemos.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoBosqueDemos.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoBosqueDemos.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoBosqueDemos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEstadoBosqueDemos.setText("<html>Bosque: [ n ] <br>demogorgones<html>");
        lblEstadoBosqueDemos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelEstadoUD.add(lblEstadoBosqueDemos);
        lblEstadoBosqueDemos.setBounds(210, 60, 200, 60);

        lblEstadoLaboratorioNinos.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoLaboratorioNinos.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoLaboratorioNinos.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoLaboratorioNinos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEstadoLaboratorioNinos.setText("<html>Laboratorio: [ n ] <br>niños<html>");
        lblEstadoLaboratorioNinos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelEstadoUD.add(lblEstadoLaboratorioNinos);
        lblEstadoLaboratorioNinos.setBounds(10, 140, 200, 60);

        lblEstadoLaboratorioDemos.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoLaboratorioDemos.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoLaboratorioDemos.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoLaboratorioDemos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEstadoLaboratorioDemos.setText("<html>Laboratorio: [ n ] <br>demogorgones<html>");
        lblEstadoLaboratorioDemos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelEstadoUD.add(lblEstadoLaboratorioDemos);
        lblEstadoLaboratorioDemos.setBounds(210, 140, 200, 60);

        lblEstadoCentroNinos.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoCentroNinos.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoCentroNinos.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoCentroNinos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEstadoCentroNinos.setText("<html>Centro: [ n ] <br>niños<html>");
        lblEstadoCentroNinos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelEstadoUD.add(lblEstadoCentroNinos);
        lblEstadoCentroNinos.setBounds(10, 220, 200, 60);

        lblEstadoCentroDemos.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoCentroDemos.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoCentroDemos.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoCentroDemos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEstadoCentroDemos.setText("<html>Centro: [ n ] <br>demogorgones<html>");
        lblEstadoCentroDemos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelEstadoUD.add(lblEstadoCentroDemos);
        lblEstadoCentroDemos.setBounds(210, 220, 200, 60);

        lblEstadoAlcantarillaNinos.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoAlcantarillaNinos.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoAlcantarillaNinos.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoAlcantarillaNinos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEstadoAlcantarillaNinos.setText("<html>Alcantarilla: [ n ] <br>niños<html>");
        lblEstadoAlcantarillaNinos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelEstadoUD.add(lblEstadoAlcantarillaNinos);
        lblEstadoAlcantarillaNinos.setBounds(10, 300, 200, 60);

        lblEstadoAlcantarillaDemos.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoAlcantarillaDemos.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoAlcantarillaDemos.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoAlcantarillaDemos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEstadoAlcantarillaDemos.setText("<html>Alcantarilla: [ n ] <br>demogorgones<html>");
        lblEstadoAlcantarillaDemos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelEstadoUD.add(lblEstadoAlcantarillaDemos);
        lblEstadoAlcantarillaDemos.setBounds(210, 300, 200, 60);

        lblEstadoBosqueNinos.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoBosqueNinos.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoBosqueNinos.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoBosqueNinos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEstadoBosqueNinos.setText("<html>Bosque: [ n ] <br>niños<html>");
        lblEstadoBosqueNinos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelEstadoUD.add(lblEstadoBosqueNinos);
        lblEstadoBosqueNinos.setBounds(10, 60, 200, 60);

        PanelGeneral.add(PanelEstadoUD);
        PanelEstadoUD.setBounds(299, 106, 420, 460);

        PanelRankingDemogorgones.setBackground(new java.awt.Color(51, 51, 51));
        PanelRankingDemogorgones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelRankingDemogorgones.setLayout(null);

        lblTituloRanking2.setFont(new java.awt.Font("Stencil", 0, 24)); // NOI18N
        lblTituloRanking2.setForeground(new java.awt.Color(204, 0, 0));
        lblTituloRanking2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloRanking2.setText("DEMOGORGONES");
        PanelRankingDemogorgones.add(lblTituloRanking2);
        lblTituloRanking2.setBounds(10, 40, 240, 30);

        lblEstadoRanking3.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoRanking3.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoRanking3.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoRanking3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstadoRanking3.setText("3: ");
        lblEstadoRanking3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelRankingDemogorgones.add(lblEstadoRanking3);
        lblEstadoRanking3.setBounds(0, 180, 260, 50);

        lblEstadoRanking1.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoRanking1.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoRanking1.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoRanking1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstadoRanking1.setText("1: ");
        lblEstadoRanking1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelRankingDemogorgones.add(lblEstadoRanking1);
        lblEstadoRanking1.setBounds(0, 80, 260, 50);

        lblEstadoRanking2.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoRanking2.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoRanking2.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoRanking2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstadoRanking2.setText("2: ");
        lblEstadoRanking2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelRankingDemogorgones.add(lblEstadoRanking2);
        lblEstadoRanking2.setBounds(0, 130, 260, 50);

        lblTituloRanking1.setFont(new java.awt.Font("Stencil", 0, 24)); // NOI18N
        lblTituloRanking1.setForeground(new java.awt.Color(204, 0, 0));
        lblTituloRanking1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloRanking1.setText("ranking");
        PanelRankingDemogorgones.add(lblTituloRanking1);
        lblTituloRanking1.setBounds(10, 20, 240, 30);

        PanelGeneral.add(PanelRankingDemogorgones);
        PanelRankingDemogorgones.setBounds(740, 20, 260, 230);

        PanelEvento.setBackground(new java.awt.Color(51, 51, 51));
        PanelEvento.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        PanelEvento.setLayout(null);

        lblTituloEvento2.setFont(new java.awt.Font("Stencil", 0, 24)); // NOI18N
        lblTituloEvento2.setForeground(new java.awt.Color(204, 0, 0));
        lblTituloEvento2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloEvento2.setText("ACTIVo");
        PanelEvento.add(lblTituloEvento2);
        lblTituloEvento2.setBounds(10, 40, 240, 30);

        lblTituloEvento1.setFont(new java.awt.Font("Stencil", 0, 24)); // NOI18N
        lblTituloEvento1.setForeground(new java.awt.Color(204, 0, 0));
        lblTituloEvento1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloEvento1.setText("EVENTO GLObal");
        PanelEvento.add(lblTituloEvento1);
        lblTituloEvento1.setBounds(10, 20, 240, 30);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0), 4));
        jPanel1.setLayout(null);

        lblEstadoTemporizadorActual.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoTemporizadorActual.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoTemporizadorActual.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoTemporizadorActual.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstadoTemporizadorActual.setText("-");
        jPanel1.add(lblEstadoTemporizadorActual);
        lblEstadoTemporizadorActual.setBounds(0, 160, 260, 80);

        lblEstadoEventoActual.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoEventoActual.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoEventoActual.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoEventoActual.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstadoEventoActual.setText("Ninguno");
        jPanel1.add(lblEstadoEventoActual);
        lblEstadoEventoActual.setBounds(0, 0, 260, 80);

        lblEstadoDescripcionActual.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoDescripcionActual.setFont(new java.awt.Font("Britannic Bold", 0, 18)); // NOI18N
        lblEstadoDescripcionActual.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoDescripcionActual.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstadoDescripcionActual.setText("Nada");
        jPanel1.add(lblEstadoDescripcionActual);
        lblEstadoDescripcionActual.setBounds(0, 80, 260, 80);

        PanelEvento.add(jPanel1);
        jPanel1.setBounds(0, 80, 260, 270);

        PanelGeneral.add(PanelEvento);
        PanelEvento.setBounds(740, 270, 260, 350);

        LabelModuloRemoto.setFont(new java.awt.Font("Stencil", 0, 36)); // NOI18N
        LabelModuloRemoto.setForeground(new java.awt.Color(204, 0, 0));
        LabelModuloRemoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelModuloRemoto.setText("Modulo Remoto");
        PanelGeneral.add(LabelModuloRemoto);
        LabelModuloRemoto.setBounds(300, 20, 420, 70);

        btnPausa.setBackground(new java.awt.Color(204, 0, 0));
        btnPausa.setForeground(new java.awt.Color(255, 255, 255));
        btnPausa.setText("Pausar");
        btnPausa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPausaActionPerformed(evt);
            }
        });
        PanelGeneral.add(btnPausa);
        btnPausa.setBounds(299, 580, 420, 40);

        getContentPane().add(PanelGeneral);
        PanelGeneral.setBounds(0, 0, 1020, 650);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPausaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPausaActionPerformed
        togglePausa();
    }//GEN-LAST:event_btnPausaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ModuloRemoto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ModuloRemoto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ModuloRemoto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ModuloRemoto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ModuloRemoto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelModuloRemoto;
    private javax.swing.JPanel PanelEstadoPortales;
    private javax.swing.JPanel PanelEstadoUD;
    private javax.swing.JPanel PanelEvento;
    private javax.swing.JPanel PanelGeneral;
    private javax.swing.JPanel PanelRankingDemogorgones;
    private javax.swing.JPanel PanelResumenHawkins;
    private javax.swing.JButton btnPausa;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblEstadoAlcantarillaDemos;
    private javax.swing.JLabel lblEstadoAlcantarillaNinos;
    private javax.swing.JLabel lblEstadoBosqueDemos;
    private javax.swing.JLabel lblEstadoBosqueNinos;
    private javax.swing.JLabel lblEstadoCentroDemos;
    private javax.swing.JLabel lblEstadoCentroNinos;
    private javax.swing.JLabel lblEstadoColmenaNinos;
    private javax.swing.JLabel lblEstadoDescripcionActual;
    private javax.swing.JLabel lblEstadoEventoActual;
    private javax.swing.JLabel lblEstadoHawkins;
    private javax.swing.JLabel lblEstadoLaboratorioDemos;
    private javax.swing.JLabel lblEstadoLaboratorioNinos;
    private javax.swing.JLabel lblEstadoPortal1;
    private javax.swing.JLabel lblEstadoPortal2;
    private javax.swing.JLabel lblEstadoPortal3;
    private javax.swing.JLabel lblEstadoPortal4;
    private javax.swing.JLabel lblEstadoRanking1;
    private javax.swing.JLabel lblEstadoRanking2;
    private javax.swing.JLabel lblEstadoRanking3;
    private javax.swing.JLabel lblEstadoTemporizadorActual;
    private javax.swing.JLabel lblTituloEstadoPortales;
    private javax.swing.JLabel lblTituloEstadoUD;
    private javax.swing.JLabel lblTituloEvento1;
    private javax.swing.JLabel lblTituloEvento2;
    private javax.swing.JLabel lblTituloRanking1;
    private javax.swing.JLabel lblTituloRanking2;
    private javax.swing.JLabel lblTituloResumenHawkins;
    // End of variables declaration//GEN-END:variables
}
