package PA;

public class Child extends Thread {
    private final String id;
    private final Sistemageneral sistema;
    private volatile boolean capturado = false;
    private final Logger logger;
    
    public Child(String id, Sistemageneral sistema) {
        this.id = id;
        this.sistema = sistema;
        this.logger = Logger.getInstance();
    }

    @Override
    public void run() {
        
        sistema.callePrincipal.entrar(this);
        
        while (true) {
            try {
                // SÓTANO BYERS 
                sistema.callePrincipal.salir(this);
                sistema.sotanoByers.entrar(this);

                logger.log(id + " ha entrado al Sótano Byers.");
                System.out.println(id + " se prepara en el Sótano Byers.");

                Thread.sleep(random(1000, 2000));
                
                // Seleccionar portal 
                Portal portal = sistema.seleccionarPortalAleatorio();
                sistema.sotanoByers.salir(this);

                // 2. PORTAL 
                portal.esperarycruzar(this); 

                // 3. UPSIDE DOWN 
                Zona zonaUD = portal.getZonaDestino();
                zonaUD.entrar(this);
                logger.log(id + " ha llegado a " + zonaUD.getTipo());
                System.out.println(id + " ha llegado a " + zonaUD.getTipo());

                // Tiempo de recolección 
                long tiemporecoleccion = random(3000, 5000);
                
                //TORMENTA: El tiempo se duplica
                if (sistema.tormentaon()) {
                    tiemporecoleccion *= 2;
                }
                
                // Recolectando sangre 
                esperarenzona(tiemporecoleccion);

                //RESULTADO DE LA EXPLORACIÓN
                if (!this.capturado) {

                    logger.log(id + " regresa con sangre de Vecna.");
                    // EXITO: Recolecta 1 unidad de sangre y vuelve
                    System.out.println(id + " recolectó sangre y regresa a Hawkins.");
                    sistema.sumar_sangre();

                    zonaUD.salir(this);
                    portal.regresaraHawkins(this); // Prioridad de regreso
                    
                    // RADIO WSQK 
                    sistema.radioWSQK.entrar(this);
                    Thread.sleep(random(2000, 4000));
                    sistema.radioWSQK.salir(this);
                } else {
                    // FRACASO: El niño espera en la COLMENA hasta ser rescatado por Eleven
                    esperarrescate();
                    // Al salir de aquí, Eleven ya lo habrá puesto en la Calle Principal
                }

                // CALLE PRINCIPAL 
                sistema.callePrincipal.entrar(this);
                Thread.sleep(random(3000, 5000));
                
                // Repetir ciclo
                
            } catch (InterruptedException e) {
                System.out.println(id + " ha dejado de existir.");
                break;
            }
        }
    }

    private void esperarenzona(long tiempo) throws InterruptedException {
        long fin = System.currentTimeMillis() + tiempo;
        while (System.currentTimeMillis() < fin && !capturado) {
            Thread.sleep(100); 
        }
    }

    private synchronized void esperarrescate() throws InterruptedException {
        while (capturado) {
            wait(); // El hilo se duerme en la colmena
        }
    }

    public synchronized void setcapturado(boolean estado) {
        this.capturado = estado;

        if (!estado) {
            notifyAll(); // Eleven nos despierta
            logger.log(id + " ha sido rescatado de la Colmena");
        } 
    }

    private long random(int min, int max) {
        return (long) (Math.random() * (max - min) + min);
    }
    
    public String getNinoId() {
        return id;
    }
}