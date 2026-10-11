package app.entities;

public enum ArtistCategory {
    APPEL("Karel Appel"),
    AVERCAMP("Hendrick Avercamp"),
    BOLLONGIER("Hans Bollongier"),
    BREITNER("George Hendrik Breitner"),
    BRUEGHEL("Jan Brueghel"),
    DOU("Gerard Dou"),
    HALS("Frans Hals"),
    HOOCH("Pieter de Hooch"),
    ISRAELS("Jozef Israëls"),
    KOSON("Ohara Koson"),
    LEEN("Willem van Leen"),
    MAUVE("Anton Mauve"),
    REMBRANDT("Rembrandt van Rijn"),
    RUISDAEL("Jacob Isaacksz van Ruisdael"),
    SAFTLEVEN("Herman Saftleven"),
    STEEN("Jan Havicksz. Steen"),
    TOOROP("Jan Toorop"),
    VAN_GOGH("Vincent van Gogh"),
    VELDE("Willem van de Velde (II)"),
    VERMEER("Johannes Vermeer");

    //Konstruktør og getter fordi mine enum-værdier og Rikjsmuseums API ikke er de samme
    //Og for at undgå if else, toLowerCase mv
    private final String apiQuery;

    ArtistCategory(String apiQuery) {
        this.apiQuery = apiQuery;
    }

    public String getApiQuery() {
        return apiQuery;
    }
}

