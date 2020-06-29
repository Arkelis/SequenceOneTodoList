# Todo List App

Application effectuée dans le cadre de l'électif PMR à l'Ecole Centrale de Lille.

## Différences avec la séquence 1

Il n'y a plus de données stockées dans des fichiers. Dorénavant, on utilise l'API pour gérer les données de todo lists. Pour cela,
on utilise la bibliothèque Retrofit. Les types de réponses sont dans le fichier `ResponseTypes.kt` du package `model`. Le DataProvider
utilise ces types pour ensuite renvoyer les objets pertinents.

Dans les préférences, on a ajouté un mot de passe et un champ pour l'URL de l'API à utiliser.
