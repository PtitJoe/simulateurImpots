package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;
// parts déclarants
// EXIGENCE : EXG_IMPOT_03
public class PartsDeclarant {
    private final SituationFamiliale situationFamiliale;

    /**
     * <h4>Constructeur</h4>
     * Seul et unique constructeur car cette classe ne prend que des paramètres atomiques
     * @param newSituationFamiliale Situation familiale qui vient de l'énumération SituationFamiliale
     */
    public PartsDeclarant(SituationFamiliale newSituationFamiliale){
        this.situationFamiliale = newSituationFamiliale;
    }

    /**
     * Calcule le nombre de parts du déclarant en fonction de sa situation Familiale
     * @return le nombre de parts du déclarant
     */
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
}
