package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;

public class SimulateurV2 {

    // revenu net
    private final int revenuNetDeclarant1;
    private final int revenuNetDeclarant2;
    // nb e finalnfants
    private final int nbEnfants;
    // nb e finalnfants handicapés
    private final int nbEnfantsHandicap;
    private final SituationFamiliale situationFamiliale;
    private final boolean isParentIsole;

    // decote
    private double decote = 0;
    // impôt du foyer fiscal
    private double mImp = 0;
    private double mImpAvantDecote = 0;
    // parent isolé

    // Getters pour adapter le code legacy pour les tests unitaires

    public double getRevenuReference() {
        return Abattement.appliquer(revenuNetDeclarant1) + Abattement.appliquer(revenuNetDeclarant2);
    }

    public double getDecote() {
        return decote; //TODO
    }


    public double getAbattement() {
        return Abattement.getAbbatement(getRevenuNetDeclatant1(), getRevenuNetDeclatant2());
    }

    public double getNbParts() {
        Parts parts = new Parts(situationFamiliale, nbEnfants, nbEnfantsHandicap, isParentIsole);
        return parts.getParts();
    }

    public double getImpotAvantDecote() {
        return mImpAvantDecote; //TODO
    }

    public double getImpotNet() {
        return mImp; //TODO
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

        // Préconditions
        ValidateurVariables.checkVariables(revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale, nbEnfants, nbEnfantsHandicap, isParentIsole);

        System.out.println("--------------------------------------------------");
        System.out.println( "Revenu net declarant1 : " + revenuNetDeclarant1);
        System.out.println( "Revenu net declarant2 : " + revenuNetDeclarant2);
        System.out.println( "Situation familiale : " + situationFamiliale.name() );

        // Abattement
        // EXIGENCE : EXG_IMPOT_02
        double revenuFiscalDeReference = Abattement.getRevenuFiscalDeReference(revenuNetDeclarant1, revenuNetDeclarant2);
        System.out.println( "Abattement : " + Abattement.getAbbatement(revenuNetDeclarant1, revenuNetDeclarant2) );

        System.out.println( "Revenu fiscal de référence : " + revenuFiscalDeReference );


        // parts déclarants
        // EXIG  : EXG_IMPOT_03
        System.out.println( "Nombre d'enfants  : " + this.nbEnfants);
        System.out.println( "Nombre d'enfants handicapés : " + nbEnfantsHandicap);
        System.out.println( "Parent isolé : " + this.isParentIsole);

        Parts parts = new Parts(situationFamiliale, this.nbEnfants, nbEnfantsHandicap, this.isParentIsole);
        double nbPtsDecl = parts.getPartsDeclarant();
        double nbPts = parts.getParts();

        System.out.println( "Nombre de parts : " + nbPts );

        // EXIGENCE : EXG_IMPOT_07:
        // Contribution exceptionnelle sur les hauts revenus
        ContributionExceptionnelleHautsRevenus contribution = new ContributionExceptionnelleHautsRevenus(revenuFiscalDeReference, situationFamiliale);
        double contribExceptionnelle = contribution.getContributionExceptionnelle();

        System.out.println( "Contribution exceptionnelle sur les hauts revenus : " + contribExceptionnelle );

        // Calcul impôt des declarants
        // EXIGENCE : EXG_IMPOT_04
        CalculImpots calculImpotsDeclarants = new CalculImpots(revenuFiscalDeReference, nbPtsDecl);
        double montantImpotsDeclarant = calculImpotsDeclarants.getImpots();

        System.out.println( "Impôt brut des déclarants : " + montantImpotsDeclarant);

        // Calcul impôt foyer fiscal complet
        // EXIGENCE : EXG_IMPOT_04
        CalculImpots calculImpotsFoyerFiscal = new CalculImpots(revenuFiscalDeReference, nbPts);
        mImp = calculImpotsFoyerFiscal.getImpots();

        System.out.println( "Impôt brut du foyer fiscal complet : " + mImp );

        // Vérification de la baisse d'impôt autorisée
        // EXIGENCE : EXG_IMPOT_05
        // baisse impot

        BaisseImpots baisseImpots = new BaisseImpots(montantImpotsDeclarant,mImp, nbPts, nbPtsDecl);

        double baisseImpot = baisseImpots.getBaisseImpots();

        System.out.println( "Baisse d'impôt : " + baisseImpot );

        // dépassement plafond

        double plafond = baisseImpots.getPlafondBaisseAutorise();

        System.out.println( "Plafond de baisse autorisée " + plafond );

        mImp = baisseImpots.getImpotsBrutApresPlafonnement();
        System.out.println( "Impôt brut après plafonnement avant decote : " + mImp );
        mImpAvantDecote = mImp;

        // Calcul de la decote
        // EXIGENCE : EXG_IMPOT_06
        Decote decoteur = new Decote(nbPtsDecl, mImp);

        decote = decoteur.getDecote();

        System.out.println( "Decote : " + decote );

        mImp -= decote;

        mImp += contribExceptionnelle;

        mImp = Math.round( mImp );

        System.out.println( "Impôt sur le revenu net final : " + mImp );
        return  (int)mImp;
    }





}
