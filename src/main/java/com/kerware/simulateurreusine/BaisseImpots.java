package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;
// Vérification de la baisse d'impôt autorisée
// EXIGENCE : EXG_IMPOT_05
public final class BaisseImpots {
    private final double montantImpotsDeclarant;
    private final double montantImpots;
    private final double nbParts;
    private final double nbPartsDeclarant;

    //plafond de baisse maximale par demi part
    private static final double PLAFOND_DEMI_PARTS = 1759;
    private static final double COEF_PARTS = 0.5;

    /**
     * <h4>Premier constructeur à usage déconseillé</h4><br>
     * Prend en paramètre des variables difficile à obtenir
     * Le deuxième constructeur est plus simple d'usage
     * @param newMontantImpotsDeclarant Quantité d'impôts payés par le déclarant
     *                                  (Obtenable par CalculImpotsDeclarant.getImpots())
     * @param newMontantImpots Quantité d'impôts payés par le foyer fiscal
     *                         (Obtenable par CalculImpotsFoyerFiscal.getImpots()
     * @param newNbParts Quantité de Parts
     *                   (Calculable par Parts.getParts)
     * @param newNbPartsDeclarant Quantité de Parts des déclarants
     *                            (Calculable par Parts.getPartsDeclarant())
     */
    public BaisseImpots(double newMontantImpotsDeclarant, double newMontantImpots,
                        double newNbParts, double newNbPartsDeclarant) {
        this.montantImpotsDeclarant = newMontantImpotsDeclarant;
        this.montantImpots = newMontantImpots;
        this.nbParts = newNbParts;
        this.nbPartsDeclarant = newNbPartsDeclarant;
    }

    /**
     * <h4>Deuxième constructeur</h4><br>
     * Prend des les paramètres atomiques rentrés par l'utilisateur
     *
     * @param revenuDeclarant1 revenu du premier déclarant
     * @param revenuDeclarant2 revenu du deuxième déclarant
     * @param situationFamiliale Situation familiale qui vient de l'énumération SituationFamiliale
     * @param nbEnfants nombre d'enfants à charge
     * @param nbEnfantsSituationHandicap nombre d'enfants Handicapés à charge
     *                                   (parmi le total d'enfants)
     * @param parentIso Si le déclarant est isolé ou non
     */
    public BaisseImpots(int revenuDeclarant1, int revenuDeclarant2,
                        SituationFamiliale situationFamiliale, int nbEnfants,
                        int nbEnfantsSituationHandicap, boolean parentIso){
            this(new CalculImpots(revenuDeclarant1, revenuDeclarant2,
                            situationFamiliale).getImpots(),
                 new CalculImpots(revenuDeclarant1, revenuDeclarant2,
                         situationFamiliale, nbEnfants, nbEnfantsSituationHandicap,
                         parentIso).getImpots(),
                 new Parts(situationFamiliale, nbEnfants, nbEnfantsSituationHandicap,
                         parentIso).getParts(),
                 new PartsDeclarant(situationFamiliale).getPartsDeclarant());
    }

    /**
     * Applique la baisse d'impôts et renvoie le nouveau montant d'impôt
     * @return le nouveau montant d'impôts après la baisse d'impôts
     */
    public double getBaisseImpots(){
        return montantImpotsDeclarant - montantImpots;
    }

    /**
     * Donne la quantité maximale de baisse d'impôts
     * Calculé avec {@link #PLAFOND_DEMI_PARTS} et {@link #COEF_PARTS}
     * @return la quantité maximale de baisse d'impôts
     */
    public double getPlafondBaisseAutorise(){
        double ecartPoints = nbParts - nbPartsDeclarant;
        return (ecartPoints / COEF_PARTS) * PLAFOND_DEMI_PARTS;
    }

    /**
     * Récupère la baisse d'impôts et le plafond de baisse autorisé
     * puis baisse le montant d'impôts du déclarant en fonction des valeurs trouvées
     * @return les impots brut après que le plafonnement est appliqué
     */
    public double getImpotsBrutApresPlafonnement(){
        double baisseImpot = getBaisseImpots();
        double plafondBaisseAutorise = getPlafondBaisseAutorise();

        if ( baisseImpot >= plafondBaisseAutorise ) {
            return montantImpotsDeclarant - plafondBaisseAutorise;
        }else{
            return montantImpotsDeclarant - baisseImpot;
        }
    }
}
