package PA;

public class Child extends Thread{
    private static int contadorId = 0;
    private final String id;
    private final Sistemageneral sistema;
    private boolean capturado = false;
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
 
                sistema.callePrincipal.salir(this);
                sistema.sotanoByers.entrar(this);
                logger.log(id + " ha entrado al Sótano Byers.");
                System.out.println(id + " ha entrado al Sótano Byers.");
                Thread.sleep(random(1000, 2000));
                
                Portal portal = sistema.seleccionarPortalAleatorio();
                sistema.sotanoByers.salir(this);
                

                portal.esperarycruzar(this); 
                

                Zona zonaUD = portal.getZonaDestino();
                zonaUD.entrar(this);
                logger.log(id + " ha llegado a " + zonaUD.getTipo());
                System.out.println(id + " ha llegado a " + zonaUD.getTipo());

                
                long tiemporecoleccion = random(3000, 5000);
                if (sistema.tormentaon()) tiemporecoleccion *= 2;
                
                
                esperarenzona(tiemporecoleccion);

                if (!this.capturado) {

                    logger.log(id + " regresa con sangre de Vecna.");
                    System.out.println(id + " regresa con sangre de Vecna.");
                    sistema.sumar_sangre();
                    zonaUD.salir(this);
                    portal.regresaraHawkins(this);
                    
                    
                    sistema.radioWSQK.entrar(this);
                    Thread.sleep(random(2000, 4000));
                    sistema.radioWSQK.salir(this);
                } else {
                    esperarrescate();
                }

               
                sistema.callePrincipal.entrar(this);
                Thread.sleep(random(3000, 5000));
                
            } catch (InterruptedException e) {
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
            wait();
        }
    }

    public synchronized void setcapturado(boolean estado) {
        this.capturado = estado;
        if (!estado) {
            notifyAll();
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
