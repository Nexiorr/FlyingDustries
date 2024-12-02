import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.ulille.but.sae_s2_2024.*;

public class Trajet implements Trancon{
    Lieu depart;
    Lieu arrivee;
    ModaliteTransport modalite;
    Map<TypeCout,Double> poids = new HashMap<>();

    public Trajet(Lieu depart, Lieu arrivee,ModaliteTransport modalite){
        this.modalite = modalite;
        this.depart = depart;
        this.arrivee = arrivee;
    }
    public Trajet(String modalite, Lieu depart, Lieu arrivee) {
        this( depart, arrivee, ModaliteTransport.valueOf(modalite.toUpperCase()));
    }
    public Trajet(Lieu depart, Lieu arrivee,ModaliteTransport modalite, double prix, double co2, double temps){
        this.modalite = modalite;
        this.depart = depart;
        this.arrivee = arrivee;
        poids.put(TypeCout.PRIX, prix);
        poids.put(TypeCout.CO2, co2);
        poids.put(TypeCout.TEMPS, temps);
    }
    public Trajet(String modalite, Lieu depart, Lieu arrivee, double prix, double co2, double temps) {
        this( depart, arrivee, ModaliteTransport.valueOf(modalite.toUpperCase()), prix, co2, temps);
    }


    public Lieu getDepart() {
        return this.depart;
    }

    public Lieu getArrivee() {
        return this.arrivee;
    }

    public ModaliteTransport getModalite() {
        return this.modalite;
    }

    public void putPoids(TypeCout c, Double val){
        poids.put(c, val);
    }
    public void rmPoids(TypeCout c, Double val){
        poids.remove(c, val);
    }    

    @Override
    public String toString() {
        String res = "";
        for (Entry<TypeCout, Double> val : poids.entrySet()){
            res += " " + val.getKey().toString().toLowerCase()+ " = " + val.getValue() + " ";
        }
        return "" + depart + "-->" + arrivee + " en " + modalite + " :" + res;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((modalite == null) ? 0 : modalite.hashCode());
        result = prime * result + ((depart == null) ? 0 : depart.hashCode());
        result = prime * result + ((arrivee == null) ? 0 : arrivee.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Trajet other = (Trajet) obj;
        if (modalite != other.modalite)
            return false;
        if (depart == null) {
            if (other.depart != null)
                return false;
        } else if (!depart.equals(other.depart))
            return false;
        if (arrivee == null) {
            if (other.arrivee != null)
                return false;
        } else if (!arrivee.equals(other.arrivee))
            return false;
        return true;
    }
    public Map<TypeCout, Double> getPoids() {
        return poids;
    }
    
}
