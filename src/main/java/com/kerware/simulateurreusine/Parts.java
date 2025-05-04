package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;
// parts déclarants
// EXIGENCE : EXG_IMPOT_03
public final class Parts {
    private final double partsDeclarant;
    private final SituationFamiliale situationFamiliale;
    private final int nbEnfants;
    private final int nbEnfantsSituationHandicap;
    private final boolean isParentIso;

    /**
     * <h4>Constructeur</h4>
     * Seul et unique constructeur de cette classe car elle n'a besoin
     * que de paramètres étant des variables atomiques
     * @param newSituationFamiliale Situation familiale qui vient de l'énumération SituationFamiliale
     * @param newNbEnfants nombre d'enfants à charge
     * @param newNbEnfantsSituationHandicap nombre d'enfants Handicapés à charge (parmi le total d'enfants)
     * @param newIsParentIso Si le déclarant est isolé ou non
     */
    public Parts(SituationFamiliale newSituationFamiliale, int newNbEnfants,
                 int newNbEnfantsSituationHandicap, boolean newIsParentIso) {
        this.situationFamiliale = newSituationFamiliale;
        this.nbEnfants = newNbEnfants;
        this.nbEnfantsSituationHandicap = newNbEnfantsSituationHandicap;
        this.isParentIso = newIsParentIso;
        this.partsDeclarant = new PartsDeclarant(situationFamiliale).getPartsDeclarant();
    }


    private static final double PREMIERS_ENFANTS_QUANTITE = 2;
    private static final double PARTS_PAR_PREMIERS_ENFANTS = 0.5;
    private static final double PARTS_PAR_ENFANTS_APRES_PREMIERS = 1;
    private static final double PARTS_PAR_PARENT_ISOLE_ET_ENFANTS = 0.5;
    private static final double PARTS_PAR_VEUF_ET_ENFANTS = 1;
    private static final double PARTS_PAR_ENFANTS_HANDICAPES = 0.5;

    public double getParts(){
        double parts = partsDeclarant;

        // parts enfants à charge
        if ( nbEnfants <= PREMIERS_ENFANTS_QUANTITE ) {
            parts += nbEnfants * PARTS_PAR_PREMIERS_ENFANTS;
        } else {
            parts += PARTS_PAR_ENFANTS_APRES_PREMIERS + ( nbEnfants - PREMIERS_ENFANTS_QUANTITE );
        }

        // parent isole
        if (isParentIso && nbEnfants > 0 ){
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
