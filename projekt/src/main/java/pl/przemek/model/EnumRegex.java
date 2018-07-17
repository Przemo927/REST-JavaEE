package pl.przemek.model;


public enum EnumRegex {
    MINIMUM_TWO_WORDS("^(\\b[\\w]+\\b([,.?!]\\s)?\\s?){2,}$"),
    WHITE_SPACES("\\s+"),
    NON_WORD_CHARACTER_INSIDE_WORD("[\\w]+(?=[^\\w\\s]).[\\w]+"),
    NON_WORD_CHARACTER("(?=[^\\w\\s]).");

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
