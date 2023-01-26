# Stock service
Houdt de voorraad bij van een artikel binnen een bepaald filiaal. Het is mogelijk om hier wijzigen op te maken door middel van de API interface.

## API docs
Na opstarten van applicatie:
http://localhost:8080/swagger-ui.html

## Reserveren van voorraad
Als er een reservering op de voorraad ligt, vervalt deze na 5 minuten automatisch. Bij het reserveren van de voorraad wordt de beschikbare voorraad voor die locatie verlaagd. 


## Getting started
- Applicatie starten: ./gradlew bootRun
- Testen draaien: ./gradlew test

## To improve
- Houd rekening met de beschikbare voorraad bij het reserveren van de voorraad