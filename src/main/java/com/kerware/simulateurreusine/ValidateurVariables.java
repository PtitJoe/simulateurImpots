package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;

public class ValidateurVariables {

    /**
     * Nombre d'enfants maximal que l'utilisateur peut rentrer
     */
    private static final int NB_ENFANTS_MAX = 7;

    /**
     * Vérifie que toutes les variables entrées par l'utilisateur sont bien cohérentes
     * @param revenuNetDeclarant1 revenu du premier déclarant
     * @param revenuNetDeclarant2 revenu du deuxième déclarant
     * @param situationFamiliale Situation familiale qui vient de l'énumération SituationFamiliale
     * @param nbEnfants nombre d'enfants à charge
     * @param nbEnfantsHandicap nombre d'enfants Handicapés à charge (parmi le total d'enfants)
     * @param isParentIsole Si le déclarant est isolé ou non
     * @throws IllegalArgumentException Si au moins une incohérence est trouvée
     */
    public static void checkVariables(int revenuNetDeclarant1, int revenuNetDeclarant2,
                                      SituationFamiliale situationFamiliale, int nbEnfants,
                                      int nbEnfantsHandicap, boolean isParentIsole)
            throws IllegalArgumentException{
        if ( revenuNetDeclarant1  < 0 || revenuNetDeclarant2 < 0 ) {
            throw new IllegalArgumentException("Le revenu net ne peut pas être négatif");
        }

        if ( nbEnfants < 0 ) {
            throw new IllegalArgumentException("Le nombre d'enfants ne peut pas être négatif");
        }

        if ( nbEnfantsHandicap < 0 ) {
            throw new IllegalArgumentException("Le nombre d'enfants handicapés ne " +
                    "peut pas être négatif");
        }

        if ( situationFamiliale == null ) {
            throw new IllegalArgumentException("La situation familiale ne peut pas être null");
        }

        if ( nbEnfantsHandicap > nbEnfants ) {
            throw new IllegalArgumentException("Le nombre d'enfants handicapés ne " +
                    "peut pas être supérieur au nombre d'enfants");
        }

        if ( nbEnfants > NB_ENFANTS_MAX ) {
            throw new IllegalArgumentException("Le nombre d'enfants ne " +
                    "peut pas être supérieur à 7");
        }

        if ( isParentIsole && ( situationFamiliale == SituationFamiliale.MARIE ||
                situationFamiliale == SituationFamiliale.PACSE ) ) {
            throw new IllegalArgumentException("Un parent isolé ne " +
                    "peut pas être marié ou pacsé");
        }
    }
}
