import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import fr.ulille.but.sae_s2_2024.AlgorithmeKPCC;
import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;
import fr.ulille.but.sae_s2_2024.Trancon;


public class Plateforme {
    MultiGrapheOrienteValue ensemblePrix;
    MultiGrapheOrienteValue ensembleCO2;
    MultiGrapheOrienteValue ensembleTemps;
    Voyageur voyageur;
    List<List<String>> correspondance;
    List<List<String>> resultatChemin;
    Map<TypeCout, Double> minCout;
    Map<TypeCout, Double> maxCout;
    List<String> historique;

    /**
     * Crée une Plateforme en initialisant 3 MultiGrapheOrienteValue
     * @param voyageur Voyageur concerné
     */
    public Plateforme(Voyageur voyageur) {
        this.voyageur = voyageur;
        ensemblePrix = new MultiGrapheOrienteValue();
        ensembleCO2 = new MultiGrapheOrienteValue();
        ensembleTemps = new MultiGrapheOrienteValue();
        correspondance = new ArrayList<List<String>>();
        resultatChemin = new ArrayList<>();
        minCout = new HashMap<TypeCout,Double>();
        maxCout = new HashMap<TypeCout,Double>();
        historique = new ArrayList<>();
    }

    /**
     * Lis le fichier fourni en paramètre et retourne une liste 
     * @param fileName : Nom du fichier à lire
     * @return : une liste avec les éléments du fichier séparés par un '\n'
     * @throws FileNotFoundException
     * @throws Exception
     */
    public List<String> loadFile(String fileName) throws FileNotFoundException, Exception{
        File file = new File(fileName);
        List<String> tab = new ArrayList<>();
        //Gestion et lecture du fichier
        Scanner inputStream = new Scanner(file);
        inputStream.useDelimiter("\n");
        while(inputStream.hasNext()){
            //read single line, put in string
            tab.add(inputStream.next()); 
        }
        inputStream.close();
        return tab;
    }

    public void createAll(String fileName,String fileNameCor) throws DataException{
        int idx = 0;
        List<String> tab = new ArrayList<>();
        //List<String> tabCor = new ArrayList<>();
        //Gestion et lecture du fichier
        try{
            tab = loadFile(fileName);
            //tabCor = loadFile(fileNameCor);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            System.out.println("Fichier introuvable !");
        } catch (Exception e){
            System.out.println("Lecture du ficher impossible");
        }
        if(tab.size() == 0)throw new DataException("Pas de données");
        for(int j = 0;j< tab.size();j++ ){
            try{
                //Séparation de la ligne en plusieurs String indépendants
                createOne(tab.get(j).split(";"));
                idx = j;
            }
            catch (TailleDonneesException | TransportException | AreteExistanteException | NumberFormatException e){
                System.out.println("La ligne "+(idx+2)+" n'a pas pu être ajoutée");
                System.out.println(e.getMessage());
            }
        }/*
        if(tabCor.size() == 0)throw new DataException("Pas de données de correspondance");
        for(int j = 0;j< tabCor.size();j++ ){
            try{
                //Séparation de la ligne en plusieurs String indépendants
                createOneCor(tabCor.get(j).split(";"));
                idx = j;
            }
            catch (TailleDonneesException | TransportException | AreteExistanteException | NumberFormatException e){
                System.out.println("La ligne "+(idx+2)+" n'a pas pu être ajoutée");
                System.out.println(e.getMessage());
            }
        }*/
    }


    // private void createOneCor(String[] res) throws NumberFormatException, TailleDonneesException, TransportException, AreteExistanteException {
    //     if (res.length != 6){ //teste si il y a bien 6 valeurs
    //         throw new TailleDonneesException("Le nombre de données en entrée est incorrect");
    //     } 
    //     if(!ensemblePrix.sommets().contains(new Ville(res[0]))){
    //         throw new AreteExistanteException("La ville n'existe pas dans le graphe");
    //     }
    //     //Ajout d'une ville tampon pour ajouter le poid d'une correspondance
    //     //ensemblePrix.ajouterSommet(new Ville(res[0]+1));ensembleCO2.ajouterSommet(new Ville(res[0]+1));ensembleTemps.ajouterSommet(new Ville(res[0]+1));

    //     //Vérification de la conformité des moyens de transport
    //     try {
    //         ModaliteTransport.valueOf(res[2].toUpperCase());
    //         ModaliteTransport.valueOf(res[1].toUpperCase());
    //     } catch(IllegalArgumentException e){
    //         throw new TransportException("Le moyen de transport donné est faux");
    //     }
    //     try {
    //         //Ajout des arrêtes dans les 3 graphes
    //         double prix = Double.parseDouble(res[3]);
    //         double co2 = Double.parseDouble(res[4]);
    //         double temps = Double.parseDouble(res[5]);
    //     } catch (NumberFormatException e) {
    //         throw new NumberFormatException("Les valeurs de prix, de temps ou de CO2 ne sont pas des nombres");
    //     }
    // }

