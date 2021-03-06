# SeamCarving

## A quoi ça sert?
Logiciel qui permet de diminuer la taille d'une image en supprimant progressivement des colonnes "inutiles".

## Auteurs
Clément Bellanger, Pierre Génard

## Lancement

### La base
```bash
# par défaut, dest.pgm = src.pgm, réduction = 50
java -jar SeamCarving.jar src.pgm [dest.pgm] [réduction]
```

### Des extras
```bash
# zone rectangulaire que l'on doit garder (g) ou supprimer (s)
# (x1, y1) coin haut gauche 
# (x2, y2) coin bas droit
java -jar SeamCarving.jar src.pgm dest.pgm réduction [gs] x1 y1 x2 y2

# réduction de lignes
java -jar SeamCarving.jar src.pgm dest.pgm réduction l

# augmentation de colonnes
java -jar SeamCarving.jar src.pgm dest.pgm augmentation a
```

### Un peu de couleur
```bash
# par défaut, dest.ppm = src.ppm, réduction = 50
java -jar SeamCarving.jar src.ppm [dest.ppm] [réduction]
```

## Représentation d'un graphe
```bash
dot -Tsvg test.dot > test.svg
```

## Images
- [Réservoir_PGM](http://people.sc.fsu.edu/~jburkardt/data/pgma/)
- [Réservoir_PPM](http://igm.univ-mlv.fr/~incerti/IMAGES/PPM.htm)

## Documentation PPM
- [Wikipedia](https://fr.wikipedia.org/wiki/Portable_pixmap)
- [Utah](http://www.eng.utah.edu/~cs5610/ppm.html)
- [Netpbm](http://netpbm.sourceforge.net/doc/ppm.html)
- [ISN](http://www.info-isn.fr/Mini%20projet%20image.pdf)
- [Bourke](http://paulbourke.net/dataformats/ppm/)
- [Polytechnique](https://www.enseignement.polytechnique.fr/informatique/profs/Philippe.Chassignet/PGM/index.html)
- [Openclassrooms](https://openclassrooms.com/forum/sujet/lecture-et-modification-d-une-image-ppm-73883)

## Feuille de route
- **Première partie** --> Vendredi 2 Février 20:00
- **Deuxième partie** --> Samedi 3 Mars 20:00
