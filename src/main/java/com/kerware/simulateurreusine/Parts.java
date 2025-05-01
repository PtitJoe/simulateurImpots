package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;

public class Parts {

    private final SituationFamiliale situationFamiliale;
    private final int nbEnfants;
    private final int nbEnfantsSituationHandicap;
    private final boolean parentIso;

    public Parts(SituationFamiliale situationFamiliale, int nbEnfants, int nbEnfantsSituationHandicap, boolean parentIso) {
        this.situationFamiliale = situationFamiliale;
        this.nbEnfants = nbEnfants;
        this.nbEnfantsSituationHandicap = nbEnfantsSituationHandicap;
        this.parentIso = parentIso;
    }

    public double getPartsDeclarant(){
        //situation familiale
        switch(situationFamiliale){
            case MARIE, PACSE -> {
                return 2;
            }
            case VEUF, DIVORCE, CELIBATAIRE -> {
                return 1;
            }
        }
        return 0;
    }

    public double getParts(){
        double points = getPartsDeclarant();

        // parts enfants à charge
        if ( nbEnfants <= 2 ) {
            points += nbEnfants * 0.5;
        } else {
            points += 1.0 + ( nbEnfants - 2 );
        }

        // parent isole
        if (parentIso && nbEnfants > 0 ){
            points += 0.5;
        }

        // Veuf avec enfant
        if ( situationFamiliale == SituationFamiliale.VEUF && nbEnfants > 0 ) {
            points += 1;
        }

        //enfants en situation de handicap
        points += nbEnfantsSituationHandicap * 0.5;

        return points;
    }
}
