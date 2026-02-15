# CSI3540 - Devoir 2: Web Calculator

Ce dépôt contient deux implémentations de la calculatrice Web:

- `cgi/`: version CGI Python avec cookie `last_access`
- `servlet/`: version Java Servlet (Jakarta/Tomcat 10+) avec `HttpSession` (`last_access`)

## Fonctions supportées

- Addition, soustraction, multiplication, division
- Validation des entrées
- Gestion de la division par zéro
- Affichage du résultat (ou erreur), du `Dernier accès`, et de l'`Accès actuel`

## Exécution - CGI (Python)

Depuis le dossier `cgi/`, lancer un serveur CGI:

```bash
cd cgi
python3 -m http.server --cgi 8000
```

Ouvrir ensuite:

- Formulaire: `http://localhost:8000/www/index.html`
- Script CGI: `http://localhost:8000/cgi-bin/calc.py`

Le cookie `last_access` est lu puis mis à jour avec:

- `Path=/`
- `Max-Age=2592000` (30 jours)

## Exécution - Servlet (Jakarta)

Compiler et packager le WAR:

```bash
cd servlet
mvn clean package
```

Déployer `servlet/target/calculator-servlet.war` sur Tomcat 10+.

Puis ouvrir:

- `http://localhost:8080/calculator-servlet/index.html`

Le servlet `/calculatrice` lit `last_access` depuis `HttpSession`, l'affiche, puis la met à jour.

## Cookies vs Sessions

- Cookie (CGI): l'information est stockée côté client (navigateur) et renvoyée à chaque requête.
- Session (Servlet): l'information est stockée côté serveur et liée à l'utilisateur via l'identifiant de session.

## Démo

Voir la vidéo de démonstration dans `docs/demo.mp4`.
