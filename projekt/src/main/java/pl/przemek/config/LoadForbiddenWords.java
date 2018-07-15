package pl.przemek.config;


import pl.przemek.Store.ForbiddenWordsStore;
import pl.przemek.model.ForbiddenWords;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Singleton
@Startup
public class LoadForbiddenWords {

    @PersistenceContext
    private EntityManager em;
    @Inject
    private ForbiddenWordsStore store;

    @PostConstruct
    public void loadWords(){
        TypedQuery<ForbiddenWords> query=em.createNamedQuery("ProhibitedWords.SelectAll",ForbiddenWords.class);
        List<ForbiddenWords> listOfWords=query.getResultList();
        store.setListOfForbiddenWords(listOfWords);
    }
}
