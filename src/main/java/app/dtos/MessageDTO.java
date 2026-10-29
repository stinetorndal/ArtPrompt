package app.dtos;

/*Denne klasse skal bruges her:
 1. Exception handlers for at give bruger standardsvar
 2. Ved handlinger der ikke returnerer et objekt (Delete/Update/Status)
 3. Ved Login og brugerbeskeder
 */

public record MessageDTO(String message) {
    }