    public void createOne(String[] res) throws NumberFormatException, TailleDonneesException, TransportException, AreteExistanteException{
        if (res.length != 6){ //teste si il y a bien 6 valeurs
            throw new TailleDonneesException("Le nombre de données en entrée est incorrect");
        } 
        //Ajout des villes dans les 3 graphes
        ensemblePrix.ajouterSommet(new Ville(res[0]));ensembleCO2.ajouterSommet(new Ville(res[0]));ensembleTemps.ajouterSommet(new Ville(res[0]));
        ensemblePrix.ajouterSommet(new Ville(res[1]));ensembleCO2.ajouterSommet(new Ville(res[1]));ensembleTemps.ajouterSommet(new Ville(res[1]));
        
        //Teste si le mode de transport fait bien parti de ceux donnés
        if (!(res[2].toUpperCase().equals("TRAIN") || res[2].toUpperCase().equals("AVION") || res[2].toUpperCase().equals("BUS"))){ 
            throw new TransportException("Le moyen de transport donné est faux");
        }
        
        //Teste si la modalité de Transport est bien celle demandée par l'utilisateur
        if (this.voyageur.getTransportChoisi() == ModaliteTransport.valueOf(res[2].toUpperCase())
        || this.voyageur.getTransportChoisi2() == ModaliteTransport.valueOf(res[2].toUpperCase())
        || this.voyageur.getTransportChoisi3() == ModaliteTransport.valueOf(res[2].toUpperCase())){ //Teste si le choix du mode de transport est bon
            //Teste si les valeurs données sont bien des doubles et si l'ajout est possible
            System.out.println(res[2]);
            try {
                //Ajout des arrêtes dans les 3 graphes
                double prix = Double.parseDouble(res[3]);
                double co2 = (Math.ceil(Double.parseDouble(res[4])*1000)/1000);
                double temps = Double.parseDouble(res[5]);

                ensemblePrix.ajouterArete(new Trajet(res[2],new Ville(res[0]), new Ville(res[1]),prix, co2, temps),prix );
                ensembleCO2.ajouterArete(new Trajet(res[2], new Ville(res[0]), new Ville(res[1]),prix, co2, temps),co2 );
                ensembleTemps.ajouterArete(new Trajet(res[2], new Ville(res[0]), new Ville(res[1]),prix, co2, temps),temps );

            } catch (NumberFormatException e) {
                throw new NumberFormatException("Les valeurs de prix, de temps ou de CO2 ne sont pas des nombres");
            } catch (IllegalArgumentException e){
                throw new AreteExistanteException("L'arrête existe déjà dans le graphe");
            }
            
        }
    }
    
    /**
     * Calcule les plus cours chemins entre les deux villes données
     * @param v1 : Première ville concernée
     * @param v2 : Deuxième ville concernée
     * @param nbChemins : Nombre de chemins à fournir
     * @return : Liste de chemin du plus petit au plus gros poids
     * @throws IndisponibleException
     * @throws VilleNonConcerneeException
     */
    public List<Chemin> triChemin(Ville v1, Ville v2, int nbChemins) throws IndisponibleException, VilleNonConcerneeException{
        List<Chemin> res = new ArrayList<>();
        if(!(ensembleCO2.sommets().contains(v2)&&ensembleCO2.sommets().contains(v1)))throw new VilleNonConcerneeException("La ville demandée n'est pas dans la liste des trajets");
        if (voyageur.getCritère() == TypeCout.CO2){
           res = AlgorithmeKPCC.kpcc(ensembleCO2, v1, v2, nbChemins);
        }else if (voyageur.getCritère() == TypeCout.PRIX){
            res = AlgorithmeKPCC.kpcc(ensemblePrix, v1, v2, nbChemins);
        }else if (voyageur.getCritère() == TypeCout.TEMPS){
            res = AlgorithmeKPCC.kpcc(ensembleTemps, v1, v2, nbChemins);
        }
        if (res.isEmpty())throw new IndisponibleException("Trajet indispnible avec les villes demandées");
        return res;
    }

    /**
     * Récupère le poids d'un chemin selon le prix, le temps et le CO2
     * @param c : Chemin où trouver le poids
     * @return Une Map contenant les poids totaux du chemin
     */
    public Map<TypeCout, Double> getPoidsChemin(Chemin c){
        Map<TypeCout, Double> res = new HashMap<TypeCout, Double>();
        Double sommePrix= 0.0;
        Double sommeCO2 = 0.0;
        Double sommeTemps = 0.0;        
        for (Trancon t : c.aretes()){
            sommePrix += ensemblePrix.getPoidsArete(t);
            sommeCO2 += ensembleCO2.getPoidsArete(t);
            sommeTemps += ensembleTemps.getPoidsArete(t);
        }
        res.put(TypeCout.TEMPS, sommeTemps);
        res.put(TypeCout.CO2, sommeCO2);
        res.put(TypeCout.PRIX, sommePrix);
        return res;
    }

