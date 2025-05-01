package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;

public class SimulateurV2 {

    // revenu net
    private final int revenuNetDeclarant1;
    private final int revenuNetDeclarant2;
    // nb enfants à charge
    private final int nbEnfants;
    // nb enfants en situation de handicap
    private final int nbEnfantsHandicap;
    private final SituationFamiliale situationFamiliale;
    private final boolean isParentIsole;

    // Getters pour adapter le code legacy pour les tests unitaires
    public double getRevenuReference() {
        return Abattement.appliquer(revenuNetDeclarant1) + Abattement.appliquer(revenuNetDeclarant2);
    }

    public double getDecote() {
        Decote decote = new Decote(revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale, nbEnfants, nbEnfantsHandicap, isParentIsole);
        return decote.getDecote();
    }


    public double getAbattement() {
        return Abattement.getAbbatement(getRevenuNetDeclatant1(), getRevenuNetDeclatant2());
    }

    public double getNbParts() {
        Parts parts = new Parts(situationFamiliale, nbEnfants, nbEnfantsHandicap, isParentIsole);
        return parts.getParts();
    }

    public double getImpotAvantDecote() {
        BaisseImpots baisseImpots = new BaisseImpots(revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale, nbEnfants, nbEnfantsHandicap, isParentIsole);
        return baisseImpots.getImpotsBrutApresPlafonnement();
    }

    public double getImpotNet() {
        ImpotsNet impotsNet = new ImpotsNet(revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale, nbEnfants, nbEnfantsHandicap, isParentIsole);
        return  impotsNet.getImpotsNet();
    }

    public int getRevenuNetDeclatant1() {
        return revenuNetDeclarant1;
    }

    public int getRevenuNetDeclatant2() {
        return revenuNetDeclarant2;
    }

    public double getContribExceptionnelle() {
        ContributionExceptionnelleHautsRevenus contribution = new ContributionExceptionnelleHautsRevenus(revenuNetDeclarant1,
                                                                            revenuNetDeclarant2, situationFamiliale);
        return contribution.getContributionExceptionnelle();
    }

    public SimulateurV2(int revenuNetDeclarant1, int revenuNetDeclarant2, SituationFamiliale situationFamiliale,
                        int nbEnfants, int nbEnfantsHandicap, boolean isParentIsole) {
        this.revenuNetDeclarant1 = revenuNetDeclarant1;
        this.revenuNetDeclarant2 = revenuNetDeclarant2;
        this.situationFamiliale = situationFamiliale;
        this.nbEnfants = nbEnfants;
        this.nbEnfantsHandicap = nbEnfantsHandicap;
        this.isParentIsole = isParentIsole;
    }

    // Fonction de calcul de l'impôt sur le revenu net en France en 2024 sur les revenu 2023
    public int calculImpot() {

        // vérifications de la cohérences des variables
        ValidateurVariables.checkVariables(revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale, nbEnfants, nbEnfantsHandicap, isParentIsole);

        System.out.println("--------------------------------------------------");
        System.out.println( "Revenu net declarant1 : " + revenuNetDeclarant1);
        System.out.println( "Revenu net declarant2 : " + revenuNetDeclarant2);
        System.out.println( "Situation familiale : " + situationFamiliale.name() );

        // Abattement
        // EXIGENCE : EXG_IMPOT_02

        System.out.println( "Abattement : " + Abattement.getAbbatement(revenuNetDeclarant1, revenuNetDeclarant2) );


        // revenu fiscal de référence
        System.out.println( "Revenu fiscal de référence : " + Abattement.getRevenuFiscalDeReference(revenuNetDeclarant1, revenuNetDeclarant2) );


        // parts déclarants
        // EXIG  : EXG_IMPOT_03

        System.out.println( "Nombre d'enfants  : " + this.nbEnfants);
        System.out.println( "Nombre d'enfants handicapés : " + nbEnfantsHandicap);
        System.out.println( "Parent isolé : " + this.isParentIsole);

        Parts parts = new Parts(situationFamiliale, this.nbEnfants, nbEnfantsHandicap, this.isParentIsole);
        System.out.println( "Nombre de parts : " + parts.getParts() );

        // EXIGENCE : EXG_IMPOT_07:
        // Contribution exceptionnelle sur les hauts revenus

        ContributionExceptionnelleHautsRevenus contribution = new ContributionExceptionnelleHautsRevenus(revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale);
        System.out.println( "Contribution exceptionnelle sur les hauts revenus : " + contribution.getContributionExceptionnelle() );

        // Calcul impôt des declarants
        // EXIGENCE : EXG_IMPOT_04

        CalculImpots calculImpotsDeclarants = new CalculImpotsDeclarant(revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale, nbEnfants, nbEnfantsHandicap, isParentIsole);
        System.out.println( "Impôt brut des déclarants : " + calculImpotsDeclarants.getImpots());

        // Calcul impôt foyer fiscal complet
        // EXIGENCE : EXG_IMPOT_04

        CalculImpots calculImpotsFoyerFiscal = new CalculImpotsFoyerFiscal(revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale, nbEnfants, nbEnfantsHandicap, isParentIsole);
        System.out.println( "Impôt brut du foyer fiscal complet : " + calculImpotsFoyerFiscal.getImpots() );

        // Vérification de la baisse d'impôt autorisée
        // EXIGENCE : EXG_IMPOT_05

        // baisse impot sans verification du plafond
        BaisseImpots baisseImpots = new BaisseImpots(revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale, nbEnfants, nbEnfantsHandicap, isParentIsole);
        System.out.println( "Baisse d'impôt : " + baisseImpots.getBaisseImpots() );

        // calcul du plafond de la baisse d'impots
        System.out.println( "Plafond de baisse autorisée " + baisseImpots.getPlafondBaisseAutorise() );

        // calcul des impots avant decote
        System.out.println( "Impôt brut après plafonnement avant decote : " + baisseImpots.getImpotsBrutApresPlafonnement() );

        // Calcul de la decote
        // EXIGENCE : EXG_IMPOT_06
        Decote decote = new Decote(revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale, nbEnfants, nbEnfantsHandicap, isParentIsole);
        System.out.println( "Decote : " + decote.getDecote() );

        //calcul final (le seul a être vraiment essentiel
        ImpotsNet impotsNet = new ImpotsNet(revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale, nbEnfants, nbEnfantsHandicap, isParentIsole);
        int impotsNetFinal = impotsNet.getImpotsNet();
        System.out.println( "Impôt sur le revenu net final : " + impotsNetFinal );

        return impotsNetFinal;
    }
}
