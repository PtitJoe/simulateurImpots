package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;
// Calcul impôt foyer fiscal complet
// EXIGENCE : EXG_IMPOT_04
public class CalculImpotsFoyerFiscal extends CalculImpots{
    public CalculImpotsFoyerFiscal(int revenuNetDeclarant1, int revenuNetDeclarant2,
                                   SituationFamiliale situationFamiliale, int nbEnfants,
                                   int nbEnfantsSituationHandicap, boolean parentIso) {
        super(revenuNetDeclarant1, revenuNetDeclarant2, new Parts(situationFamiliale,
                nbEnfants, nbEnfantsSituationHandicap, parentIso).getParts());
    }
}
