package pl.przemek.Store;

import pl.przemek.model.ForbiddenWords;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ForbiddenWordsStore {

    private List<ForbiddenWords> listOfForbiddenWords;

    public List<ForbiddenWords> getListOfForbiddenWords() {
        return listOfForbiddenWords;
    }

    public void setListOfForbiddenWords(List<ForbiddenWords> listOfForbiddenWords) {
        this.listOfForbiddenWords = listOfForbiddenWords;
    }

}
