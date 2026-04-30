package PA;

import java.util.List;

public class GUI extends javax.swing.JFrame {
    
    // ==================== CÓDIGO MANUAL ====================
    private Sistemageneral sistema;
    
    // Constructor con sistema (para usar desde Main)
    public GUI(Sistemageneral sistema) {
        this.sistema = sistema;
        initComponents();
        iniciarActualizador();
        setTitle("La Batalla de Hawkins");
    }
    
    private void iniciarActualizador() {
        new Thread(() -> {
            while (true) {
                javax.swing.SwingUtilities.invokeLater(() -> actualizarGUI());
                try { 
                    Thread.sleep(500); 
                } catch (InterruptedException e) {}
            }
        }).start();
    }
    
    private void actualizarGUI() {
        if (sistema == null) return;
        
        // Actualizar Hawkins
        LabelCallePrincipal.setText("CALLE PRINCIPAL: " + sistema.callePrincipal.getCantidadNinos());
        LabelSotanoByers.setText("SOTANO BYERS: " + sistema.sotanoByers.getCantidadNinos());
        LabelRadioWSQK.setText("RADIO WSQK: " + sistema.radioWSQK.getCantidadNinos());
        
        // Actualizar Sangre
        jLabel4.setText("SANGRE: " + sistema.get_sangre_recolectada());
        
        // Mostrar evento actual
        String evento = " ";
        if (sistema.hayApagon) {
            evento = "Apagón del Laboratorio - Portales bloqueados";
        } else if (sistema.tormentaon()) {
            evento = "Tormenta del Upside Down - Recolección x2";
        } else if (sistema.eleveen_enfadada) {
            evento = "Intervención de Eleven - Demogorgons paralizados";
        } else if (sistema.red_mental_on) {
            evento = "Red Mental - Demogorgons agrupados";
        } else {
            evento = "Sin eventos activos";
        }
        
        if (lblEventoActual != null) {
            lblEventoActual.setText("Evento: " + evento);
        }
        
        // ========== MOSTRAR IDs DE NIÑOS EN CADA ZONA ==========
        
        // Hawkins - Calle Principal
        mostrarNinosEnZona(sistema.callePrincipal, txtCallePrincipal);
        
        // Hawkins - Sótano Byers
        mostrarNinosEnZona(sistema.sotanoByers, txtSotanoByers);
        
        // Hawkins - Radio WSQK
        mostrarNinosEnZona(sistema.radioWSQK, txtRadioWSQK);
        
        // UpsideDown - Bosque
        mostrarNinosEnZona(sistema.bosque, txtBosque);
        
        // UpsideDown - Laboratorio
        mostrarNinosEnZona(sistema.laboratorio, txtLaboratorio);
        
        // UpsideDown - Centro Comercial
        mostrarNinosEnZona(sistema.centroComercial, txtCentroComercial);
        
        // UpsideDown - Alcantarillado
        mostrarNinosEnZona(sistema.alcantarillado, txtAlcantarillado);
        
        // Colmena
        mostrarNinosEnColmena();
        
        // Portales
        mostrarPortales();
        
        // Demogorgones
        mostrarDemogorgonsPorZona();
        DemogorgonDelMes();
        
        
    }
    
    // ==================== FIN CÓDIGO MANUAL ====================
    
