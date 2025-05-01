package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;

// Calcul de la decote
// EXIGENCE : EXG_IMPOT_06
public final class Decote {
    private final double nbPartsDeclarant;
    private final double montantImpots;

    private static final double SEUIL_DECOTE_DECLARANT_SEUL = 1929;
    private static final double SEUIL_DECOTE_DECLARANT_COUPLE = 3191;
    private static final double DECOTE_MAX_DECLARANT_SEUL = 873;
    private static final double DECOTE_MAX_DECLARANT_COUPLE = 1444;
    private static final double TAUX_DECOTE = 0.4525;

    public Decote(double newNbPartsDeclarant, double newMontantImpots) {
        this.nbPartsDeclarant = newNbPartsDeclarant;
        this.montantImpots = newMontantImpots;
    }

    public Decote(int revenusNetDeclarant1, int revenusNetDeclarant2,
                  SituationFamiliale situationFamiliale, int nbEnfants,
                  int nbEnfantsSituationHandicap, boolean parentIso){
        this(new Parts(situationFamiliale, nbEnfants, nbEnfantsSituationHandicap,
                        parentIso).getPartsDeclarant(),
                new CalculImpotsFoyerFiscal(revenusNetDeclarant1, revenusNetDeclarant2,
                        situationFamiliale, nbEnfants, nbEnfantsSituationHandicap,
                        parentIso).getImpots());
    }

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
