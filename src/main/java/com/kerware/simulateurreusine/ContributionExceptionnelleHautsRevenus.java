package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;

import java.util.ArrayList;
import java.util.List;
// EXIGENCE : EXG_IMPOT_07 :
// Contribution exceptionnelle sur les hauts revenus
public final class ContributionExceptionnelleHautsRevenus {
    //CEHR ---→ Contribution Exceptionnelle sur les Hauts Revenus
    private final double revenuFiscalReference;
    private final boolean estEnCouple;
    
    //Definition des tranches de Contribution Exceptionnelle sur les Hauts Revenus
    private static final ArrayList<Integer> TRANCHES_CEHR =
            new ArrayList<>(List.of(0, 250000, 500000, 1000000, Integer.MAX_VALUE));
    //Definition des taux de Contribution Exceptionnelle sur
    // les Hauts Revenus pour les Célibataires
    /**
     * Définition des taux de Contribution Exceptionnelle sur
     * les Hauts Revenus pour les Célibataires
     * pour chaque tranche de {@link #TRANCHES_CEHR}
     */
    private static final ArrayList<Double> TAUX_CEHR_CELIBATAIRE =
            new ArrayList<>(List.of(0.0, 0.03, 0.04, 0.04));

    /**
     * Definition des taux de Contribution Exceptionnelle sur les Hauts Revenus pour les Couples
     * pour chaque tranche de {@link #TRANCHES_CEHR}
     */
    private static final ArrayList<Double> TAUX_CEHR_COUPLE =
            new ArrayList<>(List.of(0.0, 0.0, 0.03, 0.04));

    /**
     * <h4>Premier constructeur à usage déconseillé</h4><br>
     * Prend en paramètre des variables difficile à obtenir
     * Le deuxième constructeur est plus simple d'usage
     * @param newRevenuFiscalReference revenu fiscal de référence
     *                                 Calculable par
     *                                 Abattement.getRevenuFiscalDeReference(int, int)
     * @param situationFamiliale Situation familiale qui vient de l'énumération SituationFamiliale
     */
    public ContributionExceptionnelleHautsRevenus(double newRevenuFiscalReference,
                                                  SituationFamiliale situationFamiliale) {
        this.revenuFiscalReference = newRevenuFiscalReference;
        this.estEnCouple = situationFamiliale == SituationFamiliale.MARIE ||
                situationFamiliale == SituationFamiliale.PACSE;
    }

    /**
     * <h4>Deuxième constructeur</h4><br>
     * Prend des les paramètres atomiques rentrés par l'utilisateur
     *
     * @param revenuNetDeclarant1 revenu du premier déclarant
     * @param revenuNetDeclarant2 revenu du deuxième déclarant
     * @param situationFamiliale Situation familiale qui vient de l'énumération SituationFamiliale
     */

    public ContributionExceptionnelleHautsRevenus(int revenuNetDeclarant1,
                                                  int revenuNetDeclarant2,
                                                  SituationFamiliale situationFamiliale){
        this(Abattement.getRevenuFiscalDeReference(revenuNetDeclarant1,
                revenuNetDeclarant2), situationFamiliale);
    }

    /**
     * Revoie la contribution exceptionnelle
     * Depend de {@link #TRANCHES_CEHR}, {@link #TAUX_CEHR_CELIBATAIRE}
     * et de {@link #TAUX_CEHR_COUPLE}
     * @return un arrondi de la contribution exceptionnelle sur les Hauts Revenus
     */
    public double getContributionExceptionnelle(){
        double contributionExceptionelle = 0;
        for (int i = 0; i < TRANCHES_CEHR.size(); i++) {
            if ( revenuFiscalReference >= TRANCHES_CEHR.get(i)
                    && revenuFiscalReference < TRANCHES_CEHR.get(i+1) ) {
                if ( !estEnCouple ) {
                    contributionExceptionelle +=
                            ( revenuFiscalReference - TRANCHES_CEHR.get(i) )
                                    * TAUX_CEHR_CELIBATAIRE.get(i);
                } else {
                    contributionExceptionelle +=
                            ( revenuFiscalReference - TRANCHES_CEHR.get(i) )
                                    * TAUX_CEHR_COUPLE.get(i);
                }
                break;
            } else {
                if ( !estEnCouple ) {
                    contributionExceptionelle +=
                            (TRANCHES_CEHR.get(i+1) - TRANCHES_CEHR.get(i) )
                                    * TAUX_CEHR_CELIBATAIRE.get(i);
                } else {
                    contributionExceptionelle +=
                            (TRANCHES_CEHR.get(i+1) - TRANCHES_CEHR.get(i) )
                                    * TAUX_CEHR_COUPLE.get(i);
                }
            }
        }
        return Math.round(contributionExceptionelle);
    }
}
