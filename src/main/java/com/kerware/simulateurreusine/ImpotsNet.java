package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;

/**
 * Classe cerveau qui permet d'éffectuer les derniers calculs pour avoir les impôts nets.
 */
public final class ImpotsNet {

    private final double montantImpots;

    private final double decote;

    private final double contributionExceptionnelle;
    /**
     * <h4>Premier constructeur à usage déconseillé</h4><br>
     * Prend en paramètre des variables difficile à obtenir
     * Le deuxième constructeur est plus simple d'usage
     * @param newDecote décote à appliquer sur les impôts
     *                  Calculable par Decote.getDecote()
     * @param newMontantImpots montant d'impôts avant décote
     *                         Calculable par BaisseImpôts.getImpotsBrutAprèsPlafonnement()
     * @param newContributionExceptionnelle contribution exceptionnelle aux hauts revenus à
     *        appliquer aux impôts Calculable par
     *        ContributionExceptionnelleHautsRevenus.getContributionExceptionnelle()
     */
    public ImpotsNet(double newMontantImpots, double newDecote,
                     double newContributionExceptionnelle) {
        this.montantImpots = newMontantImpots;
        this.decote = newDecote;
        this.contributionExceptionnelle = newContributionExceptionnelle;
    }
    /**
     * <h4>Surcharge du deuxième constructeur</h4><br>
     * Prend des les paramètres atomiques rentrés par l'utilisateur
     * @param revenuNetDeclarant1 revenu du premier déclarant
     * @param revenuNetDeclarant2 revenu du deuxième déclarant
     * @param situationFamiliale Situation familiale qui vient de l'énumération SituationFamiliale
     * @param nbEnfants nombre d'enfants à charge
     * @param nbEnfantsHandicap nombre d'enfants Handicapés à charge (parmi le total d'enfants)
     * @param isParentIsole Si le déclarant est isolé ou non
     */
    public ImpotsNet(int revenuNetDeclarant1, int revenuNetDeclarant2,
                     SituationFamiliale situationFamiliale, int nbEnfants,
                     int nbEnfantsHandicap, boolean isParentIsole){
        this(new BaisseImpots(revenuNetDeclarant1, revenuNetDeclarant2,
                        situationFamiliale, nbEnfants, nbEnfantsHandicap,
                        isParentIsole).getImpotsBrutApresPlafonnement(),
                new Decote(revenuNetDeclarant1, revenuNetDeclarant2,
                        situationFamiliale, nbEnfants, nbEnfantsHandicap,
                        isParentIsole).getDecote(),
                new ContributionExceptionnelleHautsRevenus(revenuNetDeclarant1,
                        revenuNetDeclarant2, situationFamiliale)
                        .getContributionExceptionnelle());
    }

    /**
     * Applique la décote et la contribution exceptionnelle sur les hauts revenus
     * sur le montant d'impôts et renvoie la valeur arrondie
     * @return l'impôt net, calculé arrondi et transformé en entier
     */
    public int getImpotsNet(){
        return (int) Math.round(montantImpots - decote + contributionExceptionnelle) ;
    }

}
