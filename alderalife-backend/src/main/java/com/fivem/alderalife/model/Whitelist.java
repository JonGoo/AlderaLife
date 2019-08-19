package com.fivem.alderalife.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "whitelist")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Whitelist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String age;

    @NotBlank
    @Column(length = 2048)
    private String experience;

    @NotBlank
    private String steamid;

    private boolean hasReadRules;

    @NotBlank
    private String firstNameRp;

    @NotBlank
    private String lastNameRp;

    @NotBlank
    @Column(length = 2048)
    private String background;

    @NotBlank
    private String avenirRp;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            },
            optional = false)
    @JoinColumn(nullable = false)
    private User user;

    @Value("false")
    private boolean isRefused;

    @Value("false")
    private boolean isAccepted;


    // Getter And Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public boolean isHasReadRules() {
        return hasReadRules;
    }

    public void setHasReadRules(boolean hasReadRules) {
        this.hasReadRules = hasReadRules;
    }

    public String getFirstNameRp() {
        return firstNameRp;
    }

    public void setFirstNameRp(String firstNameRp) {
        this.firstNameRp = firstNameRp;
    }

    public String getLastNameRp() {
        return lastNameRp;
    }

    public void setLastNameRp(String lastNameRp) {
        this.lastNameRp = lastNameRp;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getAvenirRp() {
        return avenirRp;
    }

    public void setAvenirRp(String avenirRp) {
        this.avenirRp = avenirRp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSteamid() {
        return steamid;
    }

    public void setSteamid(String steamid) {
        this.steamid = steamid;
    }

    public boolean isRefused() {
        return isRefused;
    }

    public void setRefused(boolean refused) {
        isRefused = refused;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
