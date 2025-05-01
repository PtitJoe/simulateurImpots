package com.kerware.simulateurreusine;

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
