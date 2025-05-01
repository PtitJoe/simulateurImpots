package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;

public class ValidateurVariables {

    private static final int NB_ENFANTS_MAX = 7;
    public static void checkVariables(int revNetDecl1,
                                      int revNetDecl2, SituationFamiliale sitFam, int nbEnfants,
                                      int nbEnfantsHandicapes, boolean parentIsol)
            throws IllegalArgumentException{
        if ( revNetDecl1  < 0 || revNetDecl2 < 0 ) {
            throw new IllegalArgumentException("Le revenu net ne peut pas être négatif");
        }

        if ( nbEnfants < 0 ) {
            throw new IllegalArgumentException("Le nombre d'enfants ne peut pas être négatif");
        }

        if ( nbEnfantsHandicapes < 0 ) {
            throw new IllegalArgumentException("Le nombre d'enfants handicapés ne " +
                    "peut pas être négatif");
        }

        if ( sitFam == null ) {
            throw new IllegalArgumentException("La situation familiale ne peut pas être null");
        }

        if ( nbEnfantsHandicapes > nbEnfants ) {
            throw new IllegalArgumentException("Le nombre d'enfants handicapés ne " +
                    "peut pas être supérieur au nombre d'enfants");
        }

        if ( nbEnfants > NB_ENFANTS_MAX ) {
            throw new IllegalArgumentException("Le nombre d'enfants ne " +
                    "peut pas être supérieur à 7");
        }

        if ( parentIsol && ( sitFam == SituationFamiliale.MARIE ||
                sitFam == SituationFamiliale.PACSE ) ) {
            throw new IllegalArgumentException("Un parent isolé ne " +
                    "peut pas être marié ou pacsé");
        }
    }
}
