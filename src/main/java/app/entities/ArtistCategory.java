package app.entities;

public enum ArtistCategory {
    APPEL("Karen Appel"),
    AVERCAMP("Hendrick Avercamp"),
    BOLLONGIER("Hans Bollongier"),
    BRUEGHEL("Jan Brueghel"),
    COORTE("Adriaen Coorte"),
    GIJSELAAR("M. de Gijselaar"),
    HIROSHIGE("Utagawa Hiroshige (I)"),
    ISRAELS("Jozef Israëls"),
    KNIP("Henriëtte Geertruida Knip"),
    KOSON("Ohara Koson"),
    LEEN("Willem van Leen"),
    MAUVE("Anton Mauve"),
    REMBRANDT("Rembrandt van Rijn"),
    SAFTLEVEN("Herman Saftleven"),
    TOOROP("Jan Toorop"),
    VAN_GOGH("Vincent van Gogh"),
    VELDE("Willem van de Velde (II)"),
    VERMEER("Johannes Vermeer"),
    WITHOOS("Pieter Withoos");

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

