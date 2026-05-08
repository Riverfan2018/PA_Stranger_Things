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
        
        setSize(1035, 670);
        setMinimumSize(new java.awt.Dimension(1024, 640));
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
        lblCallePrincipal.setText(sistema.callePrincipal.getCantidadNinos() + " :CALLE PRINCIPAL ");
        lblSotanoByers.setText(sistema.sotanoByers.getCantidadNinos() + " :SOTANO BYERS");
        lblRadioWSQK.setText(sistema.radioWSQK.getCantidadNinos() + " :RADIO WSQK");
        
        // Actualizar Sangre
        lblSangre.setText("" + sistema.get_sangre_recolectada());
        
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
        
        // ========== MOSTRAR IDs DE NIÑOS EN CADA ZONA ==========
        
        // Hawkins - Calle Principal
        mostrarNinosEnZona(sistema.callePrincipal, txtCallePrincipal, 5);
        
        // Hawkins - Sótano Byers
        mostrarNinosEnZona(sistema.sotanoByers, txtSotanoByers, 5);
        
        // Hawkins - Radio WSQK
        mostrarNinosEnZona(sistema.radioWSQK, txtRadioWSQK, 5);
        
        // UpsideDown - Bosque
        mostrarNinosEnZona(sistema.bosque, txtBosque, 3);
        
        // UpsideDown - Laboratorio
        mostrarNinosEnZona(sistema.laboratorio, txtLaboratorio, 3);
        
        // UpsideDown - Centro Comercial
        mostrarNinosEnZona(sistema.centroComercial, txtCentroComercial, 3);
        
        // UpsideDown - Alcantarillado
        mostrarNinosEnZona(sistema.alcantarillado, txtAlcantarillado, 3);
        
        // Colmena
        mostrarNinosEnColmena();
        
        // Portales
        mostrarPortales();
        
        // Demogorgones
        mostrarDemogorgonsPorZona();        
        
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
    
    private void mostrarNinosEnZona(Zona zona, javax.swing.JTextArea textArea, int TamLinea) {
        List<Child> ninos = zona.getNinosPresentes();
        StringBuilder sb = new StringBuilder();
        int contador = 0;
        int porLinea = TamLinea;  // Número de niños por línea

        for (Child nino : ninos) {
            sb.append(nino.getNinoId());
            contador++;
            if (contador % porLinea == 0) {
                sb.append("\n");
            } else {
                sb.append("   ");
            }
        }

        textArea.setText(sb.toString());
    }
    
    private void mostrarNinosEnColmena() {
        lblColmena.setText(String.valueOf(sistema.colmena.getCantidadNinos()));
    }
    
    public GUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        PanelGeneral = new javax.swing.JPanel();
        PanelHawkins = new javax.swing.JPanel();
        PanelCallePrincipal = new javax.swing.JPanel();
        lblRadioWSQK = new javax.swing.JLabel();
        ScrollRadioWSQK = new javax.swing.JScrollPane();
        txtRadioWSQK = new javax.swing.JTextArea();
        PanelSotanoByers = new javax.swing.JPanel();
        lblSotanoByers = new javax.swing.JLabel();
        ScrollSotanoByers = new javax.swing.JScrollPane();
        txtSotanoByers = new javax.swing.JTextArea();
        PanelRadioWSQK = new javax.swing.JPanel();
        lblCallePrincipal = new javax.swing.JLabel();
        SrollCallePrincipal = new javax.swing.JScrollPane();
        txtCallePrincipal = new javax.swing.JTextArea();
        lblTituloHawkins = new javax.swing.JLabel();
        PanelUD = new javax.swing.JPanel();
        lblTituloUD = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDemogorgonsBosque = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtBosque = new javax.swing.JTextArea();
        lblTituloColmena = new javax.swing.JLabel();
        lblColmena = new javax.swing.JLabel();
        lblBosque = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDemogorgonsLaboratorio = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtLaboratorio = new javax.swing.JTextArea();
        lblLaboratorio = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtDemogorgonsCentro = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtCentroComercial = new javax.swing.JTextArea();
        lblCentroComercial = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtDemogorgonsAlcantarillado = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtAlcantarillado = new javax.swing.JTextArea();
        lblAlcantarillado = new javax.swing.JLabel();
        PanelPortales = new javax.swing.JPanel();
        lblTituloPortales = new javax.swing.JLabel();
        SrollBosqueI = new javax.swing.JScrollPane();
        txtPortalBosqueSalida = new javax.swing.JTextArea();
        SrollBosqueV = new javax.swing.JScrollPane();
        txtPortalBosqueRegreso = new javax.swing.JTextArea();
        lblPortalBosque = new javax.swing.JLabel();
        lblPortalLaboratorio = new javax.swing.JLabel();
        SrollLaboratorioV = new javax.swing.JScrollPane();
        txtPortalLaboratorioRegreso = new javax.swing.JTextArea();
        SrollLaboratorioI = new javax.swing.JScrollPane();
        txtPortalLaboratorioSalida = new javax.swing.JTextArea();
        lblPortalCentroComercial = new javax.swing.JLabel();
        SrollCentroComercialV = new javax.swing.JScrollPane();
        txtPortalCentroRegreso = new javax.swing.JTextArea();
        SrollCentroComercialI = new javax.swing.JScrollPane();
        txtPortalCentroSalida = new javax.swing.JTextArea();
        lblPortalAlcantarillado = new javax.swing.JLabel();
        SrollAlcantarilladoV = new javax.swing.JScrollPane();
        txtPortalAlcantarilladoRegreso = new javax.swing.JTextArea();
        SrollAlcantarilladoI = new javax.swing.JScrollPane();
        txtPortalAlcantarilladoSalida = new javax.swing.JTextArea();
        PanelSangre = new javax.swing.JPanel();
        lblTituloSangre = new javax.swing.JLabel();
        PanelSubSangre = new javax.swing.JPanel();
        lblSangre = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 4, true));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 272, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 472, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulación BATALLA POR HAWKINS");
        setBackground(null);
        setPreferredSize(new java.awt.Dimension(1024, 640));
        getContentPane().setLayout(null);

        PanelGeneral.setBackground(new java.awt.Color(0, 0, 0));
        PanelGeneral.setLayout(null);

        PanelHawkins.setBackground(new java.awt.Color(0, 0, 0));
        PanelHawkins.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 4, true));
        PanelHawkins.setLayout(null);

        PanelCallePrincipal.setBackground(new java.awt.Color(51, 51, 51));
        PanelCallePrincipal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        PanelCallePrincipal.setLayout(null);

        lblRadioWSQK.setFont(new java.awt.Font("Book Antiqua", 0, 14)); // NOI18N
        lblRadioWSQK.setForeground(new java.awt.Color(0, 204, 0));
        lblRadioWSQK.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblRadioWSQK.setText("RADIO WSQK");
        PanelCallePrincipal.add(lblRadioWSQK);
        lblRadioWSQK.setBounds(10, 0, 240, 20);

        ScrollRadioWSQK.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        ScrollRadioWSQK.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtRadioWSQK.setEditable(false);
        txtRadioWSQK.setBackground(new java.awt.Color(0, 0, 0));
        txtRadioWSQK.setColumns(15);
        txtRadioWSQK.setFont(new java.awt.Font("Book Antiqua", 0, 12)); // NOI18N
        txtRadioWSQK.setForeground(new java.awt.Color(0, 153, 0));
        txtRadioWSQK.setRows(5);
        ScrollRadioWSQK.setViewportView(txtRadioWSQK);

        PanelCallePrincipal.add(ScrollRadioWSQK);
        ScrollRadioWSQK.setBounds(0, 20, 260, 110);

        PanelHawkins.add(PanelCallePrincipal);
        PanelCallePrincipal.setBounds(10, 320, 260, 130);

        PanelSotanoByers.setBackground(new java.awt.Color(51, 51, 51));
        PanelSotanoByers.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        PanelSotanoByers.setLayout(null);

        lblSotanoByers.setFont(new java.awt.Font("Book Antiqua", 0, 14)); // NOI18N
        lblSotanoByers.setForeground(new java.awt.Color(0, 204, 0));
        lblSotanoByers.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblSotanoByers.setText("SOTANO BYERS");
        PanelSotanoByers.add(lblSotanoByers);
        lblSotanoByers.setBounds(10, 0, 240, 20);

        ScrollSotanoByers.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        ScrollSotanoByers.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtSotanoByers.setEditable(false);
        txtSotanoByers.setBackground(new java.awt.Color(0, 0, 0));
        txtSotanoByers.setColumns(15);
        txtSotanoByers.setFont(new java.awt.Font("Book Antiqua", 0, 12)); // NOI18N
        txtSotanoByers.setForeground(new java.awt.Color(0, 153, 0));
        txtSotanoByers.setRows(5);
        ScrollSotanoByers.setViewportView(txtSotanoByers);

        PanelSotanoByers.add(ScrollSotanoByers);
        ScrollSotanoByers.setBounds(0, 20, 260, 110);

        PanelHawkins.add(PanelSotanoByers);
        PanelSotanoByers.setBounds(10, 180, 260, 130);

        PanelRadioWSQK.setBackground(new java.awt.Color(51, 51, 51));
        PanelRadioWSQK.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        PanelRadioWSQK.setLayout(null);

        lblCallePrincipal.setFont(new java.awt.Font("Book Antiqua", 0, 14)); // NOI18N
        lblCallePrincipal.setForeground(new java.awt.Color(0, 204, 0));
        lblCallePrincipal.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCallePrincipal.setText("CALLE PRINCIPAL");
        PanelRadioWSQK.add(lblCallePrincipal);
        lblCallePrincipal.setBounds(10, 0, 240, 20);

        SrollCallePrincipal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        SrollCallePrincipal.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtCallePrincipal.setEditable(false);
        txtCallePrincipal.setBackground(new java.awt.Color(0, 0, 0));
        txtCallePrincipal.setColumns(15);
        txtCallePrincipal.setFont(new java.awt.Font("Book Antiqua", 0, 12)); // NOI18N
        txtCallePrincipal.setForeground(new java.awt.Color(0, 153, 0));
        txtCallePrincipal.setRows(5);
        SrollCallePrincipal.setViewportView(txtCallePrincipal);

        PanelRadioWSQK.add(SrollCallePrincipal);
        SrollCallePrincipal.setBounds(0, 20, 260, 110);

        PanelHawkins.add(PanelRadioWSQK);
        PanelRadioWSQK.setBounds(10, 40, 260, 130);

        lblTituloHawkins.setFont(new java.awt.Font("Book Antiqua", 0, 18)); // NOI18N
        lblTituloHawkins.setForeground(new java.awt.Color(0, 153, 0));
        lblTituloHawkins.setText("HAWKINS");
        PanelHawkins.add(lblTituloHawkins);
        lblTituloHawkins.setBounds(20, 10, 110, 20);

        PanelGeneral.add(PanelHawkins);
        PanelHawkins.setBounds(10, 20, 280, 460);

        PanelUD.setBackground(new java.awt.Color(0, 0, 0));
        PanelUD.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 4, true));
        PanelUD.setLayout(null);

        lblTituloUD.setFont(new java.awt.Font("Book Antiqua", 0, 18)); // NOI18N
        lblTituloUD.setForeground(new java.awt.Color(153, 0, 0));
        lblTituloUD.setText("UPSIDE DOWN ");
        PanelUD.add(lblTituloUD);
        lblTituloUD.setBounds(10, 30, 140, 40);

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 2, true));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtDemogorgonsBosque.setEditable(false);
        txtDemogorgonsBosque.setBackground(new java.awt.Color(0, 0, 0));
        txtDemogorgonsBosque.setColumns(10);
        txtDemogorgonsBosque.setForeground(new java.awt.Color(153, 0, 0));
        txtDemogorgonsBosque.setRows(5);
        jScrollPane1.setViewportView(txtDemogorgonsBosque);

        PanelUD.add(jScrollPane1);
        jScrollPane1.setBounds(160, 120, 140, 100);

        jScrollPane2.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 2, true));
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtBosque.setEditable(false);
        txtBosque.setBackground(new java.awt.Color(0, 0, 0));
        txtBosque.setColumns(10);
        txtBosque.setForeground(new java.awt.Color(153, 0, 0));
        txtBosque.setRows(5);
        jScrollPane2.setViewportView(txtBosque);

        PanelUD.add(jScrollPane2);
        jScrollPane2.setBounds(20, 120, 140, 100);

        lblTituloColmena.setFont(new java.awt.Font("Book Antiqua", 0, 14)); // NOI18N
        lblTituloColmena.setForeground(new java.awt.Color(153, 0, 0));
        lblTituloColmena.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloColmena.setText("COLMENA");
        PanelUD.add(lblTituloColmena);
        lblTituloColmena.setBounds(200, 10, 100, 20);

        lblColmena.setFont(new java.awt.Font("Book Antiqua", 0, 18)); // NOI18N
        lblColmena.setForeground(new java.awt.Color(153, 0, 0));
        lblColmena.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblColmena.setText("0");
        lblColmena.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 2, true));
        PanelUD.add(lblColmena);
        lblColmena.setBounds(200, 30, 100, 80);

        lblBosque.setFont(new java.awt.Font("Book Antiqua", 0, 14)); // NOI18N
        lblBosque.setForeground(new java.awt.Color(153, 0, 0));
        lblBosque.setText("BOSQUE");
        PanelUD.add(lblBosque);
        lblBosque.setBounds(30, 100, 130, 20);

        jScrollPane3.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 2, true));
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtDemogorgonsLaboratorio.setEditable(false);
        txtDemogorgonsLaboratorio.setBackground(new java.awt.Color(0, 0, 0));
        txtDemogorgonsLaboratorio.setColumns(10);
        txtDemogorgonsLaboratorio.setForeground(new java.awt.Color(153, 0, 0));
        txtDemogorgonsLaboratorio.setRows(5);
        jScrollPane3.setViewportView(txtDemogorgonsLaboratorio);

        PanelUD.add(jScrollPane3);
        jScrollPane3.setBounds(160, 240, 140, 100);

        jScrollPane4.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 2, true));
        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtLaboratorio.setEditable(false);
        txtLaboratorio.setBackground(new java.awt.Color(0, 0, 0));
        txtLaboratorio.setColumns(10);
        txtLaboratorio.setForeground(new java.awt.Color(153, 0, 0));
        txtLaboratorio.setRows(5);
        jScrollPane4.setViewportView(txtLaboratorio);

        PanelUD.add(jScrollPane4);
        jScrollPane4.setBounds(20, 240, 140, 100);

        lblLaboratorio.setFont(new java.awt.Font("Book Antiqua", 0, 14)); // NOI18N
        lblLaboratorio.setForeground(new java.awt.Color(153, 0, 0));
        lblLaboratorio.setText("LABORATORIO");
        PanelUD.add(lblLaboratorio);
        lblLaboratorio.setBounds(30, 220, 130, 20);

        jScrollPane5.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 2, true));
        jScrollPane5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtDemogorgonsCentro.setEditable(false);
        txtDemogorgonsCentro.setBackground(new java.awt.Color(0, 0, 0));
        txtDemogorgonsCentro.setColumns(10);
        txtDemogorgonsCentro.setForeground(new java.awt.Color(153, 0, 0));
        txtDemogorgonsCentro.setRows(5);
        jScrollPane5.setViewportView(txtDemogorgonsCentro);

        PanelUD.add(jScrollPane5);
        jScrollPane5.setBounds(160, 360, 140, 100);

        jScrollPane6.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 2, true));
        jScrollPane6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtCentroComercial.setEditable(false);
        txtCentroComercial.setBackground(new java.awt.Color(0, 0, 0));
        txtCentroComercial.setColumns(10);
        txtCentroComercial.setForeground(new java.awt.Color(153, 0, 0));
        txtCentroComercial.setRows(5);
        jScrollPane6.setViewportView(txtCentroComercial);

        PanelUD.add(jScrollPane6);
        jScrollPane6.setBounds(20, 360, 140, 100);

        lblCentroComercial.setFont(new java.awt.Font("Book Antiqua", 0, 14)); // NOI18N
        lblCentroComercial.setForeground(new java.awt.Color(153, 0, 0));
        lblCentroComercial.setText("CENTRO COMERCIAL");
        PanelUD.add(lblCentroComercial);
        lblCentroComercial.setBounds(30, 340, 170, 20);

        jScrollPane7.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 2, true));
        jScrollPane7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtDemogorgonsAlcantarillado.setEditable(false);
        txtDemogorgonsAlcantarillado.setBackground(new java.awt.Color(0, 0, 0));
        txtDemogorgonsAlcantarillado.setColumns(10);
        txtDemogorgonsAlcantarillado.setForeground(new java.awt.Color(153, 0, 0));
        txtDemogorgonsAlcantarillado.setRows(5);
        jScrollPane7.setViewportView(txtDemogorgonsAlcantarillado);

        PanelUD.add(jScrollPane7);
        jScrollPane7.setBounds(160, 480, 140, 100);

        jScrollPane8.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 2, true));
        jScrollPane8.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtAlcantarillado.setEditable(false);
        txtAlcantarillado.setBackground(new java.awt.Color(0, 0, 0));
        txtAlcantarillado.setColumns(10);
        txtAlcantarillado.setForeground(new java.awt.Color(153, 0, 0));
        txtAlcantarillado.setRows(5);
        jScrollPane8.setViewportView(txtAlcantarillado);

        PanelUD.add(jScrollPane8);
        jScrollPane8.setBounds(20, 480, 140, 100);

        lblAlcantarillado.setFont(new java.awt.Font("Book Antiqua", 0, 14)); // NOI18N
        lblAlcantarillado.setForeground(new java.awt.Color(153, 0, 0));
        lblAlcantarillado.setText("ALCANTARILLADO");
        PanelUD.add(lblAlcantarillado);
        lblAlcantarillado.setBounds(30, 460, 160, 20);

        PanelGeneral.add(PanelUD);
        PanelUD.setBounds(690, 20, 320, 600);

        PanelPortales.setBackground(new java.awt.Color(0, 0, 0));
        PanelPortales.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 4, true));
        PanelPortales.setLayout(null);

        lblTituloPortales.setFont(new java.awt.Font("Book Antiqua", 0, 18)); // NOI18N
        lblTituloPortales.setForeground(new java.awt.Color(0, 153, 0));
        lblTituloPortales.setText("PORTALES");
        PanelPortales.add(lblTituloPortales);
        lblTituloPortales.setBounds(20, 10, 150, 20);

        SrollBosqueI.setBackground(new java.awt.Color(0, 0, 0));
        SrollBosqueI.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        SrollBosqueI.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtPortalBosqueSalida.setEditable(false);
        txtPortalBosqueSalida.setBackground(new java.awt.Color(0, 0, 0));
        txtPortalBosqueSalida.setColumns(5);
        txtPortalBosqueSalida.setForeground(new java.awt.Color(0, 153, 0));
        txtPortalBosqueSalida.setRows(5);
        SrollBosqueI.setViewportView(txtPortalBosqueSalida);

        PanelPortales.add(SrollBosqueI);
        SrollBosqueI.setBounds(20, 40, 100, 100);

        SrollBosqueV.setBackground(new java.awt.Color(0, 0, 0));
        SrollBosqueV.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 0), 2, true));
        SrollBosqueV.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtPortalBosqueRegreso.setEditable(false);
        txtPortalBosqueRegreso.setBackground(new java.awt.Color(0, 0, 0));
        txtPortalBosqueRegreso.setColumns(5);
        txtPortalBosqueRegreso.setForeground(new java.awt.Color(153, 153, 0));
        txtPortalBosqueRegreso.setRows(5);
        SrollBosqueV.setViewportView(txtPortalBosqueRegreso);

        PanelPortales.add(SrollBosqueV);
        SrollBosqueV.setBounds(240, 40, 100, 100);

        lblPortalBosque.setForeground(new java.awt.Color(0, 153, 0));
        lblPortalBosque.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        PanelPortales.add(lblPortalBosque);
        lblPortalBosque.setBounds(130, 70, 100, 30);

        lblPortalLaboratorio.setForeground(new java.awt.Color(0, 153, 0));
        lblPortalLaboratorio.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        PanelPortales.add(lblPortalLaboratorio);
        lblPortalLaboratorio.setBounds(130, 190, 100, 30);

        SrollLaboratorioV.setBackground(new java.awt.Color(0, 0, 0));
        SrollLaboratorioV.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 0), 2, true));
        SrollLaboratorioV.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtPortalLaboratorioRegreso.setEditable(false);
        txtPortalLaboratorioRegreso.setBackground(new java.awt.Color(0, 0, 0));
        txtPortalLaboratorioRegreso.setColumns(5);
        txtPortalLaboratorioRegreso.setForeground(new java.awt.Color(153, 153, 0));
        txtPortalLaboratorioRegreso.setRows(5);
        SrollLaboratorioV.setViewportView(txtPortalLaboratorioRegreso);

        PanelPortales.add(SrollLaboratorioV);
        SrollLaboratorioV.setBounds(240, 160, 100, 100);

        SrollLaboratorioI.setBackground(new java.awt.Color(0, 0, 0));
        SrollLaboratorioI.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        SrollLaboratorioI.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtPortalLaboratorioSalida.setEditable(false);
        txtPortalLaboratorioSalida.setBackground(new java.awt.Color(0, 0, 0));
        txtPortalLaboratorioSalida.setColumns(5);
        txtPortalLaboratorioSalida.setForeground(new java.awt.Color(0, 153, 0));
        txtPortalLaboratorioSalida.setRows(5);
        SrollLaboratorioI.setViewportView(txtPortalLaboratorioSalida);

        PanelPortales.add(SrollLaboratorioI);
        SrollLaboratorioI.setBounds(20, 160, 100, 100);

        lblPortalCentroComercial.setForeground(new java.awt.Color(0, 153, 0));
        lblPortalCentroComercial.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        PanelPortales.add(lblPortalCentroComercial);
        lblPortalCentroComercial.setBounds(130, 310, 100, 30);

        SrollCentroComercialV.setBackground(new java.awt.Color(0, 0, 0));
        SrollCentroComercialV.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 0), 2, true));
        SrollCentroComercialV.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtPortalCentroRegreso.setEditable(false);
        txtPortalCentroRegreso.setBackground(new java.awt.Color(0, 0, 0));
        txtPortalCentroRegreso.setColumns(5);
        txtPortalCentroRegreso.setForeground(new java.awt.Color(153, 153, 0));
        txtPortalCentroRegreso.setRows(5);
        SrollCentroComercialV.setViewportView(txtPortalCentroRegreso);

        PanelPortales.add(SrollCentroComercialV);
        SrollCentroComercialV.setBounds(240, 280, 100, 100);

        SrollCentroComercialI.setBackground(new java.awt.Color(0, 0, 0));
        SrollCentroComercialI.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        SrollCentroComercialI.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtPortalCentroSalida.setEditable(false);
        txtPortalCentroSalida.setBackground(new java.awt.Color(0, 0, 0));
        txtPortalCentroSalida.setColumns(5);
        txtPortalCentroSalida.setForeground(new java.awt.Color(0, 153, 0));
        txtPortalCentroSalida.setRows(5);
        SrollCentroComercialI.setViewportView(txtPortalCentroSalida);

        PanelPortales.add(SrollCentroComercialI);
        SrollCentroComercialI.setBounds(20, 280, 100, 100);

        lblPortalAlcantarillado.setForeground(new java.awt.Color(0, 153, 0));
        lblPortalAlcantarillado.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        PanelPortales.add(lblPortalAlcantarillado);
        lblPortalAlcantarillado.setBounds(130, 430, 100, 30);

        SrollAlcantarilladoV.setBackground(new java.awt.Color(0, 0, 0));
        SrollAlcantarilladoV.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 0), 2, true));
        SrollAlcantarilladoV.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtPortalAlcantarilladoRegreso.setEditable(false);
        txtPortalAlcantarilladoRegreso.setBackground(new java.awt.Color(0, 0, 0));
        txtPortalAlcantarilladoRegreso.setColumns(5);
        txtPortalAlcantarilladoRegreso.setForeground(new java.awt.Color(153, 153, 0));
        txtPortalAlcantarilladoRegreso.setRows(5);
        SrollAlcantarilladoV.setViewportView(txtPortalAlcantarilladoRegreso);

        PanelPortales.add(SrollAlcantarilladoV);
        SrollAlcantarilladoV.setBounds(240, 400, 100, 100);

        SrollAlcantarilladoI.setBackground(new java.awt.Color(0, 0, 0));
        SrollAlcantarilladoI.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        SrollAlcantarilladoI.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtPortalAlcantarilladoSalida.setEditable(false);
        txtPortalAlcantarilladoSalida.setBackground(new java.awt.Color(0, 0, 0));
        txtPortalAlcantarilladoSalida.setColumns(5);
        txtPortalAlcantarilladoSalida.setForeground(new java.awt.Color(0, 153, 0));
        txtPortalAlcantarilladoSalida.setRows(5);
        SrollAlcantarilladoI.setViewportView(txtPortalAlcantarilladoSalida);

        PanelPortales.add(SrollAlcantarilladoI);
        SrollAlcantarilladoI.setBounds(20, 400, 100, 100);

        PanelGeneral.add(PanelPortales);
        PanelPortales.setBounds(310, 100, 360, 520);

        PanelSangre.setBackground(new java.awt.Color(0, 0, 0));
        PanelSangre.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 4, true));
        PanelSangre.setLayout(null);

        lblTituloSangre.setFont(new java.awt.Font("Book Antiqua", 0, 18)); // NOI18N
        lblTituloSangre.setForeground(new java.awt.Color(153, 0, 0));
        lblTituloSangre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloSangre.setText("SANGRE");
        PanelSangre.add(lblTituloSangre);
        lblTituloSangre.setBounds(20, 10, 140, 30);

        PanelSubSangre.setBackground(new java.awt.Color(0, 0, 0));
        PanelSubSangre.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 4, true));

        lblSangre.setFont(new java.awt.Font("Book Antiqua", 0, 18)); // NOI18N
        lblSangre.setForeground(new java.awt.Color(153, 0, 0));
        lblSangre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSangre.setText("0");

        javax.swing.GroupLayout PanelSubSangreLayout = new javax.swing.GroupLayout(PanelSubSangre);
        PanelSubSangre.setLayout(PanelSubSangreLayout);
        PanelSubSangreLayout.setHorizontalGroup(
            PanelSubSangreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSubSangreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSangre, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelSubSangreLayout.setVerticalGroup(
            PanelSubSangreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSubSangreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSangre, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                .addContainerGap())
        );

        PanelSangre.add(PanelSubSangre);
        PanelSubSangre.setBounds(0, 40, 180, 90);

        PanelGeneral.add(PanelSangre);
        PanelSangre.setBounds(60, 490, 180, 130);

        lblTitulo.setFont(new java.awt.Font("Book Antiqua", 0, 24)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(0, 153, 0));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("LA BATALLA POR HAWKINS");
        PanelGeneral.add(lblTitulo);
        lblTitulo.setBounds(300, 20, 370, 70);

        getContentPane().add(PanelGeneral);
        PanelGeneral.setBounds(0, 0, 1020, 640);

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
    private javax.swing.JPanel PanelCallePrincipal;
    private javax.swing.JPanel PanelGeneral;
    private javax.swing.JPanel PanelHawkins;
    private javax.swing.JPanel PanelPortales;
    private javax.swing.JPanel PanelRadioWSQK;
    private javax.swing.JPanel PanelSangre;
    private javax.swing.JPanel PanelSotanoByers;
    private javax.swing.JPanel PanelSubSangre;
    private javax.swing.JPanel PanelUD;
    private javax.swing.JScrollPane ScrollRadioWSQK;
    private javax.swing.JScrollPane ScrollSotanoByers;
    private javax.swing.JScrollPane SrollAlcantarilladoI;
    private javax.swing.JScrollPane SrollAlcantarilladoV;
    private javax.swing.JScrollPane SrollBosqueI;
    private javax.swing.JScrollPane SrollBosqueV;
    private javax.swing.JScrollPane SrollCallePrincipal;
    private javax.swing.JScrollPane SrollCentroComercialI;
    private javax.swing.JScrollPane SrollCentroComercialV;
    private javax.swing.JScrollPane SrollLaboratorioI;
    private javax.swing.JScrollPane SrollLaboratorioV;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JLabel lblAlcantarillado;
    private javax.swing.JLabel lblBosque;
    private javax.swing.JLabel lblCallePrincipal;
    private javax.swing.JLabel lblCentroComercial;
    private javax.swing.JLabel lblColmena;
    private javax.swing.JLabel lblLaboratorio;
    private javax.swing.JLabel lblPortalAlcantarillado;
    private javax.swing.JLabel lblPortalBosque;
    private javax.swing.JLabel lblPortalCentroComercial;
    private javax.swing.JLabel lblPortalLaboratorio;
    private javax.swing.JLabel lblRadioWSQK;
    private javax.swing.JLabel lblSangre;
    private javax.swing.JLabel lblSotanoByers;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTituloColmena;
    private javax.swing.JLabel lblTituloHawkins;
    private javax.swing.JLabel lblTituloPortales;
    private javax.swing.JLabel lblTituloSangre;
    private javax.swing.JLabel lblTituloUD;
    private javax.swing.JTextArea txtAlcantarillado;
    private javax.swing.JTextArea txtBosque;
    private javax.swing.JTextArea txtCallePrincipal;
    private javax.swing.JTextArea txtCentroComercial;
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
    private javax.swing.JTextArea txtSotanoByers;
    // End of variables declaration//GEN-END:variables
}
