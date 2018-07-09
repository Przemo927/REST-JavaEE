package pl.przemek.model;


public enum EnumRegex {
    TWO_WORDS("\\S+\\s+\\S+"),
    MINIMUM_ONE_WHITE_SPACE("//s+");

    private String regex;

    private EnumRegex(String regex){
        this.regex=regex;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
