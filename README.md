# SeamCarving

## A quoi ça sert?
Logiciel qui permet de diminuer la taille d'une image en supprimant progressivement des colonnes "inutiles".

## Auteurs
Clément Bellanger, Pierre Génard

## Lancement

### La base
```bash
java -jar SeamCarving.jar src.pgm [dest.pgm] [réduction]
```

### Des extras
```bash
java -jar SeamCarving.jar src.pgm dest.pgm réduction g|s x1 y1 x2 y2
java -jar SeamCarving.jar src.pgm dest.pgm réduction l
java -jar SeamCarving.jar src.pgm dest.pgm augmentation a
```

### Un peu de couleur
```bash
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
- **Deuxième partie** --> 2 Mars
