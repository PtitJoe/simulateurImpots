package com.kerware.simulateurreusine;

import java.util.ArrayList;
import java.util.List;

public class CalculImpots {
    private final double nbParts; // parts declarant ou parts totales

    private final double revenuFiscalReference;

    private final static ArrayList<Integer> limites = new ArrayList<>(List.of(0, 11294, 28797, 82341, 177106, Integer.MAX_VALUE));
    private final static ArrayList<Double> taux = new ArrayList<>(List.of(0.0, 0.11, 0.3, 0.41, 0.45));
    public CalculImpots(double revenuFiscalReference, double nbParts) {
        this.nbParts = nbParts;
        this.revenuFiscalReference = revenuFiscalReference;
    }

    public double getImpots(){
        double montantImpotDeclarant = 0;
        double revenuImposable = getRevenusImposables();
        for (int i = 0; i < 5; i++) {
            if ( revenuImposable >= limites.get(i) && revenuImposable < limites.get(i+1) ) {
                montantImpotDeclarant += ( revenuImposable - limites.get(i) ) * taux.get(i);
                break;
            } else {
                montantImpotDeclarant += ( limites.get(i+1) - limites.get(i) ) * taux.get(i);
            }
        }

        montantImpotDeclarant *= nbParts;

        return Math.round(montantImpotDeclarant);
    }

    public double getRevenusImposables(){
        return revenuFiscalReference / nbParts;
    }
}
