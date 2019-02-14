package pl.przemek.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name="Event.findAll",query = "Select p From Event p"),
        @NamedQuery(name="Event.findAllAndGroupByTime",query="Select p From Event p GROUP BY p.timestamp"),
        @NamedQuery(name="Event.findByCity",query = "Select p From Event p WHERE p.nameOfCity Like :city"),
        @NamedQuery(name="Event.selectAllCities",query = "Select p.nameOfCity From Event p")
})
@NamedNativeQueries({
        @NamedNativeQuery(name = "Event.findAllWithLimit", query = "SELECT * FROM event limit :begin,:quantity",resultClass = Event.class)
})
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "event_id",nullable = false, unique = true)
    private long id;
    @NotNull
    @Size(max=200)
    @Column(nullable = false)
    private String name;
    @NotNull
    @Size(max=100)
    @Column(nullable = false)
    private String nameOfCity;
    @NotNull
    @Column(nullable = false)
    private Timestamp timestamp;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", referencedColumnName="event_id")
    private List<EventPosition> eventPosition;


    public Event(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<EventPosition> getEventPosition() {
        return eventPosition;
    }

    public void setEventPosition(List<EventPosition> eventPosition) {
        this.eventPosition = eventPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;

        Event event = (Event) o;

        if (id != event.id) return false;
        if (name != null ? !name.equals(event.name) : event.name != null) return false;
        if (nameOfCity != null ? !nameOfCity.equals(event.nameOfCity) : event.nameOfCity != null) return false;
        if (timestamp != null ? !timestamp.equals(event.timestamp) : event.timestamp != null) return false;
        if (user != null ? !user.equals(event.user) : event.user != null) return false;
        return eventPosition != null ? eventPosition.equals(event.eventPosition) : event.eventPosition == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (nameOfCity != null ? nameOfCity.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (eventPosition != null ? eventPosition.hashCode() : 0);
        return result;
    }

    public String getNameOfCity() {
        return nameOfCity;
    }

    public void setNameOfCity(String nameOfCity) {
        this.nameOfCity = nameOfCity;
    }
}
