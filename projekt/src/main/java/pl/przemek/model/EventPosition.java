package pl.przemek.model;


import javax.persistence.*;
import java.io.Serializable;

@NamedQueries({
        @NamedQuery(name="EventPosition.findAll",query="SELECT p FROM EventPosition p")
        })
@Entity
public class EventPosition implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Double xCoordinate;
    private Double yCoordinate;


    public EventPosition(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(Double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(Double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventPosition)) return false;

        EventPosition that = (EventPosition) o;

        if (id != that.id) return false;
        if (xCoordinate != null ? !xCoordinate.equals(that.xCoordinate) : that.xCoordinate != null) return false;
        return yCoordinate != null ? yCoordinate.equals(that.yCoordinate) : that.yCoordinate == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (xCoordinate != null ? xCoordinate.hashCode() : 0);
        result = 31 * result + (yCoordinate != null ? yCoordinate.hashCode() : 0);
        return result;
    }
}
