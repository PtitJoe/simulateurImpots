package com.kerware.simulateurreusine;

import com.kerware.SituationFamiliale;

public class SimulateurV2 {

    // Abattement
    private  int lAbtMax = 14171;
    private  int lAbtMin = 495;
    private double tAbt = 0.1;

    // Plafond de baisse maximal par demi part
    private double plafDemiPart = 1759;

    private double seuilDecoteDeclarantSeul = 1929;
    private double seuilDecoteDeclarantCouple    = 3191;

    private double decoteMaxDeclarantSeul = 873;
    private double decoteMaxDeclarantCouple = 1444;
    private double tauxDecote = 0.4525;

    // revenu net
    private int rNetDecl1 = 0;
    private int rNetDecl2 = 0;
    // nb enfants
    private int nbEnf = 0;
    // nb enfants handicapés
    private int nbEnfH = 0;

    // revenu fiscal de référence
    private double rFRef = 0;

    // revenu imposable
    private double rImposable = 0;

    // abattement
    private double abt = 0;

    // nombre de parts des  déclarants
    private double nbPtsDecl = 0;
    // nombre de parts du foyer fiscal
    private double nbPts = 0;

    // decote
    private double decote = 0;
    // impôt des déclarants
    private double mImpDecl = 0;
    // impôt du foyer fiscal
    private double mImp = 0;
    private double mImpAvantDecote = 0;
    // parent isolé
    private boolean parIso = false;
    // Contribution exceptionnelle sur les hauts revenus
    private double contribExceptionnelle = 0;

    // Getters pour adapter le code legacy pour les tests unitaires

    public double getRevenuReference() {
        return rFRef;
    }

    public double getDecote() {
        return decote;
    }


    public double getAbattement() {
        return Abattement.getAbbatement(getRevenuNetDeclatant1(), getRevenuNetDeclatant2());
    }

    public double getNbParts() {
        return nbPts;
    }

    public double getImpotAvantDecote() {
        return mImpAvantDecote;
    }

    public double getImpotNet() {
        return mImp;
    }

    public int getRevenuNetDeclatant1() {
        return rNetDecl1;
    }

    public int getRevenuNetDeclatant2() {
        return rNetDecl2;
    }

    public double getContribExceptionnelle() {
        return contribExceptionnelle;
    }


    // Fonction de calcul de l'impôt sur le revenu net en France en 2024 sur les revenu 2023

