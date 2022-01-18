## Crée les Datas Class à partir des objets JSON

Dans cette partie nous verrons comment passer des objets JSON vue dans la partie précédente. Nous allons ici découvrir un aspect intéressent de Kotlin à savoir les Data Class, ces dernières permet de crée de objets avec un ensemble de donné mais sans « réelle » valeurs métier mais très pratique a manipuler.
Prenons notre API précédemment vue :

Nous observons que l’objet reçu dans la réponse n’est pas utilisable telle qu’elle dans notre application il faut d’abord la « Transformer » pour pouvoir la manipuler.
C’est là qu’intervient le type Data Class. Dans notre exemple nous recevront une liste de donné avec des éléments qui ressemble à cela :

    {

    "login": "mojombo",

    "id": 1,

    "node_id": "MDQ6VXNlcjE=",

    "avatar_url": "https://avatars.githubusercontent.com/u/1?v=4",

    "gravatar_id": "",

    "url": "https://api.github.com/users/mojombo",

    "html_url": "https://github.com/mojombo",

    "followers_url": "https://api.github.com/users/mojombo/followers",

    "following_url": "https://api.github.com/users/mojombo/following{/other_user}",

    "gists_url": "https://api.github.com/users/mojombo/gists{/gist_id}",

    "starred_url": "https://api.github.com/users/mojombo/starred{/owner}{/repo}",

    "subscriptions_url": "https://api.github.com/users/mojombo/subscriptions",

    "organizations_url": "https://api.github.com/users/mojombo/orgs",

    "repos_url": "https://api.github.com/users/mojombo/repos",

    "events_url": "https://api.github.com/users/mojombo/events{/privacy}",

    "received_events_url": "https://api.github.com/users/mojombo/received_events",

    "type": "User",

    "site_admin": false

    }

Le but va être crée une classe pour stocker ces données.
Afin de définir une data class, nous précédons le mot clé “class” par “data” puis le nom de la class comme ceci : data class User(), maintenant comment remplir cette class ? globalement je vous conseille de nommée vos paramètre de la même manière que dans l’objet JSON par exemple "node_id": "MDQ6VXNlcjE=" en JSON deviendra : val node_id: String
val pour une valeur qui n’est pas réaffectable ou var si vous voulez pouvoir la réaffecter plus tard.
Un nom explicite qui correspond à la variable puis le type de l’objet dans le cas présent un string.
Maintenant parlons du liens entre le nom de l’attribue JSON et de l’attribue en Kotlin une manière élégante est d’utiliser com.google.gson.annotations.SerializedName cette annotation vous permette de simplement mapper votre objet JSON a votre objets en Kotlin nous auront donc : @SerializedName("node_id") val node_id: String.
Bon maintenant la manière pour « tricher » vous avez le body de votre réponse en JSON ? vous voulez générer les datas class ? utiliser https://www.json2kotlin.com/  cette dernier vous demandera parfois de retyper certain argument mais pour les gros objets JSON je vous recommande quand même de passer par ce site pour gagner du temps, écrire les datas class n’est ni intéressant ni enrichissant elle sont très pratique c’est tout, alors ne vous privez pas d’outils qui peuvent vous faire gagner du temps et qui vous évitera quelques erreurs d’inattention sur ce genre de tache fastidieuse.
Pour utiliser https://www.json2kotlin.com/ rien de plus simple prenez le résultats de votre requête (qui est au format JSON) insérer le dans le champs textes, sélectionnez le mode « I want GSON SerializedName Mapping » puis cliqué sur Generate ! Un zip sera alors télécharger avec l’ensemble de vos data class, bien sur prenez le contenu de la class mais ne garder pas le nom générique donnez par le site.
