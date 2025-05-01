package com.kerware.simulateurreusine;

import com.kerware.ICalculateurImpot;
import com.kerware.SituationFamiliale;

public class AdaptateurSimulateurV2 implements ICalculateurImpot {
    private SimulateurV2 simulateur;
    private int revenusNetDecl1 = 0;
    private int revenusNetDecl2 = 0;
    private SituationFamiliale situationFamiliale;
    private int nbEnfantsACharge;
    private int nbEnfantsSituationHandicap;
    private boolean parentIsole;


    @Override
    public void setRevenusNetDeclarant1(int rn) {
        this.revenusNetDecl1 = rn;
    }

    @Override
    public void setRevenusNetDeclarant2(int rn) {
        this.revenusNetDecl2 = rn;
    }

    @Override
    public void setSituationFamiliale(SituationFamiliale sf) {
        this.situationFamiliale = sf;
    }

    @Override
    public void setNbEnfantsACharge(int nbe) {
        this.nbEnfantsACharge = nbe;
    }

    @Override
    public void setNbEnfantsSituationHandicap(int nbesh) {
        this.nbEnfantsSituationHandicap = nbesh;
    }

    @Override
    public void setParentIsole(boolean pi) {
        this.parentIsole = pi;
    }

    @Override
    public void calculImpotSurRevenuNet() {
        simulateur = new SimulateurV2(revenusNetDecl1, revenusNetDecl2, situationFamiliale,
                nbEnfantsACharge, nbEnfantsSituationHandicap, parentIsole);
        int impots = simulateur.calculImpot();
        System.out.println("impots net calculés par le nouveau code : " + impots);
    }

    @Override
    public int getRevenuNetDeclatant1() {
        return revenusNetDecl1;
    }

    @Override
    public int getRevenuNetDeclatant2() {
        return revenusNetDecl2;
    }

    @Override
    public double getContribExceptionnelle() {
        return simulateur.getContribExceptionnelle();
    }

    @Override
    public int getRevenuFiscalReference() {
        return (int)simulateur.getRevenuReference();
    }

    @Override
    public int getAbattement() {
        return (int)simulateur.getAbattement();
    }

    @Override
    public double getNbPartsFoyerFiscal() {
        return simulateur.getNbParts();
    }

    @Override
    public int getImpotAvantDecote() {
        return (int)simulateur.getImpotAvantDecote();
    }

    @Override
    public int getDecote() {
        return (int)simulateur.getDecote();
    }

    @Override
    public int getImpotSurRevenuNet() {
        return (int)simulateur.getImpotNet();
    }
}