    public int calculImpot(int revNetDecl1, int revNetDecl2, SituationFamiliale sitFam, int nbEnfants, int nbEnfantsHandicapes, boolean parentIsol) {

        // Préconditions
        if ( revNetDecl1  < 0 || revNetDecl2 < 0 ) {
            throw new IllegalArgumentException("Le revenu net ne peut pas être négatif");
        }

        if ( nbEnfants < 0 ) {
            throw new IllegalArgumentException("Le nombre d'enfants ne peut pas être négatif");
        }

        if ( nbEnfantsHandicapes < 0 ) {
            throw new IllegalArgumentException("Le nombre d'enfants handicapés ne peut pas être négatif");
        }

        if ( sitFam == null ) {
            throw new IllegalArgumentException("La situation familiale ne peut pas être null");
        }

        if ( nbEnfantsHandicapes > nbEnfants ) {
            throw new IllegalArgumentException("Le nombre d'enfants handicapés ne peut pas être supérieur au nombre d'enfants");
        }

        if ( nbEnfants > 7 ) {
            throw new IllegalArgumentException("Le nombre d'enfants ne peut pas être supérieur à 7");
        }

        if ( parentIsol && ( sitFam == SituationFamiliale.MARIE || sitFam == SituationFamiliale.PACSE ) ) {
            throw new IllegalArgumentException("Un parent isolé ne peut pas être marié ou pacsé");
        }

        boolean seul = sitFam == SituationFamiliale.CELIBATAIRE || sitFam == SituationFamiliale.DIVORCE || sitFam == SituationFamiliale.VEUF;
        if (  seul && revNetDecl2 > 0 ) {
            throw new IllegalArgumentException("Un célibataire, un divorcé ou un veuf ne peut pas avoir de revenu pour le déclarant 2");
        }

        // Initialisation des variables

        rNetDecl1 = revNetDecl1;
        rNetDecl2 = revNetDecl2;

        nbEnf = nbEnfants;
        nbEnfH = nbEnfantsHandicapes;
        parIso = parentIsol;

        System.out.println("--------------------------------------------------");
        System.out.println( "Revenu net declarant1 : " + rNetDecl1 );
        System.out.println( "Revenu net declarant2 : " + rNetDecl2 );
        System.out.println( "Situation familiale : " + sitFam.name() );

        // Abattement
        // EXIGENCE : EXG_IMPOT_02
        double revenuFiscalDeReference = Abattement.appliquer(revNetDecl1) + Abattement.appliquer(revNetDecl2);
        System.out.println( "Abattement : " + Abattement.getAbbatement(revNetDecl1, revNetDecl2) );

        rFRef = revenuFiscalDeReference;
        System.out.println( "Revenu fiscal de référence : " + revenuFiscalDeReference );


        // parts déclarants
        // EXIG  : EXG_IMPOT_03
        System.out.println( "Nombre d'enfants  : " + nbEnf );
        System.out.println( "Nombre d'enfants handicapés : " + nbEnfH );
        System.out.println( "Parent isolé : " + parIso );

        PointsDeclarant pointsDeclarant = new PointsDeclarant(sitFam, nbEnf, nbEnfH, parIso);
        nbPtsDecl = pointsDeclarant.getPointsDeclarant();
        nbPts = pointsDeclarant.getPoints();

        System.out.println( "Nombre de parts : " + nbPts );

        // EXIGENCE : EXG_IMPOT_07:
        // Contribution exceptionnelle sur les hauts revenus
        ContributionExceptionnelleHautsRevenus contribution = new ContributionExceptionnelleHautsRevenus(revenuFiscalDeReference, sitFam);
        contribExceptionnelle = contribution.getContributionExceptionnelle();


        contribExceptionnelle = Math.round( contribExceptionnelle );
        System.out.println( "Contribution exceptionnelle sur les hauts revenus : " + contribExceptionnelle );

        // Calcul impôt des declarants
        // EXIGENCE : EXG_IMPOT_04
        CalculImpots calculImpotsDeclarants = new CalculImpots(revenuFiscalDeReference, nbPtsDecl);
        rImposable = calculImpotsDeclarants.getRevenusImposables();
        mImpDecl = calculImpotsDeclarants.getImpots();

        System.out.println( "Impôt brut des déclarants : " + mImpDecl );

        // Calcul impôt foyer fiscal complet
        // EXIGENCE : EXG_IMPOT_04
        CalculImpots calculImpotsFoyerFiscal = new CalculImpots(revenuFiscalDeReference, nbPts);
        rImposable = calculImpotsFoyerFiscal.getRevenusImposables();
        mImp = calculImpotsFoyerFiscal.getImpots();

        System.out.println( "Impôt brut du foyer fiscal complet : " + mImp );

        // Vérification de la baisse d'impôt autorisée
        // EXIGENCE : EXG_IMPOT_05
        // baisse impot

        BaisseImpots baisseImpots = new BaisseImpots(mImpDecl, mImp, nbPts, nbPtsDecl);

        double baisseImpot = baisseImpots.getBaisseImpots();

        System.out.println( "Baisse d'impôt : " + baisseImpot );

        // dépassement plafond

        double plafond = baisseImpots.getPlafondBaisseAutorise();

        System.out.println( "Plafond de baisse autorisée " + plafond );

        mImp = baisseImpots.getImpotsBrutApresPlafonnement();
        System.out.println( "Impôt brut après plafonnement avant decote : " + mImp );
        mImpAvantDecote = mImp;
//////////////////////////////////////////////////////////////////////////////////////
        // Calcul de la decote
        // EXIGENCE : EXG_IMPOT_06

        decote = 0;
        // decote
        if ( nbPtsDecl == 1 ) {
            if ( mImp < seuilDecoteDeclarantSeul ) {
                decote = decoteMaxDeclarantSeul - ( mImp  * tauxDecote );
            }
        }
        if (  nbPtsDecl == 2 ) {
            if ( mImp < seuilDecoteDeclarantCouple ) {
                decote =  decoteMaxDeclarantCouple - ( mImp  * tauxDecote  );
            }
        }
        decote = Math.round( decote );

        if ( mImp <= decote ) {
            decote = mImp;
        }

        System.out.println( "Decote : " + decote );

        mImp = mImp - decote;

        mImp += contribExceptionnelle;

        mImp = Math.round( mImp );

        System.out.println( "Impôt sur le revenu net final : " + mImp );
        return  (int)mImp;
    }





}
