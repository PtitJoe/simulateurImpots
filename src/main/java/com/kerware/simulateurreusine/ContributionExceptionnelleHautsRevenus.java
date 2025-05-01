package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;

import java.util.ArrayList;
import java.util.List;

public class ContributionExceptionnelleHautsRevenus {
    private final double revenuFiscalReference;
    private final boolean estEnCouple;
    
    //Definition des limites de Contribution sur les  Hauts Revenus
    private static final ArrayList<Integer> limitesCEHR = new ArrayList<>(List.of(0, 250000, 500000, 1000000, Integer.MAX_VALUE));
    //Definition des taux de Contribution sur les Hauts Revenus pour les Celibataires
    private static final ArrayList<Double> tauxCEHRCelibataire = new ArrayList<>(List.of(0.0, 0.03, 0.04, 0.04));

    //Definition des taux de Contribution sur les Hauts Revenus pour les Couples
    private static final ArrayList<Double> tauxCEHRCouple = new ArrayList<>(List.of(0.0, 0.0, 0.03, 0.04));

    public ContributionExceptionnelleHautsRevenus(double revenuFiscalReference, SituationFamiliale situationFamiliale) {
        this.revenuFiscalReference = revenuFiscalReference;
        this.estEnCouple = situationFamiliale == SituationFamiliale.MARIE || situationFamiliale == SituationFamiliale.PACSE;
    }

    public ContributionExceptionnelleHautsRevenus(int revenuNetDeclarant1, int revenuNetDeclarant2, SituationFamiliale situationFamiliale){
        this(Abattement.getRevenuFiscalDeReference(revenuNetDeclarant1, revenuNetDeclarant2), situationFamiliale);
    }

    public double getContributionExceptionnelle(){
        double contributionExceptionelle = 0;
        for (int i = 0; i < 5; i++) {
            if ( revenuFiscalReference >= limitesCEHR.get(i) && revenuFiscalReference < limitesCEHR.get(i+1) ) {
                if ( !estEnCouple ) {
                    contributionExceptionelle += ( revenuFiscalReference - limitesCEHR.get(i) ) * tauxCEHRCelibataire.get(i);
                } else {
                    contributionExceptionelle += ( revenuFiscalReference - limitesCEHR.get(i) ) * tauxCEHRCouple.get(i);
                }
                break;
            } else {
                if ( !estEnCouple ) {
                    contributionExceptionelle += ( limitesCEHR.get(i+1) - limitesCEHR.get(i) ) * tauxCEHRCelibataire.get(i);
                } else {
                    contributionExceptionelle += ( limitesCEHR.get(i+1) - limitesCEHR.get(i) ) * tauxCEHRCouple.get(i);
                }
            }
        }
        return Math.round(contributionExceptionelle);
    }
}
