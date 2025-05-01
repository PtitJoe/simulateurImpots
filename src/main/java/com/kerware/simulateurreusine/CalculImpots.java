package com.kerware.simulateurreusine;

import java.util.ArrayList;
import java.util.List;

public class CalculImpots {
    private final double nbParts; // parts declarant ou parts totales

    private final double revenuFiscalReference;

    private final static ArrayList<Integer> LIMITES =
            new ArrayList<>(List.of(0, 11294, 28797, 82341,
                    177106, Integer.MAX_VALUE));
    private final static ArrayList<Double> TAUX =
            new ArrayList<>(List.of(0.0, 0.11, 0.3, 0.41, 0.45));
    public CalculImpots(double newRevenuFiscalReference, double newNbParts) {
        this.nbParts = newNbParts;
        this.revenuFiscalReference = newRevenuFiscalReference;
    }

    public CalculImpots(int revenuNetDeclarant1, int revenuNetDeclarant2,double newNbParts){
        this(Abattement.getRevenuFiscalDeReference(revenuNetDeclarant1,
                revenuNetDeclarant2), newNbParts);
    }
    public final double getImpots(){
        double montantImpotDeclarant = 0;
        double revenuImposable = getRevenusImposables();
        for (int i = 0; i < LIMITES.size() - 1; i++) {
            if ( revenuImposable >= LIMITES.get(i) && revenuImposable < LIMITES.get(i+1) ) {
                montantImpotDeclarant += ( revenuImposable - LIMITES.get(i) ) * TAUX.get(i);
                break;
            } else {
                montantImpotDeclarant += ( LIMITES.get(i+1) - LIMITES.get(i) ) * TAUX.get(i);
            }
        }

        montantImpotDeclarant *= nbParts;

        return Math.round(montantImpotDeclarant);
    }

    public final double getRevenusImposables(){
        return revenuFiscalReference / nbParts;
    }
}
