package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;

public class BaisseImpots {
    private final double montantImpotsDeclarant;
    private final double montantImpots;
    private final double nbParts;
    private final double nbPartsDeclarant;

    //plafon de baisse maximale par demi part
    private static final double plafondDemiPart = 1759;

    public BaisseImpots(double montantImpotsDeclarant, double montantImpots, double nbParts, double nbPartsDeclarant) {
        this.montantImpotsDeclarant = montantImpotsDeclarant;
        this.montantImpots = montantImpots;
        this.nbParts = nbParts;
        this.nbPartsDeclarant = nbPartsDeclarant;
    }

    public BaisseImpots(int revenuDeclarant1, int revenuDeclarant2, SituationFamiliale situationFamiliale, int nbEnfants, int nbEnfantsSituationHandicap, boolean parentIso){
            this(new CalculImpotsDeclarant(revenuDeclarant1, revenuDeclarant2, situationFamiliale, nbEnfants, nbEnfantsSituationHandicap, parentIso).getImpots(),
                 new CalculImpotsFoyerFiscal(revenuDeclarant1, revenuDeclarant2, situationFamiliale, nbEnfants, nbEnfantsSituationHandicap, parentIso).getImpots(),
                 new Parts(situationFamiliale, nbEnfants, nbEnfantsSituationHandicap, parentIso).getParts(),
                 new Parts(situationFamiliale, nbEnfants, nbEnfantsSituationHandicap, parentIso).getPartsDeclarant());
    }

    public double getBaisseImpots(){
        return montantImpotsDeclarant - montantImpots;
    }

    public double getPlafondBaisseAutorise(){
        double ecartPoints = nbParts - nbPartsDeclarant;
        return (ecartPoints / 0.5) * plafondDemiPart;
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
