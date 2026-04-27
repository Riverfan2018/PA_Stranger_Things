package PA;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Sistemageneral {
    private final java.util.concurrent.atomic.AtomicInteger total_capturas = new java.util.concurrent.atomic.AtomicInteger(0);
    private final java.util.concurrent.atomic.AtomicInteger sangre_total = new java.util.concurrent.atomic.AtomicInteger(0);
    
    public final Zona colmena;
    
    public volatile boolean red_mental_on = false;
    
    public volatile boolean eleveen_enfadada = false;
    
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
    
    public volatile boolean hayApagon = false;
    
    private final AtomicBoolean tormentaActiva = new AtomicBoolean(false);
    
    private final List<Demogorgon> demogorgons;
    
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
        
        demogorgons = new ArrayList<>();
        
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
            Child chavalito = colmena.seleccionarninoaleatorio();
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
                String id = String.format("N%04d", i);
                Child nino = new Child(id, this);
                nino.start();
                
                try {
                    // Esperar entre 0.5 y 2 segundos 
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
                    //Espera aleatoria de 30 a 60 segundos
                    long espera = 30000 + rnd.nextInt(30001);
                    System.out.println("Próximo evento en " + (espera / 1000) + " segundos");
                    Thread.sleep(espera);

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
        switch (tipo) {
            case 0: // APAGÓN
                System.out.println(" EVENTO: APAGÓN DEL LABORATORIO");
                this.hayApagon = true; // Bloquea portales y movimiento de bichos
                Thread.sleep(10000);   
                this.hayApagon = false;
                despertar_portales();   // Notifica a los niños que esperaban
                System.out.println("Apagón finalizado");
                break;

            case 1: // TORMENTA
                System.out.println("EVENTO: TORMENTA DEL UPSIDE DOWN");
                this.setTormentaActiva(true); // Duplica recolección y velocidad de bicho
                Thread.sleep(15000);
                this.setTormentaActiva(false);
                System.out.println("Tormenta finalizada");
                break;

            case 2: // ELEVEN
                System.out.println("EVENTO: INTERVENCIÓN DE ELEVEN");
                this.eleveen_enfadada = true; // Paraliza demogorgons

                // Liberar niños según sangre 
                int sangre = get_sangre_recolectada(); 
                int rescatados = 0;
                for (int i = 0; i < sangre && colmena.hayNinos(); i++) {
                    Child c = colmena.seleccionarninoaleatorio();
                    if (c != null) {
                        colmena.salir(c);
                        c.setcapturado(false);
                        callePrincipal.entrar(c);
                        rescatados++;
                    }
                }
                System.out.println("Eleven ha usado " + sangre + " de sangre para rescatar a " + rescatados + " niños.");
                Thread.sleep(5000); // Duración de la parálisis
                this.eleveen_enfadada = false;
                break;

            case 3: // RED MENTAL
                System.out.println("EVENTO: LA RED MENTAL");
                this.red_mental_on = true; // Demogorgons van a la zona con más niños
                Thread.sleep(12000);
                this.red_mental_on = false;
                System.out.println("Red mental desconectada");
                break;
        }
    }

    // Función auxiliar para despertar a los niños que esperan en portales
    private void despertar_portales() {
        portalBosque.despertarPorLuz();
        portalLaboratorio.despertarPorLuz();
        portalCentroComercial.despertarPorLuz();
        portalAlcantarillado.despertarPorLuz();
    }

    
    
}
