package CodeTest;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import servlets.MonAnnotation;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Scanner de Classpath pour l'annotation @MonAnnotation ===");
        
        // Scanner tout le classpath
        List<Class<?>> classesWithAnnotation = scanClasspathForAnnotation();
        
        System.out.println("Nombre de classes trouvées avec l'annotation : " + classesWithAnnotation.size());
        
        // Traiter chaque classe trouvée
        for (Class<?> clazz : classesWithAnnotation) {
            System.out.println("\n--- Classe : " + clazz.getName() + " ---");
            
            try {
                Object obj = clazz.getDeclaredConstructor().newInstance();
                
                // Parcourir toutes les méthodes de cette classe
                for (Method m : clazz.getDeclaredMethods()) {
                    if (m.isAnnotationPresent(MonAnnotation.class)) {
                        MonAnnotation ann = m.getAnnotation(MonAnnotation.class);
                        System.out.println("Méthode annotée trouvée : " + m.getName());
                        System.out.println("Valeur de l'annotation : " + ann.value());

                        // Vérifier si la méthode attend une URL en paramètre
                        if (m.getParameterCount() == 1 && m.getParameterTypes()[0] == URL.class) {
                            URL url = new URL("https://www.example.com");
                            m.setAccessible(true);
                            m.invoke(obj, url);
                        } else if (m.getParameterCount() == 0) {
                            m.setAccessible(true);
                            m.invoke(obj);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Erreur lors du traitement de la classe " + clazz.getName() + " : " + e.getMessage());
            }
        }
    }
    
    /**
     * Scanne tout le classpath pour trouver les classes qui contiennent des méthodes
     * annotées avec @MonAnnotation
     */
    private static List<Class<?>> scanClasspathForAnnotation() {
        List<Class<?>> result = new ArrayList<>();
        
        try {
            // Obtenir le classpath système
            String classpath = System.getProperty("java.class.path");
            String[] classpathEntries = classpath.split(File.pathSeparator);
            
            System.out.println("Scanning classpath entries:");
            for (String entry : classpathEntries) {
                System.out.println("  - " + entry);
                scanClasspathEntry(entry, result);
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors du scan du classpath : " + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * Scanne une entrée du classpath (jar ou répertoire)
     */
    private static void scanClasspathEntry(String entry, List<Class<?>> result) {
        try {
            File file = new File(entry);
            
            if (file.isDirectory()) {
                // Scanner un répertoire
                scanDirectory(file, "", result);
            } else if (file.isFile() && entry.endsWith(".jar")) {
                // Scanner un fichier JAR
                // Pour simplifier, on va juste scanner les répertoires pour le moment
                System.out.println("  (JAR scanning not implemented yet: " + entry + ")");
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors du scan de " + entry + " : " + e.getMessage());
        }
    }
    
    /**
     * Scanne récursivement un répertoire à la recherche de fichiers .class
     */
    private static void scanDirectory(File dir, String packageName, List<Class<?>> result) {
        File[] files = dir.listFiles();
        if (files == null) return;
        
        for (File file : files) {
            if (file.isDirectory()) {
                String newPackageName = packageName.isEmpty() ? 
                    file.getName() : packageName + "." + file.getName();
                scanDirectory(file, newPackageName, result);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                
                try {
                    Class<?> clazz = Class.forName(className);
                    
                    // Vérifier si cette classe a des méthodes avec notre annotation
                    boolean hasAnnotation = false;
                    for (Method method : clazz.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(MonAnnotation.class)) {
                            hasAnnotation = true;
                            break;
                        }
                    }
                    
                    if (hasAnnotation) {
                        result.add(clazz);
                        System.out.println("  Classe trouvée avec annotation : " + className);
                    }
                    
                } catch (ClassNotFoundException | NoClassDefFoundError e) {
                    // Ignorer les classes qui ne peuvent pas être chargées
                    System.out.println("  Impossible de charger la classe : " + className);
                }
            }
        }
    }
}

