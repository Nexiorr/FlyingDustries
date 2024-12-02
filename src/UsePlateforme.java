import fr.ulille.but.sae_s2_2024.ModaliteTransport;

public class UsePlateforme {
    public static void main(String[] args){

        // String[] data = new String[]{
        //     "villeA;villeB;Train;60;1.7;80",
        //     "villeB;villeD;Train;22;2.4;40",
        //     "villeA;villeC;Train;42;1.4;50",
        //     "villeB;villeC;Train;14;1.4;60",
        //     "villeC;villeD;Avion;110;150;22",
        //     "villeC;villeD;Train;65;1.2;90"
        //     };

        //Soit tous les filtres, soit aucun
        Voyageur voyageur = new Voyageur(ModaliteTransport.TRAIN,ModaliteTransport.AVION,ModaliteTransport.BUS, TypeCout.PRIX, -1,-1,-1);
        //Voyageur bougnoul = new Voyageur("Bougnoul", "Train", "prix", null,null,null);
        Plateforme p = new Plateforme(voyageur);

        p.launch("B3/csv/data.csv", "J", "B", "B3/csv/transport_data.csv");
    }
}

