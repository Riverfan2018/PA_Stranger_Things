package PA;

public class Demogorgon extends Thread{
    private final String id;
    private final Sistemageneral sistema;
    private Zona zonaactual;
    private int contadorCapturas;
    
    public Demogorgon(String id, Sistemageneral sistema) {
        this.id = id;
        this.sistema = sistema;
        this.contadorCapturas = 0;
        Zona[] zonasUD = {
            sistema.bosque,
            sistema.laboratorio,
            sistema.centroComercial,
            sistema.alcantarillado
        }; // Hacer ruleta para ver donde empiezan a spawniar
        int index = (int) (Math.random() * zonasUD.length);
        this.zonaactual = zonasUD[index];
    }
    
    // Getters :-)
    public String getDemogorgonId() {
        return id;
    }
    
    public int getContadorCapturas() {
        return contadorCapturas;
    }
    
    public Zona getZonaActual() {
        return zonaactual;
    }
    
    public void incrementarCapturas() {
        this.contadorCapturas++;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                // ¿esta eleven enfadada?
                chequear_paralisis();

                // a ver si hay alguna merienda cerca
                Child victima = zonaactual.seleccionarninoaleatorio();

                if (victima != null) {
                    // se viene pelea
                    darle_caña(victima);
                } else {
                    // aqui no hay ni un alma, me echo una siesta
                    System.out.println(id + " se aburre en " + zonaactual.getTipo() + ". Esperando...");
                    Thread.sleep((long) (4000 + Math.random() * 1000));
                }

                // me piro a otro barrio
                patear_calle();

            } catch (InterruptedException e) {
                System.out.println(id + " ha sido borrado del mapa.");
                break;
            }
        }
    }

    private void chequear_paralisis() throws InterruptedException {
        // si la niña esta usando sus poderes, me quedo moñeco
        while (sistema.eleveen_enfadada) {
            Thread.sleep(100); 
        }
    }

    private void darle_caña(Child victima) throws InterruptedException {
        // cuanto tardo en soltar bofetadas
        long tiempo_pelea = (long) (500 + Math.random() * 1000);

        // con tormenta voy con el turbo puesto
        if (sistema.tormentaon()) {
            tiempo_pelea /= 2;
        }

        System.out.println(id + " zurrando a " + victima.getNinoId() + " en " + zonaactual.getTipo());
        Thread.sleep(tiempo_pelea);

        // a ver si tengo punteria (1/3 de exito)
        if (Math.random() < (1.0 / 3.0)) {
            // ¡pa la saca!
            if (zonaactual.capturarnino(victima) != null) {
                secuestrar(victima);
            }
        } else {
            System.out.println(id + " es un manco y ha fallado contra " + victima.getNinoId());
        }
    }

    private void secuestrar(Child victima) throws InterruptedException {
        // lo llevo al zulo (la colmena)
        sistema.colmena.entrar(victima);
        victima.setcapturado(true); 

        // tardo un poco en atarlo bien
        Thread.sleep((long) (500 + Math.random() * 500));

        incrementarCapturas();
        sistema.aviso_a_vecna(); // vecna vigila...
    }

    private void patear_calle() {
        // si el jefe (mindflayer) me habla, voy al buffet libre
        if (sistema.red_mental_on) {
            this.zonaactual = sistema.get_zona_con_mas_carne();
        } else if (!sistema.hayApagon) {
            // si hay luz, me muevo a donde me de la gana
            this.zonaactual = sistema.get_sitio_feo_random();
        }
        // si hay apagon, me quedo aqui que no veo tres en un burro
    }
}
