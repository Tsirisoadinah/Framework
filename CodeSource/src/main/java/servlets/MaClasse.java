package servlets;

import java.net.URL;

public class MaClasse {

    @MonAnnotation("Test d'annotation sur une méthode avec URL")
    public void methodeAvecAnnotation(URL url) {
        System.out.println("Méthode exécutée avec URL : " + url);
    }
}
