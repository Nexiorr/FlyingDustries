import java.io.Serializable;

import fr.ulille.but.sae_s2_2024.*;

public class Voyageur implements Serializable{
    private ModaliteTransport transportChoisi;
    private ModaliteTransport transportChoisi2;
    private ModaliteTransport transportChoisi3;
    private TypeCout critère;
    private double filtreTemps = -1;
    private double filtrePrix =-1;
    private double filtreCO2 =-1; 


    public Voyageur(ModaliteTransport transportChoisi,ModaliteTransport transportChoisi2,ModaliteTransport transportChoisi3, TypeCout critère, double filtreTemps,
        double filtrePrix, double filtreCO2) {
        this.transportChoisi = transportChoisi;
        this.transportChoisi2 = transportChoisi2;
        this.transportChoisi3 = transportChoisi3;
        this.critère = critère;
        this.filtreTemps = filtreTemps;
        this.filtrePrix = filtrePrix;
        this.filtreCO2 = filtreCO2;
    }

    public Voyageur( String transportChoisi,String transportChoisi2,String transportChoisi3, String critère, double filtreTemps, double filtrePrix, double filtreCO2){
        this(ModaliteTransport.valueOf(transportChoisi.toUpperCase()),ModaliteTransport.valueOf(transportChoisi2.toUpperCase()),ModaliteTransport.valueOf(transportChoisi3.toUpperCase()), TypeCout.valueOf(critère.toUpperCase()), filtreTemps, filtrePrix, filtreCO2);
    }

    public Voyageur(ModaliteTransport transportChoisi,ModaliteTransport transportChoisi2,ModaliteTransport transportChoisi3,TypeCout critère) {
        this.transportChoisi = transportChoisi;
        this.transportChoisi2 = transportChoisi2;
        this.transportChoisi3 = transportChoisi3;
        this.critère = critère;
    }
    public Voyageur(String transportChoisi,String transportChoisi2,String transportChoisi3, String critère) {
        this(ModaliteTransport.valueOf(transportChoisi.toUpperCase()),ModaliteTransport.valueOf(transportChoisi2.toUpperCase()),ModaliteTransport.valueOf(transportChoisi3.toUpperCase()), TypeCout.valueOf(critère.toUpperCase()));
    }

    public double getTemps() {
        return filtreTemps;
    }

    public double getPrix() {
        return filtrePrix;
    }

    public double getCO2() {
        return filtreCO2;
    }

    public TypeCout getCritère() {
        return critère;
    }


    public ModaliteTransport getTransportChoisi() {
        return transportChoisi;
    }


    public ModaliteTransport getTransportChoisi2() {
        return transportChoisi2;
    }

    public ModaliteTransport getTransportChoisi3() {
        return transportChoisi3;
    }

    @Override
    public String toString() {
        return "Voyageur [critère choisi : " + critère + " , transport choisi : " + transportChoisi + 
                                                                             " ,2eme transport choisi :" + transportChoisi2 +
                                                                             " ,3eme transport choisi :"+ transportChoisi3 +"]";
    }
    
    
}