# Application de Gestion Scolaire - Laravel

Cette application permet aux administrateurs de gérer :

- Les utilisateurs (étudiants, enseignants, etc.)
- Les rôles
- Les formations
- Les cours
- Les années scolaires
- Les plannings hebdomadaires de cours

## Prérequis
- [Node.js](https://nodejs.org/) installé
- [Composer](https://getcomposer.org/) installé
- [Laravel](https://laravel.com/docs/12.x/) installé
- [PHP](https://www.php.net/downloads.php/) installé


## Étapes d'installation

### Cloner le projet
```sh
git clone https://github.com/dalton0x0/ypareoLike.git
```

### Se rendre dans le dossier du projet cloner
```sh
cd ypareoLike
```

### Installer les dépendances PHP
```sh
composer install
```

### Installer les dépendances JavaScript
```sh
npm install
```

### Configurer les variables d'environnements
```sh
cp .env.example .env
```

### Générer la clé pour le projet dans votre fichier .env
```sh
php artisan key:generate
```

### Mettez en place la base de données (SQlite par défaut)
```sh
php artisan:migrate
```

### (Optionnel) Peuplez la base de données
```sh
php artisan db:seed
```
ou
```sh
php artisan migrate:fresh --seed
```

### Compiler les assets
```sh
npm run dev
```
Pour une version optimisée :
```sh
npm run prod
```

## Lancer le projet Laravel
```sh
php artisan serve
```
Le projet est accessible sur `http://127.0.0.1:8000`

ou avec Herd tapez
```html
http://ypareoLike.test
```

Le projet Laravel est maintenant accessible avec toutes les dépendances installées et une base de données peuplées!
