package pl.przemek.model;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name="ProhibitedWords.SelectAll",query = "SELECT p FROM ForbiddenWords p")
})
@Entity
public class ForbiddenWords {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "word_id")
    private long id;
    @Column(length = 250)
    private String word;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