    private void mostrarDemogorgonsPorZona() {
        // BOSQUE
        StringBuilder sbBosque = new StringBuilder();
        for (Demogorgon d : sistema.getDemogorgons()) {
            if (d.getZonaActual() == sistema.bosque) {
                sbBosque.append(d.getDemogorgonId()).append(" (").append(d.getContadorCapturas()).append(")\n");
            }
        }
        txtDemogorgonsBosque.setText(sbBosque.toString());

        // LABORATORIO
        StringBuilder sbLaboratorio = new StringBuilder();
        for (Demogorgon d : sistema.getDemogorgons()) {
            if (d.getZonaActual() == sistema.laboratorio) {
                sbLaboratorio.append(d.getDemogorgonId()).append(" (").append(d.getContadorCapturas()).append(")\n");
            }
        }
        txtDemogorgonsLaboratorio.setText(sbLaboratorio.toString());

        // CENTRO COMERCIAL
        StringBuilder sbCentro = new StringBuilder();
        for (Demogorgon d : sistema.getDemogorgons()) {
            if (d.getZonaActual() == sistema.centroComercial) {
                sbCentro.append(d.getDemogorgonId()).append(" (").append(d.getContadorCapturas()).append(")\n");
            }
        }
        txtDemogorgonsCentro.setText(sbCentro.toString());

        // ALCANTARILLADO
        StringBuilder sbAlcantarillado = new StringBuilder();
        for (Demogorgon d : sistema.getDemogorgons()) {
            if (d.getZonaActual() == sistema.alcantarillado) {
                sbAlcantarillado.append(d.getDemogorgonId()).append(" (").append(d.getContadorCapturas()).append(")\n");
            }
        }
        txtDemogorgonsAlcantarillado.setText(sbAlcantarillado.toString());
    }
    
    private void DemogorgonDelMes() {
        List<Demogorgon> demogorgons = sistema.getDemogorgons();
        // Ordenar por capturas (mayor a menor)
        demogorgons.sort((a, b) -> Integer.compare(b.getContadorCapturas(), a.getContadorCapturas()));

        StringBuilder sb = new StringBuilder();
        sb.append("Top 3 Demogorgones\n");
        for (int i = 0; i < Math.min(3, demogorgons.size()); i++) {
            Demogorgon d = demogorgons.get(i);
            sb.append((i+1)).append(". ")
              .append(d.getDemogorgonId())
              .append(" - ")
              .append(d.getContadorCapturas())
              .append(" capt.\n");
        }

        txtRanking.setText(sb.toString());
    }
    
    private void mostrarPortales() {
        // PORTAL BOSQUE - Obtener los portales a través de sistema
        Portal portalBosque = sistema.getPortalBosque();
        Portal portalLaboratorio = sistema.getPortalLaboratorio();
        Portal portalCentroComercial = sistema.getPortalCentroComercial();
        Portal portalAlcantarillado = sistema.getPortalAlcantarillado();

        // PORTAL BOSQUE
        mostrarNiñosEnCola(portalBosque.getNinosEsperandoSalida(), txtPortalBosqueSalida, "→ UD");
        mostrarNiñosEnCola(portalBosque.getNinosEsperandoRegreso(), txtPortalBosqueRegreso, "→ HWK");

        // PORTAL LABORATORIO
        mostrarNiñosEnCola(portalLaboratorio.getNinosEsperandoSalida(), txtPortalLaboratorioSalida, "→ UD");
        mostrarNiñosEnCola(portalLaboratorio.getNinosEsperandoRegreso(), txtPortalLaboratorioRegreso, "→ HWK");

        // PORTAL CENTRO COMERCIAL
        mostrarNiñosEnCola(portalCentroComercial.getNinosEsperandoSalida(), txtPortalCentroSalida, "→ UD");
        mostrarNiñosEnCola(portalCentroComercial.getNinosEsperandoRegreso(), txtPortalCentroRegreso, "→ HWK");

        // PORTAL ALCANTARILLADO
        mostrarNiñosEnCola(portalAlcantarillado.getNinosEsperandoSalida(), txtPortalAlcantarilladoSalida, "→ UD");
        mostrarNiñosEnCola(portalAlcantarillado.getNinosEsperandoRegreso(), txtPortalAlcantarilladoRegreso, "→ HWK");
    }
    
