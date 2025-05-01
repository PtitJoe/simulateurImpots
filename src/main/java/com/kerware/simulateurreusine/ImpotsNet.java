package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;

public class ImpotsNet {

    private final double montantImpots;

    private final double decote;

    private final double contrbutionExceptionnelle;

    public ImpotsNet(double montantImpots, double decote, double contrbutionExceptionnelle) {
        this.montantImpots = montantImpots;
        this.decote = decote;
        this.contrbutionExceptionnelle = contrbutionExceptionnelle;
    }

    public ImpotsNet(int revenuNetDeclarant1, int revenuNetDeclarant2, SituationFamiliale situationFamiliale, int nbEnfants, int nbEnfantsHandicap, boolean isParentIsole){
        this(new BaisseImpots(revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale, nbEnfants, nbEnfantsHandicap, isParentIsole).getImpotsBrutApresPlafonnement(),
                new Decote(revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale, nbEnfants, nbEnfantsHandicap, isParentIsole).getDecote(),
                new ContributionExceptionnelleHautsRevenus(revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale).getContributionExceptionnelle());
    }

    public int getImpotsNet(){
        return (int) Math.round(montantImpots - decote + contrbutionExceptionnelle) ;
    }

}
