package com.kerware.simulateurreusine;

public class Decote {

    private final double nbPartsDeclarant;
    private final double montantImpots;

    private static final double seuilDecoteDeclarantSeul = 1929;
    private static final double seuilDecoteDeclarantCouple = 3191;
    private static final double decoteMaxDeclarantSeul = 873;
    private static final double decoteMaxDeclarantCouple = 1444;
    private static final double tauxDecote = 0.4525;

    public Decote(double nbPartsDeclarant, double montantImpots) {
        this.nbPartsDeclarant = nbPartsDeclarant;
        this.montantImpots = montantImpots;
    }

    public double getDecote(){
        double decote = 0;
        // decote
        if ( nbPartsDeclarant == 1 ) {
            if ( montantImpots < seuilDecoteDeclarantSeul ) {
                decote = decoteMaxDeclarantSeul - ( montantImpots  * tauxDecote );
            }
        }
        if (  nbPartsDeclarant == 2 ) {
            if ( montantImpots < seuilDecoteDeclarantCouple ) {
                decote =  decoteMaxDeclarantCouple - ( montantImpots  * tauxDecote  );
            }
        }
        decote = Math.round( decote );

        if ( montantImpots <= decote ) {
            decote = montantImpots;
        }
        return decote;
    }

}
