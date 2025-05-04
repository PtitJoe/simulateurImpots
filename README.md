# simulateurImpots

Ce repository a pour but mettre en place une technique de réagencement de legacy code
Le legacy code donné est une classe permettant d'obtenir le montant d'impots net à payer en fonction de plusieurs paramètres
Ce fonctionnement est tiré du simulateur d'impots du gouvernement français : https://simulateur-ir-ifi.impots.gouv.fr/calcul_impot/2024/

Le code contiendra donc deux parties similaires : une partie qui doit être réagencée et qui servira de référence et la partie réagencée
Le code est donc organisé de la manière suivante :
  1. Le package com.kerware qui contient les classes communes pour les deux parties:
    . Une interface ICalculteurImpot qui doit êter implémentée par l'adaptateur du code 'Simultateur' afin de passer des tests dessus
    . Une énumération SituationFamiliale qui contient les différents situations familiales qui peuvent être rentrées dans le simulateur d'impots 2024 
  2. Un sous package (com.kerware.simulateur) dans lequel est rangé le code legacy :
    . Un Simulateur capable de donner l'impôt net en fonction de plusieurs paramètres
    . Un AdaptateurSimulateur qui suit le patron Adaptateur afin de rendre le Simulateur testable
  3. Un autre sous package (com.kerware.simulateurreusine) dans lequel est rangé le nouveau code avec les classes suivantes :
    . Un SimulateurV2 qui a les mêmes fonctionnalités que le Simulateur d'origine mais avec un code plus propre et plus lisible
    . Un AdaptateurSimulateurV2 qui adapte le code du SimulateurV2 afin de le rendre testable
    . Une dizaine d'autres classes qui permettent de séparer les différentes étapes du calcul des impôts
      Dans cette dizaines les classes sont glabalement organisées de la même manière :
       - Des constantes qui sont utiles lors du calcul de la classe ex: TAUX_DECOTE pour la classe Decote
       - Un constructeur qui prend les variables les plus pécifiques possible et qui parfois ont besoin d'une autre classe pour être claculés
       - Un autre constructeur qui prend des paramètres moins compliqués à obtenir ( les variables rentrés dans le simulateur dès le début )
         et qui va faire appel à d'autres classes pour les transformer en variables plus spécifiques :
           Par exemple : La classe Decote a besoin du nombre de parts du déclarant et du montant d'impots avant la decote
                         Elle a donc un constructeur qui prend ces paramètres.
                         Mais elle a aussi un autre constructeur qui appel la classe Parts et la Classe CalculImpotsFoyerFiscal pour obtenir les parts et le montant d'impots
                         Ce constructeur ci ne prend donc que deux variables simples : les revenus du déclarant 1 et du déclarant 2
      - Une ou plusieurs méthodes qui effectuent des calculs avec les variables stockées et retournent la valeur calculée ex: getDecote pour la classe Decote
     De cette manière chaque classe n'effectue qu'un calcul et demande aux autres de se charger des calculs antérieurs
  4. Enfin il y a les classes et fichiers de tests dans le dossier de tests mais qui n'ont été modifiés que très légèrement:
     . Il y a une nouvelle classe TestsSimulateurV2 qui effectue les mêmes tests quel l'autre classe mais sur le SimulateurV2
     . Il y a un test qui a été ajouté au fichier datasImpositions.csv afin d'avoir une meilleure couverture de code

Le code réusiné a donc une couverture de code de :
  96 % pour les instructions
  91 % pour les branches
Aucun warning ni erreur sur checkstyle (les warnings trouvés sont ceux du code legacy)


Explications Complétmentaires : 
Il y a un héritage dans sur la classe CalulImpots
En effet la classe pouvait à l'origine calculer les impots du déclarant et du foyer fiscal mais il était difficile de faire un constructeur qui appelait les bonnes classes en fonction du souhait de l'utilisateur
Pour plus de clareté et pour éviter ce problème il y a maintenant deux classes filles : CalculImpotDeclarant et CalculImpotFoyerFiscal qui appellent soit getpartsDeclarant soit getParts en fonction de la classe
       
  
