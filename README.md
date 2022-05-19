# nft-site


## About the project

## How to start the project?
In order to work with all microservices, you should be starting EurekaServer project as a first step and later continue with UserManagement, NFTService and PaymentManagement in this order. This is mainly because all microservices need to be registered in Eureka Server and need to know each other's information to access later.

## Important notice about errors in NFTService about Google Drive
When the project is started it will probably give an error about Google Drive integration. This issue is because I have created a Google Cloud project and given according scopes to reach my personal Google Drive account. If you want to use this microservice, you need to first open a Google Cloud project, then configure scopes in OAuth Consent screen and download its configuration file. Later, at your first you will be redirected to enter your credentials. After these steps, you can use your Google Drive account by assigning the correct folder IDs in the project. 