    /**
     * Retourne true si le chemin a des poids inférieurs aux critères de l'utilisateur
     * @param chemin : Chemin à vérifier
     */
    public boolean cheminInferieurAuxCriteresMax(Chemin chemin){
        Map<TypeCout, Double> res = getPoidsChemin(chemin);
        if(maxCout.get(TypeCout.PRIX)<res.get(TypeCout.PRIX))maxCout.replace(TypeCout.PRIX,res.get(TypeCout.PRIX));
        if(maxCout.get(TypeCout.CO2)<res.get(TypeCout.CO2))maxCout.replace(TypeCout.CO2,res.get(TypeCout.CO2));
        if(maxCout.get(TypeCout.TEMPS)<res.get(TypeCout.TEMPS))maxCout.replace(TypeCout.TEMPS,res.get(TypeCout.TEMPS));
        if(minCout.get(TypeCout.PRIX)>res.get(TypeCout.PRIX))minCout.replace(TypeCout.PRIX,res.get(TypeCout.PRIX));
        if(minCout.get(TypeCout.CO2)>res.get(TypeCout.CO2))minCout.replace(TypeCout.CO2,res.get(TypeCout.CO2));
        if(minCout.get(TypeCout.TEMPS)>res.get(TypeCout.TEMPS))minCout.replace(TypeCout.TEMPS,res.get(TypeCout.TEMPS));
        if ((res.remove(TypeCout.PRIX) < voyageur.getPrix() || voyageur.getPrix() == -1) 
        && (res.remove(TypeCout.CO2) < voyageur.getCO2() || voyageur.getCO2() == -1) 
        && (res.remove(TypeCout.TEMPS) < voyageur.getTemps()|| voyageur.getTemps() == -1)){
            return true;
        }
        return false;
    }

    /**
     * Lance l'application
     * @param fileName : Nom du fichier contenant les données
     * @param v1 : Ville du départ du trajet
     * @param v2 : Ville de l'arrivée du trajet
     */
    public void launch(String fileName, String v1, String v2, String fileNameCor){
        Ville ville1 = new Ville(v1);
        Ville ville2 = new Ville(v2);
        try{
            //créée des graphes aves les données fournies
            this.createAll(fileName, fileNameCor);
            System.out.println("Ajout effectué");
            try{
                //fais la recherche des plus cours chemins dans les graphes
                List<Chemin> chemins = triChemin(ville1, ville2,45);
                maxCout = getPoidsChemin(chemins.get(0));
                minCout = getPoidsChemin(chemins.get(0));
                for(Chemin c : chemins ){
                    //Affiche les plus cours chemins si les critères sont respectés
                    if(cheminInferieurAuxCriteresMax(c)) resultatChemin.add(cheminToString(c)); 
                }
            } catch(VilleNonConcerneeException | IndisponibleException e){
                System.out.println(e.getMessage());
            }
        }
        catch(DataException e) {
            System.out.println(e.getMessage());
        }
    }    

    /**
     * Fourni un String d'affichage exploitable pour les chemins
     * @param c : Chemin à afficher
     * @return : Le chemin en String
     */
    public List<String> cheminToString(Chemin c){
        List<String> res = new ArrayList<>();
        res.add(""+c.aretes().get(0).getDepart());
        res.add(""+c.aretes().get(0).getModalite());
        for(int i = 1; i< c.aretes().size(); i++){
            Trancon t = c.aretes().get(i);
            if(t.getModalite()!= c.aretes().get(i-1).getModalite()){
                res.add(""+t.getDepart());
                res.add(""+t.getModalite());
            }
        }
        res.add(""+c.aretes().get(c.aretes().size()-1).getArrivee());
        for (Entry<TypeCout, Double> val : getPoidsChemin(c).entrySet()){
            res.add(""+val.getKey().toString().toLowerCase());
            res.add(""+val.getValue());
        }
        return res;
    }

    public void serializeVoyageur(){
        String filename = "./bin/"+ "voyageur"+LocalDate.now();
        try(ObjectOutputStream oos =
            new ObjectOutputStream(
                new FileOutputStream(new File(filename)))){
                    oos.writeObject(voyageur);
                    historique.add(filename);
        } catch(Exception e) {e.printStackTrace();}
    }
    
    public List<List<String>> getResultatChemin() {
        return resultatChemin;
    }

    public Map<TypeCout, Double> getMinCout() {
        return minCout;
    }

    public Map<TypeCout, Double> getMaxCout() {
        return maxCout;
    }

    @Override
    public String toString() {
        return ""+this.ensemblePrix +'\n' +this.ensembleCO2 +'\n'+ this.ensembleTemps;
    }

}
