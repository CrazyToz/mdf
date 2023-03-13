package solutions.mdf_2019_1640;

import java.util.Scanner;

public class LesCarottesSontCuites {

    public static void main(String[] argv) throws Exception {
        Scanner sc = new Scanner(System.in);
        String secret = sc.nextLine();
        StringBuilder stringBuilder = new StringBuilder();
        while (sc.hasNextLine()) {
            stringBuilder.append(sc.nextLine());
        }
        String texte = stringBuilder.toString();

//        int resultat = 1;
//        int posLettreTexte = 0;
//        for (int posLettreSecret = 0; posLettreSecret < secret.length(); posLettreSecret++) {
//            posLettreTexte = trouverOccurence(secret, texte, posLettreSecret, posLettreTexte);
//            if (posLettreTexte < 0) {
//                resultat = 0;
//                break;
//            }
//            posLettreTexte++;
//        }
//        System.out.println(resultat);


        System.out.println(chercher(0, 0, secret, texte));
        /* Vous pouvez aussi effectuer votre traitement une fois que vous avez lu toutes les données.*/
    }

//    private static int trouverOccurence(String secret, String texte, int posLettreSecret, int posLettreTexte) {
//        for (int j = posLettreTexte; j < texte.length(); j++) {
//            if (secret.charAt(posLettreSecret) == texte.charAt(j)) {
//                return j;
//            }
//        }
//        return -1;
//    }

    private static int chercher(int posLettreSecret, int posLettreTexte, String secret, String texte) {
        if (posLettreSecret == secret.length()) {
            return 1;
        }
        for (int i = posLettreTexte; i < texte.length(); i++) {
            if (secret.charAt(posLettreSecret) == texte.charAt(i)) {
                return chercher(posLettreSecret + 1, i + 1, secret, texte);
            }
        }
        return 0;
    }
}
