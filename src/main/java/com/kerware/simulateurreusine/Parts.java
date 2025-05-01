package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;
// parts déclarants
// EXIG  : EXG_IMPOT_03
public final class Parts {

    private final SituationFamiliale situationFamiliale;
    private final int nbEnfants;
    private final int nbEnfantsSituationHandicap;
    private final boolean parentIso;

    public Parts(SituationFamiliale newSituationFamiliale, int newNbEnfants,
                 int newNbEnfantsSituationHandicap, boolean newParentIso) {
        this.situationFamiliale = newSituationFamiliale;
        this.nbEnfants = newNbEnfants;
        this.nbEnfantsSituationHandicap = newNbEnfantsSituationHandicap;
        this.parentIso = newParentIso;
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
            default -> {
                return 0;
            }
        }
    }
    private static final double PERMIERS_ENFANTS_QUANTITE = 2;
    private static final double PARTS_PAR_PREMIERS_ENFANTS = 0.5;
    private static final double PARTS_PAR_ENFANTS_APRES_PREMIERS = 1;
    private static final double PARTS_PAR_PARENT_ISOLE_ET_ENFANTS = 0.5;
    private static final double PARTS_PAR_VEUF_ET_ENFANTS = 1;
    private static final double PARTS_PAR_ENFANTS_HANDICAPES = 0.5;

    public double getParts(){
        double parts = getPartsDeclarant();

        // parts enfants à charge
        if ( nbEnfants <= PERMIERS_ENFANTS_QUANTITE ) {
            parts += nbEnfants * PARTS_PAR_PREMIERS_ENFANTS;
        } else {
            parts += PARTS_PAR_ENFANTS_APRES_PREMIERS + ( nbEnfants - PERMIERS_ENFANTS_QUANTITE );
        }

        // parent isole
        if (parentIso && nbEnfants > 0 ){
            parts += PARTS_PAR_PARENT_ISOLE_ET_ENFANTS;
        }

        // Veuf avec enfant
        if ( situationFamiliale == SituationFamiliale.VEUF && nbEnfants > 0 ) {
            parts += PARTS_PAR_VEUF_ET_ENFANTS;
        }

        //enfants en situation de handicap
        parts += nbEnfantsSituationHandicap * PARTS_PAR_ENFANTS_HANDICAPES;

        return parts;
    }
}
