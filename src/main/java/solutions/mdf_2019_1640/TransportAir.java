package solutions.mdf_2019_1640;

import java.util.Scanner;

public class TransportAir {

    public static void main(String[] argv) throws Exception {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        int maGare = Integer.valueOf(line.split(" ")[0]);
        int maLigne = Integer.valueOf(line.split(" ")[1]);
        line = sc.nextLine();
        int saGare = Integer.valueOf(line.split(" ")[0]);
        int saLigne = Integer.valueOf(line.split(" ")[1]);

        if (maGare == saGare) {
            System.out.println(maGare);
        } else if (maGare == 36 || saGare == 36) {
            System.out.println(36);
        } else {
            int premiereGarePossible = maGare > saGare ? maGare : saGare;
            int gareEnCommun = 36;
            for (int i = premiereGarePossible; i < 37; i++) {
                if (i % maLigne == 0 && i % saLigne == 0) {
                    gareEnCommun = i;
                    break;
                }
            }
            System.out.println(gareEnCommun);
        }
        /* Vous pouvez aussi effectuer votre traitement une fois que vous avez lu toutes les données.*/
    }

}
