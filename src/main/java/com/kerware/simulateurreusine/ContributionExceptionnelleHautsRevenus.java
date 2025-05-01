package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;

import java.util.ArrayList;
import java.util.List;
// EXIGENCE : EXG_IMPOT_07:
// Contribution exceptionnelle sur les hauts revenus
public final class ContributionExceptionnelleHautsRevenus {
    private final double revenuFiscalReference;
    private final boolean estEnCouple;
    
    //Definition des limites de Contribution sur les  Hauts Revenus
    private static final ArrayList<Integer> LIMITES_CEHR =
            new ArrayList<>(List.of(0, 250000, 500000, 1000000, Integer.MAX_VALUE));
    //Definition des taux de Contribution sur les Hauts Revenus pour les Celibataires
    private static final ArrayList<Double> TAUX_CEHR_CELIBATAIRE =
            new ArrayList<>(List.of(0.0, 0.03, 0.04, 0.04));

    //Definition des taux de Contribution sur les Hauts Revenus pour les Couples
    private static final ArrayList<Double> TAUX_CEHR_COUPLE =
            new ArrayList<>(List.of(0.0, 0.0, 0.03, 0.04));

    public ContributionExceptionnelleHautsRevenus(double newRevenuFiscalReference,
                                                  SituationFamiliale situationFamiliale) {
        this.revenuFiscalReference = newRevenuFiscalReference;
        this.estEnCouple = situationFamiliale == SituationFamiliale.MARIE ||
                situationFamiliale == SituationFamiliale.PACSE;
    }

    public ContributionExceptionnelleHautsRevenus(int revenuNetDeclarant1,
                                                  int revenuNetDeclarant2,
                                                  SituationFamiliale situationFamiliale){
        this(Abattement.getRevenuFiscalDeReference(revenuNetDeclarant1,
                revenuNetDeclarant2), situationFamiliale);
    }

    public double getContributionExceptionnelle(){
        double contributionExceptionelle = 0;
        for (int i = 0; i < LIMITES_CEHR.size(); i++) {
            if ( revenuFiscalReference >= LIMITES_CEHR.get(i)
                    && revenuFiscalReference < LIMITES_CEHR.get(i+1) ) {
                if ( !estEnCouple ) {
                    contributionExceptionelle +=
                            ( revenuFiscalReference - LIMITES_CEHR.get(i) )
                                    * TAUX_CEHR_CELIBATAIRE.get(i);
                } else {
                    contributionExceptionelle +=
                            ( revenuFiscalReference - LIMITES_CEHR.get(i) )
                                    * TAUX_CEHR_COUPLE.get(i);
                }
                break;
            } else {
                if ( !estEnCouple ) {
                    contributionExceptionelle +=
                            (LIMITES_CEHR.get(i+1) - LIMITES_CEHR.get(i) )
                                    * TAUX_CEHR_CELIBATAIRE.get(i);
                } else {
                    contributionExceptionelle +=
                            (LIMITES_CEHR.get(i+1) - LIMITES_CEHR.get(i) )
                                    * TAUX_CEHR_COUPLE.get(i);
                }
            }
        }
        return Math.round(contributionExceptionelle);
    }
}
