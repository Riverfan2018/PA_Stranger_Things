package PA;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Sistemageneral {
    private final java.util.concurrent.atomic.AtomicInteger total_capturas = new java.util.concurrent.atomic.AtomicInteger(0);
    private final java.util.concurrent.atomic.AtomicInteger sangre_total = new java.util.concurrent.atomic.AtomicInteger(0);
    
    public final Colmena colmena;
    
    // Eventos
    public volatile boolean red_mental_on = false;
    
    public volatile boolean eleveen_enfadada = false;
    
    public volatile boolean hayApagon = false;
    
    private final AtomicBoolean tormentaActiva = new AtomicBoolean(false);
    
    // Zonas Hola soy German
    public final Zona callePrincipal;
    public final Zona sotanoByers;
    public final Zona radioWSQK;
    
    // Zonas UP
    public final Zona bosque;
    public final Zona laboratorio;
    public final Zona centroComercial;
    public final Zona alcantarillado;
    
    // Portales al Nether
    public final Portal portalBosque;
    public final Portal portalLaboratorio;
    public final Portal portalCentroComercial;
    public final Portal portalAlcantarillado;
    
    
    private final List<Demogorgon> demogorgons;
    
    private final Logger logger;
    
    private volatile boolean pausado = false;
    private long tiempoFinEvento = 0;
    private String nombreEventoActual = "";
    
    public Sistemageneral() {
        
        

        callePrincipal = new Zona(Zona.TipoZona.CALLE_PRINCIPAL);
        sotanoByers = new Zona(Zona.TipoZona.SOTANO_BYERS);
        radioWSQK = new Zona(Zona.TipoZona.RADIO_WSQK);
        
        bosque = new Zona(Zona.TipoZona.BOSQUE);
        laboratorio = new Zona(Zona.TipoZona.LABORATORIO);
        centroComercial = new Zona(Zona.TipoZona.CENTRO_COMERCIAL);
        alcantarillado = new Zona(Zona.TipoZona.ALCANTARILLADO);
        
        // tamaños: Bosque=2, Laboratorio=3, Centro=4, Alcantarillado=2
        portalBosque = new Portal("Bosque", 2, bosque, this);
        portalLaboratorio = new Portal("Laboratorio", 3, laboratorio, this);
        portalCentroComercial = new Portal("Centro Comercial", 4, centroComercial, this);
        portalAlcantarillado = new Portal("Alcantarillado", 2, alcantarillado, this);
        
        this.colmena = new Colmena();
        
        demogorgons = new ArrayList<>();
        
        this.logger = Logger.getInstance();
    }
    
    public Portal seleccionarPortalAleatorio() {
            Portal[] portales = {
                portalBosque, portalLaboratorio, 
                portalCentroComercial, portalAlcantarillado
            };
            int index = (int) (Math.random() * portales.length);
            return portales[index];
    }
    
    public boolean tormentaon() {
        return tormentaActiva.get();
    }
    
   
    public void setTormentaActiva(boolean activa) {
        tormentaActiva.set(activa);
    }
    
    public void agregarDemogorgon(Demogorgon d) {
        demogorgons.add(d); // The big Dih
    }
    
    public List<Demogorgon> getDemogorgons() {
        return new ArrayList<>(demogorgons);
    }
    
    public void setHayApagon(boolean estado) {
        this.hayApagon = estado;
        if (!estado) {
            //Hay que avisar a los portales que la luz ha vuelto
            portalBosque.despertarPorLuz();
            portalLaboratorio.despertarPorLuz();
            portalCentroComercial.despertarPorLuz();
            portalAlcantarillado.despertarPorLuz();
        }
    }
    public void eleveen() {
        this.eleveen_enfadada = true;
        System.out.println("¡La cria con poderes está rara! Los bichos se han quedado tiesos.");

        int rescatados = 0;
        while (colmena.hayNinos()) {
            Child chavalito = colmena.seleccionarNinoAleatorio();
            if (chavalito != null) {
                colmena.salir(chavalito);      
                chavalito.setcapturado(false); 
                callePrincipal.entrar(chavalito); 
                rescatados++;
            }
        }
        System.out.println("Eleven ha rescatado a " + rescatados + " chavales.");

        // Hilo temporal para que Eleven duré 5 s
        new Thread(() -> {
            try {
                Thread.sleep(5000); 
                this.eleveen_enfadada = false;
                System.out.println("Eleven se ha cansado. Los Demogorgons vuelven a la caza...");
            } catch (InterruptedException e) { e.printStackTrace(); }
        }).start();
    }
    
    // Método para iniciar el sistema (crear niños escalonados y demogorgon alpha Auuuuu!)
    public void iniciar() {
        Demogorgon alpha = new Demogorgon("D0000", this);
        demogorgons.add(alpha);
        alpha.start();

        Thread creadorNiños = new Thread(() -> {
            for (int i = 1; i <= 1500; i++) {
                // VERIFICAR PAUSA ANTES DE CREAR CADA NIÑO
                while (isPausado()) {
                    synchronized (this) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }

                String id = String.format("N%04d", i);
                Child nino = new Child(id, this);
                nino.start();

                try {
                    long espera = (long) (500 + Math.random() * 1500);
                    Thread.sleep(espera);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        creadorNiños.start();
    }
    
    
    public Zona get_sitio_feo_random() {
        Zona[] sitios = {bosque, laboratorio, centroComercial, alcantarillado};
        return sitios[(int) (Math.random() * sitios.length)];
    }

    //Para el evento de Red Mental 
    public Zona get_zona_con_mas_carne() {
        Zona[] sitios = {bosque, laboratorio, centroComercial, alcantarillado};
        Zona top = sitios[0];
        for (Zona z : sitios) {
            if (z.getCantidadNinos() > top.getCantidadNinos()) {
                top = z;
            }
        }
        return top;
    }

    //Para registrar capturas y que Vecna cree más bichos cada 8 veces
    public synchronized void aviso_a_vecna() {
       
        int cuenta = total_capturas.incrementAndGet();
        if (cuenta % 8 == 0) {
            String id_nuevo = String.format("D%04d", demogorgons.size());
            Demogorgon nuevo = new Demogorgon(id_nuevo, this);
            demogorgons.add(nuevo);
            nuevo.start();
            logger.log("VECNA HA ENVIADO A: " + id_nuevo);
            System.out.println("VECNA HA ENVIADO A: " + id_nuevo);
        }
    }
    public void sumar_sangre() {
        sangre_total.incrementAndGet();
    }

    public int get_sangre_recolectada() {
        return sangre_total.get(); 
    }
    
    public void lanzar_ciclo_eventos() {
        Thread gestor = new Thread(() -> {
            java.util.Random rnd = new java.util.Random();
            while (true) {
                try {
                    // Verificar si el sistema está pausado
                    while (isPausado()) {
                        synchronized (this) {
                            this.wait();
                        }
                    }
                    
                    //Espera aleatoria de 30 a 60 segundos
                    long espera = 30000 + rnd.nextInt(30001);
                    logger.log("Próximo evento en " + (espera / 1000) + " segundos");
                    System.out.println("Próximo evento en " + (espera / 1000) + " segundos");
                    
                    // Espera en intervalos pequeños para verificar pausa
                    long tiempoRestante = espera;
                    while (tiempoRestante > 0 && !isPausado()) {
                        long paso = Math.min(1000, tiempoRestante);
                        Thread.sleep(paso);
                        tiempoRestante -= paso;
                    }

                    // Si se pausó durante la espera, saltar este evento
                    if (isPausado()) {
                        continue;
                    }

                    // Elegir evento (0: Apagón, 1: Tormenta, 2: Eleven, 3: Red Mental)
                    int evento = rnd.nextInt(4);
                    ejecutar_evento(evento);

                } catch (InterruptedException e) { break; }
            }
        });
        gestor.setDaemon(true);
        gestor.start();
    }
    
    private void ejecutar_evento(int tipo) throws InterruptedException {
        java.util.Random rnd = new java.util.Random();
        long duracion = 5000 + rnd.nextInt(5001);

        switch (tipo) {
            case 0: // APAGÓN
                iniciarEvento("APAGÓN", duracion);
                logger.log("EVENTO GLOBAL: Apagón del Laboratorio iniciado");
                System.out.println(" EVENTO: APAGÓN DEL LABORATORIO");
                this.hayApagon = true;

                // Esperar con verificación de pausa
                long tiempoRestante = duracion;
                while (tiempoRestante > 0 && !isPausado()) {
                    long paso = Math.min(1000, tiempoRestante);
                    Thread.sleep(paso);
                    tiempoRestante -= paso;
                }

                // Si se pausó, al reanudar continuar el evento
                if (isPausado()) {
                    // Guardar el tiempo restante y esperar reanudación
                    synchronized (this) {
                        this.wait();
                    }
                }

                this.hayApagon = false;
                despertar_portales();
                logger.log("EVENTO GLOBAL: Apagón del Laboratorio finalizado");
                System.out.println("Apagón finalizado");
                break;

            case 1: // TORMENTA
                iniciarEvento("TORMENTA", duracion);
                logger.log("EVENTO GLOBAL: Tormenta del Upside Down iniciada");
                System.out.println("EVENTO: TORMENTA DEL UPSIDE DOWN");
                this.setTormentaActiva(true);

                tiempoRestante = duracion;
                while (tiempoRestante > 0 && !isPausado()) {
                    long paso = Math.min(1000, tiempoRestante);
                    Thread.sleep(paso);
                    tiempoRestante -= paso;
                }

                if (isPausado()) {
                    synchronized (this) {
                        this.wait();
                    }
                }

                this.setTormentaActiva(false);
                logger.log("EVENTO GLOBAL: Tormenta del Upside Down finalizada");
                System.out.println("Tormenta finalizada");
                break;

            case 2: // ELEVEN
                iniciarEvento("ELEVEN", duracion);
                logger.log("EVENTO GLOBAL: Intervención de Eleven iniciada");
                System.out.println("EVENTO: INTERVENCIÓN DE ELEVEN");
                this.eleveen_enfadada = true;

                int sangre = get_sangre_recolectada();
                int rescatados = 0;
                for (int i = 0; i < sangre && colmena.hayNinos(); i++) {
                    Child c = colmena.seleccionarNinoAleatorio();
                    if (c != null) {
                        colmena.salir(c);
                        c.setcapturado(false);
                        callePrincipal.entrar(c);
                        rescatados++;
                    }
                }
                logger.log("Eleven ha usado " + sangre + " unidades de sangre para rescatar a " + rescatados + " niños");
                System.out.println("Eleven ha usado " + sangre + " de sangre para rescatar a " + rescatados + " niños.");

                tiempoRestante = duracion;
                while (tiempoRestante > 0 && !isPausado()) {
                    long paso = Math.min(1000, tiempoRestante);
                    Thread.sleep(paso);
                    tiempoRestante -= paso;
                }

                if (isPausado()) {
                    synchronized (this) {
                        this.wait();
                    }
                }

                this.eleveen_enfadada = false;
                logger.log("EVENTO GLOBAL: Intervención de Eleven finalizada");
                break;

            case 3: // RED MENTAL
                iniciarEvento("RED_MENTAL", duracion);
                logger.log("EVENTO GLOBAL: Red Mental conectada");
                System.out.println("EVENTO: LA RED MENTAL");
                this.red_mental_on = true;

                tiempoRestante = duracion;
                while (tiempoRestante > 0 && !isPausado()) {
                    long paso = Math.min(1000, tiempoRestante);
                    Thread.sleep(paso);
                    tiempoRestante -= paso;
                }

                if (isPausado()) {
                    synchronized (this) {
                        this.wait();
                    }
                }

                this.red_mental_on = false;
                logger.log("EVENTO GLOBAL: Red Mental desconectada");
                System.out.println("Red mental desconectada");
                break;
        }
    }
    
    public void iniciarEvento(String nombre, long duracionMs) {
        this.nombreEventoActual = nombre;
        this.tiempoFinEvento = System.currentTimeMillis() + duracionMs;
    }

    public long getTiempoRestanteEvento() {
        if (tiempoFinEvento == 0) return 0;
        long restante = tiempoFinEvento - System.currentTimeMillis();
        return restante > 0 ? restante / 1000 : 0;
    }

    public String getNombreEventoActual() {
        return nombreEventoActual;
    }

    // Función auxiliar para despertar a los niños que esperan en portales
    private void despertar_portales() {
        portalBosque.despertarPorLuz();
        portalLaboratorio.despertarPorLuz();
        portalCentroComercial.despertarPorLuz();
        portalAlcantarillado.despertarPorLuz();
    }

    public Portal getPortalBosque() { return portalBosque; }
    public Portal getPortalLaboratorio() { return portalLaboratorio; }
    public Portal getPortalCentroComercial() { return portalCentroComercial; }
    public Portal getPortalAlcantarillado() { return portalAlcantarillado; }
    
    public synchronized void pausar() {
        this.pausado = true;
        logger.log("SISTEMA PAUSADO");
    }

    public synchronized void reanudar() {
        this.pausado = false;
        this.notifyAll();
        logger.log("SISTEMA REANUDADO");
    }
    public boolean isPausado() {
        return pausado;
    }
    
    public void sleepPausable(long tiempoMs) throws InterruptedException {
        if (tiempoMs <= 0) return;
        long fin = System.currentTimeMillis() + tiempoMs;
        while (System.currentTimeMillis() < fin) {
            if (isPausado()) {
                synchronized (this) {
                    this.wait();
                }
            }
            long resto = fin - System.currentTimeMillis();
            if (resto <= 0) break;
            Thread.sleep(Math.min(100, resto));
        }
    }
}
