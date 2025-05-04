package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;

public final class SimulateurV2 {

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
        return Abattement.getRevenuFiscalDeReference(revenuNetDeclarant1 , revenuNetDeclarant2);
    }

    public double getDecote() {
        Decote decote = new Decote(revenuNetDeclarant1, revenuNetDeclarant2,
                situationFamiliale, nbEnfants, nbEnfantsHandicap, isParentIsole);
        return decote.getDecote();
    }

    public double getAbattement() {
        return Abattement.getAbattement(getRevenuNetDeclarant1(),
                getRevenuNetDeclarant2());
    }

    public double getNbParts() {
        Parts parts = new Parts(situationFamiliale, nbEnfants,
                nbEnfantsHandicap, isParentIsole);
        return parts.getParts();
    }

    public double getImpotAvantDecote() {
        BaisseImpots baisseImpots = new BaisseImpots(revenuNetDeclarant1,
                revenuNetDeclarant2, situationFamiliale, nbEnfants,
                nbEnfantsHandicap, isParentIsole);
        return baisseImpots.getImpotsBrutApresPlafonnement();
    }

    public double getImpotNet() {
        ImpotsNet impotsNet = new ImpotsNet(revenuNetDeclarant1,
                revenuNetDeclarant2, situationFamiliale, nbEnfants,
                nbEnfantsHandicap, isParentIsole);
        return  impotsNet.getImpotsNet();
    }

    public int getRevenuNetDeclarant1() {
        return revenuNetDeclarant1;
    }

    public int getRevenuNetDeclarant2() {
        return revenuNetDeclarant2;
    }

    public double getContribExceptionnelle() {
        ContributionExceptionnelleHautsRevenus contribution =
                new ContributionExceptionnelleHautsRevenus(revenuNetDeclarant1,
                        revenuNetDeclarant2, situationFamiliale);
        return contribution.getContributionExceptionnelle();
    }

    public SimulateurV2(int newRevenuNetDeclarant1,
                        int newRevenuNetDeclarant2, SituationFamiliale newSituationFamiliale,
                        int newNbEnfants, int newNbEnfantsHandicap, boolean newIsParentIsole) {
        this.revenuNetDeclarant1 = newRevenuNetDeclarant1;
        this.revenuNetDeclarant2 = newRevenuNetDeclarant2;
        this.situationFamiliale = newSituationFamiliale;
        this.nbEnfants = newNbEnfants;
        this.nbEnfantsHandicap = newNbEnfantsHandicap;
        this.isParentIsole = newIsParentIsole;
    }

    // Fonction de calcul de l'impôt sur le revenu net en France en 2024 sur les revenus 2023
    // effectue le même affichage que la fonction calculImpot du Simulateur précédent
    public int calculImpot() {
        // vérifications de la cohérence des variables
        ValidateurVariables.checkVariables(revenuNetDeclarant1,
                revenuNetDeclarant2, situationFamiliale, nbEnfants,
                nbEnfantsHandicap, isParentIsole);

        affichageValeursDebut();

        Parts parts = new Parts(situationFamiliale, this.nbEnfants,
                nbEnfantsHandicap, this.isParentIsole);
        System.out.println( "Nombre de parts : " + parts.getParts() );
        ContributionExceptionnelleHautsRevenus contribution =
                new ContributionExceptionnelleHautsRevenus(
                        revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale);
        System.out.println( "Contribution exceptionnelle sur les hauts revenus : "
                + contribution.getContributionExceptionnelle() );
        CalculImpots calculImpotsDeclarants = new CalculImpots(
                revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale);
        System.out.println( "Impôt brut des déclarants : " +
                calculImpotsDeclarants.getImpots() );
        CalculImpots calculImpotsFoyerFiscal = new CalculImpots(
                revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale,
                nbEnfants, nbEnfantsHandicap, isParentIsole);
        System.out.println( "Impôt brut du foyer fiscal complet : " +
                calculImpotsFoyerFiscal.getImpots() );
        // baisse impot sans verification du plafond
        BaisseImpots baisseImpots = new BaisseImpots(revenuNetDeclarant1,
                revenuNetDeclarant2, situationFamiliale, nbEnfants,
                nbEnfantsHandicap, isParentIsole);
        System.out.println( "Baisse d'impôt : " + baisseImpots.getBaisseImpots() );
        // calcul du plafond de la baisse d'impôts
        System.out.println( "Plafond de baisse autorisée " +
                baisseImpots.getPlafondBaisseAutorise() );
        // calcul des impots avant decote
        System.out.println( "Impôt brut après plafonnement avant decote : " +
                 getImpotAvantDecote() );
        System.out.println( "Decote : " + getDecote() );
        //calcul final (le seul à être vraiment essentiel)
        ImpotsNet impotsNet = new ImpotsNet(revenuNetDeclarant1, revenuNetDeclarant2,
                situationFamiliale, nbEnfants, nbEnfantsHandicap, isParentIsole);
        int impotsNetFinal = impotsNet.getImpotsNet();
        System.out.println( "Impôt sur le revenu net final : " + impotsNetFinal );
        return impotsNetFinal;
    }

    private void affichageValeursDebut(){
        System.out.println("--------------------------------------------------");
        System.out.println( "Revenu net declarant1 : " + revenuNetDeclarant1);
        System.out.println( "Revenu net declarant2 : " + revenuNetDeclarant2);
        System.out.println( "Situation familiale : " + situationFamiliale.name());
        System.out.println( "Abattement : " + getAbattement());
        System.out.println( "Revenu fiscal de référence : " + getRevenuReference());
        System.out.println( "Nombre d'enfants  : " + this.nbEnfants);
        System.out.println( "Nombre d'enfants handicapés : " + nbEnfantsHandicap);
        System.out.println( "Parent isolé : " + this.isParentIsole);
    }
}

