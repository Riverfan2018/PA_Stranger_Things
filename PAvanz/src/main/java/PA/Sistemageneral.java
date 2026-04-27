package PA;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Sistemageneral {
    private final java.util.concurrent.atomic.AtomicInteger total_capturas = new java.util.concurrent.atomic.AtomicInteger(0);
    
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
    public void eleveen() {
        
        this.eleveen_enfadada = true;
        System.out.println("¡Eleven está usando sus poderes! Los bichos se han quedado tiesos.");

        int rescatados = 0;
        while (colmena.hayNinos()) {
            Child chavalito = colmena.seleccionarninoaleatorio();
            if (chavalito != null) {
                colmena.salir(chavalito);      // Sale de la colmena
                chavalito.setcapturado(false);  // Se despierta (hace el notifyAll)
                callePrincipal.entrar(chavalito); // Vuelve a la civilización
                rescatados++;
            }
        }
        
        System.out.println("Eleven ha rescatado a " + rescatados + " chavales.");

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

    
    
}
