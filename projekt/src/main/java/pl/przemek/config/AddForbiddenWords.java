package pl.przemek.config;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Context;
import java.io.*;
import java.math.BigInteger;
import java.net.URL;

@Singleton
@Startup
public class AddForbiddenWords {

    @PersistenceContext
    EntityManager entityManager;

    private static final String FILE_NAME="META-INF/sql/bad-words_regex.txt";
    private static final String ADD_FORBIDDEN_WORD_QUERY="INSERT INTO FORBIDDENWORDS(word) VALUES(:forbiddenWord)";
    private static final String COUNT_FORBIDDEN_WORDS="SELECT count(*) FROM FORBIDDENWORDS";

    @PostConstruct
    public void init() {
        String line;
        Query query;
        query = entityManager.createNativeQuery(COUNT_FORBIDDEN_WORDS);
        if (((BigInteger)query.getSingleResult()).intValue()==0) {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            try(
                    InputStream is = classloader.getResourceAsStream(FILE_NAME);
                    InputStreamReader inputStreamReader=new InputStreamReader(is);
                    BufferedReader br=new BufferedReader(inputStreamReader);)
            {

                while((line=br.readLine())!=null){
                    query=entityManager.createNativeQuery(ADD_FORBIDDEN_WORD_QUERY);
                    query.setParameter("forbiddenWord",line);
                    query.executeUpdate();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
