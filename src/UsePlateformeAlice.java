import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fr.ulille.but.sae_s2_2024.ModaliteTransport;

public class UsePlateformeAlice {

    public static void main(String[] args){


        // String[] data = new String[]{
        //     "Paris;Dijon;Train;30;10;90",
        //     "Dijon;Macon;Train;15;5;60",
        //     "Macon;Lyon;Train;10;3;45",
        //     "Paris;Dijon;Bus;10;20;180",
        //     "Dijon;Macon;Bus;5;10;120",
        //     "Macon;Lyon;Bus;5;8;90",
        //     "Paris;Lyon;Avion;100;150;60"
        // };


        //Mettre -1 si on ne veut pas certain filtre

        Voyageur Alice = new Voyageur( ModaliteTransport.TRAIN,ModaliteTransport.AVION,null, TypeCout.PRIX, -1, 150, -1);
        Plateforme p = new Plateforme(Alice);
        p.launch("csv/Alice.csv", "Paris", "Lyon","csv/Alice.csv" );

        

        
        String filePath ="bin/";
        String fileName ="save";


        try(ObjectOutputStream oos =
            new ObjectOutputStream(
                new FileOutputStream(new File(filePath+fileName)))){
                    oos.writeObject(Alice);
        } catch(Exception e) {e.printStackTrace();}

        try(ObjectInputStream ois =
            new ObjectInputStream(
                new FileInputStream(new File(filePath+fileName)))) {
                    Voyageur tmp = (Voyageur)ois.readObject();
                    System.out.println(tmp);
        } catch(Exception e) {e.printStackTrace();}



    }

}