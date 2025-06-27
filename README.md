# KnowVault

## Environnement de DEV
### Déploiement
Déployer la base Postgres et Keycloak avec docker-compose
```shell
cd env/knowvault-docker
docker-compose up
```
Lancer l'application Spring Boot avec le profil 'dev'
L'application est disponible sur l'URL http://localhost:8081

### Keycloak
docs:
- https://www.keycloak.org/documentation
- https://www.keycloak.org/docs-api/26.0.5/rest-api/index.html

Keycloak est déployé sur le port 8080  
http://localhost:8080

Se connecter à l'interface d'admin avec:
- login: admin
- mdp: password

Le realm de dev est KnowVault.

2 users sont créés avec des rôles attribués via group (mdp: password):
- admin_user => rôle 'Admin'
- redacteur_user => rôle 'Redacteur'

