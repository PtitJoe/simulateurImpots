package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;

// Calcul de la decote
// EXIGENCE : EXG_IMPOT_06
public final class Decote {
    private final double nbPartsDeclarant;
    private final double montantImpots;

    /**
     * Minimum de décote pour un déclarant seul
     */
    private static final double SEUIL_DECOTE_DECLARANT_SEUL = 1929;
    /**
     * Minimum de décote pour un déclarant non seul
     */
    private static final double SEUIL_DECOTE_DECLARANT_COUPLE = 3191;
    /**
     * Maximum de décote pour un déclarant seul
     */
    private static final double DECOTE_MAX_DECLARANT_SEUL = 873;
    /**
     * Maximum de décote pour un déclarant non seul
     */
    private static final double DECOTE_MAX_DECLARANT_COUPLE = 1444;
    /**
     * Coefficient de décote multiplié par le {@link #montantImpots}
     */
    private static final double TAUX_DECOTE = 0.4525;

    /**
     * <h4>Premier constructeur à usage déconseillé</h4><br>
     * Prend en paramètre des variables difficile à obtenir
     * Le deuxième constructeur est plus simple d'usage
     * @param newNbPartsDeclarant quantité de parts du déclarant Calculable par Parts.getPartsDeclarant()
     * @param newMontantImpots montant d'impôts avant décote Calculable par BaisseImpôts.getImpotsBrutAprèsPlafonnement()
     */
    public Decote(double newNbPartsDeclarant, double newMontantImpots) {
        this.nbPartsDeclarant = newNbPartsDeclarant;
        this.montantImpots = newMontantImpots;
    }

    /**
     * <h4>Surcharge du deuxième constructeur</h4><br>
     * Prend des les paramètres atomiques rentrés par l'utilisateur
     * @param revenuNetDeclarant1 revenu du premier déclarant
     * @param revenuNetDeclarant2 revenu du deuxième déclarant
     * @param situationFamiliale Situation familiale qui vient de l'énumération SituationFamiliale
     * @param nbEnfants nombre d'enfants à charge
     * @param nbEnfantsSituationHandicap nombre d'enfants Handicapés à charge (parmi le total d'enfants)
     * @param isParentIso Si le déclarant est isolé ou non
     */
    public Decote(int revenuNetDeclarant1, int revenuNetDeclarant2,
                  SituationFamiliale situationFamiliale, int nbEnfants,
                  int nbEnfantsSituationHandicap, boolean isParentIso){
        this(new PartsDeclarant(situationFamiliale).getPartsDeclarant(),
                new CalculImpots(revenuNetDeclarant1, revenuNetDeclarant2,
                        situationFamiliale, nbEnfants, nbEnfantsSituationHandicap,
                        isParentIso).getImpots());
    }

    /**
     * Calcule la décote à appliquer.
     * Vérifie que la décote peut être appliquée puis calcule sa valeur.
     * La décote ne peut pas être supérieure au montant d'impôts.
     * @return la décote à appliquer
     */
    public double getDecote(){
        double decote = 0;
        // decote
        if ( nbPartsDeclarant == 1 ) {
            if ( montantImpots < SEUIL_DECOTE_DECLARANT_SEUL) {
                decote = DECOTE_MAX_DECLARANT_SEUL - ( montantImpots  * TAUX_DECOTE);
            }
        }
        if (  nbPartsDeclarant == 2 ) {
            if ( montantImpots < SEUIL_DECOTE_DECLARANT_COUPLE) {
                decote =  DECOTE_MAX_DECLARANT_COUPLE - ( montantImpots  * TAUX_DECOTE);
            }
        }
        decote = Math.round( decote );

        if ( montantImpots <= decote ) {
            decote = montantImpots;
        }
        return decote;
    }

}
