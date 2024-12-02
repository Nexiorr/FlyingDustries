import fr.ulille.but.sae_s2_2024.*;

public class Ville implements Lieu {
    String ville;

    public Ville (String ville){
        this.ville = ville;
    }

    public String getName(){
        return this.ville;
    }

    @Override
    public String toString() {
        return ""+ ville ;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ville == null) ? 0 : ville.hashCode());
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
        Ville other = (Ville) obj;
        if (ville == null) {
            if (other.ville != null)
                return false;
        } else if (!ville.equals(other.ville))
            return false;
        return true;
    }

    
}
