package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;
// Vérification de la baisse d'impôt autorisée
// EXIGENCE : EXG_IMPOT_05
public final class BaisseImpots {
    private final double montantImpotsDeclarant;
    private final double montantImpots;
    private final double nbParts;
    private final double nbPartsDeclarant;

    //plafon de baisse maximale par demi part
    private static final double PLAFOND_DEMI_PARTS = 1759;
    private static final double COEF_PARTS = 0.5;

    public BaisseImpots(double newMontantImpotsDeclarant, double newMontantImpots,
                        double newNbParts, double newNbPartsDeclarant) {
        this.montantImpotsDeclarant = newMontantImpotsDeclarant;
        this.montantImpots = newMontantImpots;
        this.nbParts = newNbParts;
        this.nbPartsDeclarant = newNbPartsDeclarant;
    }

    public BaisseImpots(int revenuDeclarant1, int revenuDeclarant2,
                        SituationFamiliale situationFamiliale, int nbEnfants,
                        int nbEnfantsSituationHandicap, boolean parentIso){
            this(new CalculImpotsDeclarant(revenuDeclarant1, revenuDeclarant2,
                            situationFamiliale, nbEnfants, nbEnfantsSituationHandicap,
                            parentIso).getImpots(),
                 new CalculImpotsFoyerFiscal(revenuDeclarant1, revenuDeclarant2,
                         situationFamiliale, nbEnfants, nbEnfantsSituationHandicap,
                         parentIso).getImpots(),
                 new Parts(situationFamiliale, nbEnfants, nbEnfantsSituationHandicap,
                         parentIso).getParts(),
                 new Parts(situationFamiliale, nbEnfants, nbEnfantsSituationHandicap,
                         parentIso).getPartsDeclarant());
    }

    public double getBaisseImpots(){
        return montantImpotsDeclarant - montantImpots;
    }

    public double getPlafondBaisseAutorise(){
        double ecartPoints = nbParts - nbPartsDeclarant;
        return (ecartPoints / COEF_PARTS) * PLAFOND_DEMI_PARTS;
    }

    public double getImpotsBrutApresPlafonnement(){
        double baisseImpot = getBaisseImpots();
        double plafiondBaisseAutorise = getPlafondBaisseAutorise();

        if ( baisseImpot >= plafiondBaisseAutorise ) {
            return montantImpotsDeclarant - plafiondBaisseAutorise;
        }else{
            return montantImpotsDeclarant - baisseImpot;
        }
    }
}
