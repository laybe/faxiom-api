package org.laybe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;

import org.laybe.domain.enumeration.ArgumentType;

/**
 * A Argument.
 */
@Entity
@Table(name = "argument")
public class Argument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ArgumentType type;

    @ManyToOne
    @JsonIgnoreProperties(value = "conclusions", allowSetters = true)
    private Proposition premise;

    @ManyToOne
    @JsonIgnoreProperties(value = "premises", allowSetters = true)
    private Proposition conclusion;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArgumentType getType() {
        return type;
    }

    public Argument type(ArgumentType type) {
        this.type = type;
        return this;
    }

    public void setType(ArgumentType type) {
        this.type = type;
    }

    public Proposition getPremise() {
        return premise;
    }

    public Argument premise(Proposition proposition) {
        this.premise = proposition;
        return this;
    }

    public void setPremise(Proposition proposition) {
        this.premise = proposition;
    }

    public Proposition getConclusion() {
        return conclusion;
    }

    public Argument conclusion(Proposition proposition) {
        this.conclusion = proposition;
        return this;
    }

    public void setConclusion(Proposition proposition) {
        this.conclusion = proposition;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Argument)) {
            return false;
        }
        return id != null && id.equals(((Argument) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Argument{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
