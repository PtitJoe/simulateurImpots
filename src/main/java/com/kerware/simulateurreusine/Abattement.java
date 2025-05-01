package com.kerware.simulateurreusine;
public class Abattement {
    private final static double tauxAbattement = 0.1;

    private final static double lAbattementMax = 14171;
    private final static double lAbattementMin = 495;

    public static double getAbbatement(int revenuNetDeclarant){
        if (revenuNetDeclarant == 0) return 0;

        //calcul abbattment
        double abbatement = revenuNetDeclarant * tauxAbattement;

        //majoration et minoration de l'abbatement
        abbatement = Math.max(abbatement, lAbattementMin);
        abbatement = Math.min(abbatement, lAbattementMax);

        return Math.max(abbatement, 0); //on vérifie que l'abbatement soit positif
    }

    public static double getAbbatement(int revenuNetDeclarant1, int revenuNetDeclarant2) {
        return getAbbatement(revenuNetDeclarant1) + getAbbatement(revenuNetDeclarant2);
    }

    public static double appliquer(int revenuNetDeclarant){
        return revenuNetDeclarant - getAbbatement(revenuNetDeclarant);
    }

    public static double getRevenuFiscalDeReference(int revenuNetDeclarant1, int revenuNetDeclarant2){
        return appliquer(revenuNetDeclarant1) + appliquer(revenuNetDeclarant2);
    }
}