    private void mostrarNiñosEnCola(List<Child> ninos, javax.swing.JTextArea textArea, String direccion) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(direccion).append("]\n");
        for (Child nino : ninos) {
            sb.append(nino.getNinoId()).append("\n");
        }
        textArea.setText(sb.toString());
    }
    
    private void mostrarNinosEnZona(Zona zona, javax.swing.JTextArea textArea) {
        StringBuilder sb = new StringBuilder();
        for (Child nino : zona.getNinosPresentes()) {
            sb.append(nino.getNinoId()).append("\n");
        }
        textArea.setText(sb.toString());
    }
    
    private void mostrarNinosEnColmena() {
        StringBuilder sb = new StringBuilder();
        // Necesitas un método en Colmena para obtener la lista
        // Por ahora usamos cantidad, pero idealmente:
        for (Child nino : sistema.colmena.getNinosCapturados()) {
            sb.append(nino.getNinoId()).append("\n");
        }
        txtColmena.setText(sb.toString());
    }
    
    public GUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelGeneral = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        PanelHawkins = new javax.swing.JPanel();
        LabelHawkins = new javax.swing.JLabel();
        PanelCallePrincipal = new javax.swing.JPanel();
        LabelCallePrincipal = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtCallePrincipal = new javax.swing.JTextArea();
        PanelSotanoByers = new javax.swing.JPanel();
        LabelSotanoByers = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtSotanoByers = new javax.swing.JTextArea();
        PanelRadioWSQK = new javax.swing.JPanel();
        LabelRadioWSQK = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtRadioWSQK = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        PanelPortales = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtPortalLaboratorioSalida = new javax.swing.JTextArea();
        jScrollPane9 = new javax.swing.JScrollPane();
        txtPortalBosqueRegreso = new javax.swing.JTextArea();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtPortalBosqueSalida = new javax.swing.JTextArea();
        jScrollPane11 = new javax.swing.JScrollPane();
        txtPortalLaboratorioRegreso = new javax.swing.JTextArea();
        jScrollPane12 = new javax.swing.JScrollPane();
        txtPortalCentroSalida = new javax.swing.JTextArea();
        jScrollPane13 = new javax.swing.JScrollPane();
        txtPortalCentroRegreso = new javax.swing.JTextArea();
        jScrollPane14 = new javax.swing.JScrollPane();
        txtPortalAlcantarilladoSalida = new javax.swing.JTextArea();
        jScrollPane15 = new javax.swing.JScrollPane();
        txtPortalAlcantarilladoRegreso = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtBosque = new javax.swing.JTextArea();
        jScrollPane23 = new javax.swing.JScrollPane();
        jScrollPane24 = new javax.swing.JScrollPane();
        txtDemogorgonsBosque = new javax.swing.JTextArea();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtLaboratorio = new javax.swing.JTextArea();
        jScrollPane18 = new javax.swing.JScrollPane();
        txtDemogorgonsLaboratorio = new javax.swing.JTextArea();
        jPanel24 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtCentroComercial = new javax.swing.JTextArea();
        jScrollPane19 = new javax.swing.JScrollPane();
        jScrollPane20 = new javax.swing.JScrollPane();
        txtDemogorgonsCentro = new javax.swing.JTextArea();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtAlcantarillado = new javax.swing.JTextArea();
        jScrollPane21 = new javax.swing.JScrollPane();
        jScrollPane22 = new javax.swing.JScrollPane();
        txtDemogorgonsAlcantarillado = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        EventPanel = new javax.swing.JPanel();
        lblEventoActual = new javax.swing.JLabel();
        jScrollPane16 = new javax.swing.JScrollPane();
        txtColmena = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane17 = new javax.swing.JScrollPane();
        txtRanking = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PanelGeneral.setBackground(new java.awt.Color(0, 102, 0));

        jPanel2.setBackground(new java.awt.Color(0, 102, 0));

        PanelHawkins.setBackground(new java.awt.Color(0, 153, 0));

        LabelHawkins.setText("-HAWKINS---------------------------------------");

        PanelCallePrincipal.setBackground(new java.awt.Color(0, 204, 0));

        LabelCallePrincipal.setText("CALLE PRINCIPAL");

        txtCallePrincipal.setEditable(false);
        txtCallePrincipal.setColumns(15);
        txtCallePrincipal.setRows(5);
        jScrollPane1.setViewportView(txtCallePrincipal);

        javax.swing.GroupLayout PanelCallePrincipalLayout = new javax.swing.GroupLayout(PanelCallePrincipal);
        PanelCallePrincipal.setLayout(PanelCallePrincipalLayout);
        PanelCallePrincipalLayout.setHorizontalGroup(
            PanelCallePrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCallePrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCallePrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelCallePrincipalLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(LabelCallePrincipal))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        PanelCallePrincipalLayout.setVerticalGroup(
            PanelCallePrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCallePrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LabelCallePrincipal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        PanelSotanoByers.setBackground(new java.awt.Color(0, 204, 0));

        LabelSotanoByers.setText("SOTANO BYERS");

        txtSotanoByers.setEditable(false);
        txtSotanoByers.setColumns(15);
        txtSotanoByers.setRows(5);
        jScrollPane2.setViewportView(txtSotanoByers);

        javax.swing.GroupLayout PanelSotanoByersLayout = new javax.swing.GroupLayout(PanelSotanoByers);
        PanelSotanoByers.setLayout(PanelSotanoByersLayout);
        PanelSotanoByersLayout.setHorizontalGroup(
            PanelSotanoByersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSotanoByersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelSotanoByersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelSotanoByersLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(LabelSotanoByers))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        PanelSotanoByersLayout.setVerticalGroup(
            PanelSotanoByersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSotanoByersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LabelSotanoByers)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        PanelRadioWSQK.setBackground(new java.awt.Color(0, 204, 0));

        LabelRadioWSQK.setText("RADIO WSQK");

        txtRadioWSQK.setEditable(false);
        txtRadioWSQK.setColumns(15);
        txtRadioWSQK.setRows(5);
        jScrollPane3.setViewportView(txtRadioWSQK);

        javax.swing.GroupLayout PanelRadioWSQKLayout = new javax.swing.GroupLayout(PanelRadioWSQK);
        PanelRadioWSQK.setLayout(PanelRadioWSQKLayout);
        PanelRadioWSQKLayout.setHorizontalGroup(
            PanelRadioWSQKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelRadioWSQKLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelRadioWSQKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelRadioWSQKLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(LabelRadioWSQK))
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
        PanelRadioWSQKLayout.setVerticalGroup(
            PanelRadioWSQKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelRadioWSQKLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LabelRadioWSQK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(204, 51, 0));

        jLabel4.setBackground(new java.awt.Color(204, 0, 0));
        jLabel4.setText("SANGRE:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(4, 4, 4))
        );

        javax.swing.GroupLayout PanelHawkinsLayout = new javax.swing.GroupLayout(PanelHawkins);
        PanelHawkins.setLayout(PanelHawkinsLayout);
        PanelHawkinsLayout.setHorizontalGroup(
            PanelHawkinsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelHawkinsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelHawkinsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(PanelHawkinsLayout.createSequentialGroup()
                        .addComponent(LabelHawkins)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(PanelCallePrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelSotanoByers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelRadioWSQK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        PanelHawkinsLayout.setVerticalGroup(
            PanelHawkinsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelHawkinsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LabelHawkins)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelCallePrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelSotanoByers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelRadioWSQK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        PanelPortales.setBackground(new java.awt.Color(0, 153, 0));

        jPanel12.setBackground(new java.awt.Color(0, 204, 0));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );

        jPanel14.setBackground(new java.awt.Color(0, 204, 0));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );

        jPanel21.setBackground(new java.awt.Color(0, 204, 0));

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );

        jPanel18.setBackground(new java.awt.Color(0, 204, 0));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );

        txtPortalLaboratorioSalida.setEditable(false);
        txtPortalLaboratorioSalida.setColumns(5);
        txtPortalLaboratorioSalida.setRows(5);
        jScrollPane8.setViewportView(txtPortalLaboratorioSalida);

        txtPortalBosqueRegreso.setEditable(false);
        txtPortalBosqueRegreso.setColumns(5);
        txtPortalBosqueRegreso.setRows(5);
        jScrollPane9.setViewportView(txtPortalBosqueRegreso);

        txtPortalBosqueSalida.setEditable(false);
        txtPortalBosqueSalida.setColumns(5);
        txtPortalBosqueSalida.setRows(5);
        jScrollPane10.setViewportView(txtPortalBosqueSalida);

        txtPortalLaboratorioRegreso.setEditable(false);
        txtPortalLaboratorioRegreso.setColumns(5);
        txtPortalLaboratorioRegreso.setRows(5);
        jScrollPane11.setViewportView(txtPortalLaboratorioRegreso);

        txtPortalCentroSalida.setEditable(false);
        txtPortalCentroSalida.setColumns(5);
        txtPortalCentroSalida.setRows(5);
        jScrollPane12.setViewportView(txtPortalCentroSalida);

        txtPortalCentroRegreso.setEditable(false);
        txtPortalCentroRegreso.setColumns(5);
        txtPortalCentroRegreso.setRows(5);
        jScrollPane13.setViewportView(txtPortalCentroRegreso);

        txtPortalAlcantarilladoSalida.setEditable(false);
        txtPortalAlcantarilladoSalida.setColumns(5);
        txtPortalAlcantarilladoSalida.setRows(5);
        jScrollPane14.setViewportView(txtPortalAlcantarilladoSalida);

        txtPortalAlcantarilladoRegreso.setEditable(false);
        txtPortalAlcantarilladoRegreso.setColumns(5);
        txtPortalAlcantarilladoRegreso.setRows(5);
        jScrollPane15.setViewportView(txtPortalAlcantarilladoRegreso);

        javax.swing.GroupLayout PanelPortalesLayout = new javax.swing.GroupLayout(PanelPortales);
        PanelPortales.setLayout(PanelPortalesLayout);
        PanelPortalesLayout.setHorizontalGroup(
            PanelPortalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPortalesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelPortalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPortalesLayout.createSequentialGroup()
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelPortalesLayout.createSequentialGroup()
                        .addGroup(PanelPortalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelPortalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelPortalesLayout.createSequentialGroup()
                                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PanelPortalesLayout.createSequentialGroup()
                                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(PanelPortalesLayout.createSequentialGroup()
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelPortalesLayout.setVerticalGroup(
            PanelPortalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelPortalesLayout.createSequentialGroup()
                .addGroup(PanelPortalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPortalesLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelPortalesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(PanelPortalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPortalesLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79)
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(77, 77, 77)
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))
                    .addGroup(PanelPortalesLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(PanelPortalesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(0, 153, 0));

        jLabel3.setText("-UPSIDE-DOWN-----------------------------------------------------------");

        jPanel22.setBackground(new java.awt.Color(0, 204, 0));

        txtBosque.setEditable(false);
        txtBosque.setColumns(10);
        txtBosque.setRows(5);
        jScrollPane4.setViewportView(txtBosque);

        txtDemogorgonsBosque.setEditable(false);
        txtDemogorgonsBosque.setColumns(5);
        txtDemogorgonsBosque.setRows(5);
        jScrollPane24.setViewportView(txtDemogorgonsBosque);

        jScrollPane23.setViewportView(jScrollPane24);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane23)))
        );

        jPanel23.setBackground(new java.awt.Color(0, 204, 0));

        txtLaboratorio.setEditable(false);
        txtLaboratorio.setColumns(10);
        txtLaboratorio.setRows(5);
        jScrollPane5.setViewportView(txtLaboratorio);

        txtDemogorgonsLaboratorio.setEditable(false);
        txtDemogorgonsLaboratorio.setColumns(5);
        txtDemogorgonsLaboratorio.setRows(5);
        jScrollPane18.setViewportView(txtDemogorgonsLaboratorio);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel24.setBackground(new java.awt.Color(0, 204, 0));

        txtCentroComercial.setEditable(false);
        txtCentroComercial.setColumns(10);
        txtCentroComercial.setRows(5);
        jScrollPane6.setViewportView(txtCentroComercial);

        txtDemogorgonsCentro.setEditable(false);
        txtDemogorgonsCentro.setColumns(5);
        txtDemogorgonsCentro.setRows(5);
        jScrollPane20.setViewportView(txtDemogorgonsCentro);

        jScrollPane19.setViewportView(jScrollPane20);

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane6)
                    .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)))
        );

        jPanel25.setBackground(new java.awt.Color(0, 204, 0));

        txtAlcantarillado.setEditable(false);
        txtAlcantarillado.setColumns(10);
        txtAlcantarillado.setRows(5);
        jScrollPane7.setViewportView(txtAlcantarillado);

        txtDemogorgonsAlcantarillado.setEditable(false);
        txtDemogorgonsAlcantarillado.setColumns(5);
        txtDemogorgonsAlcantarillado.setRows(5);
        jScrollPane22.setViewportView(txtDemogorgonsAlcantarillado);

        jScrollPane21.setViewportView(jScrollPane22);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane7)
                    .addComponent(jScrollPane21, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel24, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel23, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel22, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setText("Batalla Por Hawkins");

        EventPanel.setBackground(new java.awt.Color(153, 153, 0));

        lblEventoActual.setBackground(new java.awt.Color(204, 0, 0));
        lblEventoActual.setText("Evento Actual:");

        javax.swing.GroupLayout EventPanelLayout = new javax.swing.GroupLayout(EventPanel);
        EventPanel.setLayout(EventPanelLayout);
        EventPanelLayout.setHorizontalGroup(
            EventPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EventPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEventoActual)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        EventPanelLayout.setVerticalGroup(
            EventPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EventPanelLayout.createSequentialGroup()
                .addComponent(lblEventoActual)
                .addGap(4, 4, 4))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelHawkins, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(EventPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PanelPortales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelHawkins, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(22, 22, 22))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(PanelPortales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EventPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        txtColmena.setColumns(8);
        txtColmena.setRows(5);
        jScrollPane16.setViewportView(txtColmena);

        jLabel8.setText("COLMENA");

        txtRanking.setColumns(3);
        txtRanking.setRows(5);
        jScrollPane17.setViewportView(txtRanking);

        javax.swing.GroupLayout PanelGeneralLayout = new javax.swing.GroupLayout(PanelGeneral);
        PanelGeneral.setLayout(PanelGeneralLayout);
        PanelGeneralLayout.setHorizontalGroup(
            PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelGeneralLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel8))
                    .addGroup(PanelGeneralLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(jScrollPane17))))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        PanelGeneralLayout.setVerticalGroup(
            PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelGeneralLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(149, 149, 149))
            .addGroup(PanelGeneralLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PanelGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, 570, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
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
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel EventPanel;
    private javax.swing.JLabel LabelCallePrincipal;
    private javax.swing.JLabel LabelHawkins;
    private javax.swing.JLabel LabelRadioWSQK;
    private javax.swing.JLabel LabelSotanoByers;
    private javax.swing.JPanel PanelCallePrincipal;
    private javax.swing.JPanel PanelGeneral;
    private javax.swing.JPanel PanelHawkins;
    private javax.swing.JPanel PanelPortales;
    private javax.swing.JPanel PanelRadioWSQK;
    private javax.swing.JPanel PanelSotanoByers;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel lblEventoActual;
    private javax.swing.JTextArea txtAlcantarillado;
    private javax.swing.JTextArea txtBosque;
    private javax.swing.JTextArea txtCallePrincipal;
    private javax.swing.JTextArea txtCentroComercial;
    private javax.swing.JTextArea txtColmena;
    private javax.swing.JTextArea txtDemogorgonsAlcantarillado;
    private javax.swing.JTextArea txtDemogorgonsBosque;
    private javax.swing.JTextArea txtDemogorgonsCentro;
    private javax.swing.JTextArea txtDemogorgonsLaboratorio;
    private javax.swing.JTextArea txtLaboratorio;
    private javax.swing.JTextArea txtPortalAlcantarilladoRegreso;
    private javax.swing.JTextArea txtPortalAlcantarilladoSalida;
    private javax.swing.JTextArea txtPortalBosqueRegreso;
    private javax.swing.JTextArea txtPortalBosqueSalida;
    private javax.swing.JTextArea txtPortalCentroRegreso;
    private javax.swing.JTextArea txtPortalCentroSalida;
    private javax.swing.JTextArea txtPortalLaboratorioRegreso;
    private javax.swing.JTextArea txtPortalLaboratorioSalida;
    private javax.swing.JTextArea txtRadioWSQK;
    private javax.swing.JTextArea txtRanking;
    private javax.swing.JTextArea txtSotanoByers;
    // End of variables declaration//GEN-END:variables
}
