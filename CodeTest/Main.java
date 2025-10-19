package CodeTest;

import java.lang.reflect.Method;
import java.net.URL;
import servlets.MaClasse;
import servlets.MonAnnotation;

public class Main {
    public static void main(String[] args) throws Exception {
        MaClasse obj = new MaClasse();

        // On parcourt toutes les méthodes de MaClasse
        for (Method m : MaClasse.class.getDeclaredMethods()) {
            if (m.isAnnotationPresent(MonAnnotation.class)) {
                MonAnnotation ann = m.getAnnotation(MonAnnotation.class);
                System.out.println("Méthode annotée trouvée : " + m.getName());
                System.out.println("Valeur de l’annotation : " + ann.value());

                // On appelle la méthode avec une URL en paramètre
                URL url = new URL("https://www.example.com");
                m.invoke(obj, url);
            }
        }
    }
}

