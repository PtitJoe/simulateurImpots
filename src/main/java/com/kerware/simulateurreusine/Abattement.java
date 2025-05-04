package com.kerware.simulateurreusine;

// Abattement
// EXIGENCE : EXG_IMPOT_02
public class Abattement {
    //coefficient de calcul de l'abattement
    private final static double TAUX_ABATTEMENT = 0.1;
    // limite maximale de l'abattement
    private final static double LIMITES_ABATTEMENT_MAX = 14171;
    // limite minimale de l'abattement
    private final static double LIMITES_ABATTEMENT_MIN = 495;

    /**
     * Renvoie l'abattement calculé en fonction du revenu net. <br>
     * Ne peut pas être hors des constantes définies dans la classe <br>
     * Dépends de {@link #TAUX_ABATTEMENT}, {@link #LIMITES_ABATTEMENT_MIN}
     * et {@link #LIMITES_ABATTEMENT_MAX}
     * @param revenuNetDeclarant le revenu net de l'un des deux déclarants
     * @return l'abattement calculé ou 0 si le revenu net donné vaut 0
     */
    private static double getAbattement(int revenuNetDeclarant){
        // cas du second revenu qui peut être nul
        if (revenuNetDeclarant == 0) {
            return 0;
        }

        //calcul abattement
        double abattement = revenuNetDeclarant * TAUX_ABATTEMENT;

        //majoration et minoration de l'abattement
        abattement = Math.max(abattement, LIMITES_ABATTEMENT_MIN);
        abattement = Math.min(abattement, LIMITES_ABATTEMENT_MAX);

        return abattement;
    }

    /**
     * Appelle {@link #getAbattement(int)} sur les deux revenus et les additionne
     * @param revenuNetDeclarant1 l'abattement du premier déclarant
     * @param revenuNetDeclarant2 l'abattement du deuxième déclarant (0 si pas d'autre déclarant)
     * @return l'abattement des deux revenus sous forme de double
     */
    public static double getAbattement(int revenuNetDeclarant1, int revenuNetDeclarant2) {
        return Abattement.getAbattement(revenuNetDeclarant1) +
                Abattement.getAbattement(revenuNetDeclarant2);
    }

    /**
     * Applique l'abattement sur un revenu net
     * @param revenuNetDeclarant le revenu net d'un des deux déclarants
     * @return le revenu net - l'abattement
     */
    private static double appliquer(int revenuNetDeclarant){
        return revenuNetDeclarant - Abattement.getAbattement(revenuNetDeclarant);
    }

    /**
     * Calcule le revenu fiscal de référence à partir de {@link #appliquer(int)}
     * et additionne les valeurs trouvées
     * @param revenuNetDeclarant1 le revenu net du premier déclarant
     * @param revenuNetDeclarant2 le revenu net du deuxième déclarant
     * @return le revenu fiscal de référence
     */
    public static double getRevenuFiscalDeReference(int revenuNetDeclarant1,
                                                    int revenuNetDeclarant2){
        return appliquer(revenuNetDeclarant1) + appliquer(revenuNetDeclarant2);
    }
}
