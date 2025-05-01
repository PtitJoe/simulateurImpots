package com.kerware.simulateurreusine;

// Abattement
// EXIGENCE : EXG_IMPOT_02
public class Abattement {
    private final static double TAUX_ABATTEMENT = 0.1;
    private final static double LIMITES_ABATTEMENT_MAX = 14171;
    private final static double LIMITES_ABATTEMENT_MIN = 495;

    public static double getAbbatement(int revenuNetDeclarant){
        if (revenuNetDeclarant == 0) {
            return 0;
        }

        //calcul abbattment
        double abbatement = revenuNetDeclarant * TAUX_ABATTEMENT;

        //majoration et minoration de l'abbatement
        abbatement = Math.max(abbatement, LIMITES_ABATTEMENT_MIN);
        abbatement = Math.min(abbatement, LIMITES_ABATTEMENT_MAX);

        return Math.max(abbatement, 0); //on vérifie que l'abbatement soit positif
    }

    public static double getAbbatement(int revenuNetDeclarant1, int revenuNetDeclarant2) {
        return getAbbatement(revenuNetDeclarant1) + getAbbatement(revenuNetDeclarant2);
    }

    public static double appliquer(int revenuNetDeclarant){
        return revenuNetDeclarant - getAbbatement(revenuNetDeclarant);
    }

    public static double getRevenuFiscalDeReference(int revenuNetDeclarant1,
                                                    int revenuNetDeclarant2){
        return appliquer(revenuNetDeclarant1) + appliquer(revenuNetDeclarant2);
    }
}
