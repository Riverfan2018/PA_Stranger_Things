/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PA;

/**
 *
 * @author lukal
 */
public class Child extends Thread{
    private static int contadorId = 0;
    private final String id;
    private final Sistemageneral mundo;
    private boolean capturado = false;


    public Child(String id, Sistemageneral mundo) {
        this.id = id;
        this.mundo = mundo;
    }

    @Override
    public void run() {
 
        mundo.callePrincipal.entrar(this);
        
        while (true) {
            try {
 
                mundo.calleprincipal.salir(this);
                mundo.sotanobyers.entrar(this);
                System.out.println(id + " ha entrado al Sótano Byers.");
                Thread.sleep(random(1000, 2000));
                
                Portal portal = mundo.seleccionarportalaleatorio();
                mundo.sotanobyers.salir(this);
                

                portal.esperarycruzar(this); 
                

                Zona zonaUD = portal.getzonadestino();
                zonaUD.entrar(this);
                System.out.println(id + " ha llegado a " + zonaUD.getnombre());

                
                long tiemporecoleccion = random(3000, 5000);
                if (mundo.istormentaactiva()) tiemporecoleccion *= 2;
                
                
                esperarenzona(tiemporecoleccion);

                if (!this.capturado) {

                    System.out.println(id + " regresa con sangre de Vecna.");
                    zonaUD.salir(this);
                    portal.regresaraHawkins(this);
                    
                    
                    mundo.radioWSQK.entrar(this);
                    Thread.sleep(random(2000, 4000));
                    mundo.radioWSQK.salir(this);
                } else {
                    esperarrescate();
                }

               
                mundo.calleprincipal.entrar(this);
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
        if (!estado) notifyAll(); 
    }

    private long random(int min, int max) {
        return (long) (Math.random() * (max - min) + min);
    }
    
}
