package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;

import java.util.ArrayList;
import java.util.List;

public class CalculImpots {
    private final double nbParts; // parts declarant ou parts totales
    private final double revenuFiscalReference;

    private final static ArrayList<Integer> TRANCHES =
            new ArrayList<>(List.of(0, 11294, 28797, 82341,
                    177106, Integer.MAX_VALUE));
    /**
     * Taux de chaque {@link #TRANCHES}
     */
    private final static ArrayList<Double> TAUX =
            new ArrayList<>(List.of(0.0, 0.11, 0.3, 0.41, 0.45));

    /**
     * <h4>Premier constructeur à usage déconseillé</h4><br>
     * Prend en paramètre des variables difficile à obtenir
     * Le deuxième constructeur est plus simple d'usage
     * @param newRevenuFiscalReference revenu fiscal de reference Calculable par
     *                                 Abattement.getRevenuFiscalDeReference(int , int)
     * @param newNbParts Quantité de Parts
     *                   (Calculable par Parts.getParts ou
     *                   Parts.getPartsDeclarant en fonction des impots souhaités)
     */
    public CalculImpots(double newRevenuFiscalReference, double newNbParts) {
        this.nbParts = newNbParts;
        this.revenuFiscalReference = newRevenuFiscalReference;
    }

    // Calcul impôt des déclarants
    // EXIGENCE : EXG_IMPOT_04
    /**
     * <h4>Deuxième constructeur</h4><br>
     * Prend des les paramètres atomiques rentrés par l'utilisateur
     * En utilisant ce constructeur getImpots vous retournera les impôts du déclarant
     * @param revenuNetDeclarant1 revenu du premier déclarant
     * @param revenuNetDeclarant2 revenu du deuxième déclarant
     * @param situationFamiliale Situation familiale qui vient de l'énumération SituationFamiliale
     */
    public CalculImpots(int revenuNetDeclarant1, int revenuNetDeclarant2,
                        SituationFamiliale situationFamiliale){
        this(Abattement.getRevenuFiscalDeReference(revenuNetDeclarant1,
                revenuNetDeclarant2), new PartsDeclarant(situationFamiliale).getPartsDeclarant());
    }

    // Calcul impôt foyer fiscal complet
    // EXIGENCE : EXG_IMPOT_04
    /**
     * <h4>Surcharge du deuxième constructeur</h4><br>
     * Prend des les paramètres atomiques rentrés par l'utilisateur
     * En utilisant ce constructeur getImpots vous retournera les impôts du foyer fiscal
     * @param revenuNetDeclarant1 revenu du premier déclarant
     * @param revenuNetDeclarant2 revenu du deuxième déclarant
     * @param situationFamiliale Situation familiale qui vient de l'énumération SituationFamiliale
     * @param nbEnfants nombre d'enfants à charge
     * @param nbEnfantsSituationHandicap nombre d'enfants Handicapés à charge (parmi le total d'enfants)
     * @param isParentIso Si le déclarant est isolé ou non
     */
    public CalculImpots(int revenuNetDeclarant1, int revenuNetDeclarant2,
                                   SituationFamiliale situationFamiliale, int nbEnfants,
                                   int nbEnfantsSituationHandicap, boolean isParentIso) {
        this(Abattement.getRevenuFiscalDeReference(revenuNetDeclarant1, revenuNetDeclarant2),
                new Parts(situationFamiliale,nbEnfants, nbEnfantsSituationHandicap, isParentIso)
                        .getParts());
    }


    /**
     * Calcule les impôts avant décote, avant baisse d'impôts et
     * avant contribution exceptionnelle sur les hauts revenus <br>
     * Dépend de {@link #TRANCHES} et de {@link #TAUX} |
     * Appelle {@link #getRevenuImposables()}
     * @return un arrondi des impôts avant décote, avant baisse et
     * avant contribution exceptionnelle sur les hauts revenus
     */
    public final double getImpots(){
        double montantImpotDeclarant = 0;
        double revenuImposable = getRevenuImposables();
        for (int i = 0; i < TRANCHES.size() - 1; i++) {
            if ( revenuImposable >= TRANCHES.get(i) && revenuImposable < TRANCHES.get(i+1) ) {
                montantImpotDeclarant += ( revenuImposable - TRANCHES.get(i) ) * TAUX.get(i);
                break;
            } else {
                montantImpotDeclarant += ( TRANCHES.get(i+1) - TRANCHES.get(i) ) * TAUX.get(i);
            }
        }

        montantImpotDeclarant *= nbParts;

        return Math.round(montantImpotDeclarant);
    }

    /**
     * Calcule les revenus imposables
     * @return les revenus imposables
     */
    public final double getRevenuImposables(){
        return revenuFiscalReference / nbParts;
    }
}
