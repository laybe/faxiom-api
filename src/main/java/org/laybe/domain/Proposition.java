package org.laybe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.laybe.domain.enumeration.PropositionType;

import org.laybe.domain.enumeration.ConnectionType;

/**
 * A Proposition.
 */
@Entity
@Table(name = "proposition")
public class Proposition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PropositionType type;

    @Column(name = "text")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "connection_type")
    private ConnectionType connectionType;

    @OneToMany(mappedBy = "premise")
    private Set<Argument> premises = new HashSet<>();

    @OneToMany(mappedBy = "conclusion")
    private Set<Argument> conclusions = new HashSet<>();

    @OneToMany(mappedBy = "proposition1")
    private Set<Proposition> partOfConnections1s = new HashSet<>();

    @OneToMany(mappedBy = "proposition2")
    private Set<Proposition> partOfConnections2s = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "partOfConnections1s", allowSetters = true)
    private Proposition proposition1;

    @ManyToOne
    @JsonIgnoreProperties(value = "partOfConnections2s", allowSetters = true)
    private Proposition proposition2;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PropositionType getType() {
        return type;
    }

    public Proposition type(PropositionType type) {
        this.type = type;
        return this;
    }

    public void setType(PropositionType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public Proposition text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public Proposition connectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
        return this;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public Set<Argument> getPremises() {
        return premises;
    }

    public Proposition premises(Set<Argument> arguments) {
        this.premises = arguments;
        return this;
    }

    public Proposition addPremises(Argument argument) {
        this.premises.add(argument);
        argument.setPremise(this);
        return this;
    }

    public Proposition removePremises(Argument argument) {
        this.premises.remove(argument);
        argument.setPremise(null);
        return this;
    }

    public void setPremises(Set<Argument> arguments) {
        this.premises = arguments;
    }

    public Set<Argument> getConclusions() {
        return conclusions;
    }

    public Proposition conclusions(Set<Argument> arguments) {
        this.conclusions = arguments;
        return this;
    }

    public Proposition addConclusions(Argument argument) {
        this.conclusions.add(argument);
        argument.setConclusion(this);
        return this;
    }

    public Proposition removeConclusions(Argument argument) {
        this.conclusions.remove(argument);
        argument.setConclusion(null);
        return this;
    }

    public void setConclusions(Set<Argument> arguments) {
        this.conclusions = arguments;
    }

    public Set<Proposition> getPartOfConnections1s() {
        return partOfConnections1s;
    }

    public Proposition partOfConnections1s(Set<Proposition> propositions) {
        this.partOfConnections1s = propositions;
        return this;
    }

    public Proposition addPartOfConnections1(Proposition proposition) {
        this.partOfConnections1s.add(proposition);
        proposition.setProposition1(this);
        return this;
    }

    public Proposition removePartOfConnections1(Proposition proposition) {
        this.partOfConnections1s.remove(proposition);
        proposition.setProposition1(null);
        return this;
    }

    public void setPartOfConnections1s(Set<Proposition> propositions) {
        this.partOfConnections1s = propositions;
    }

    public Set<Proposition> getPartOfConnections2s() {
        return partOfConnections2s;
    }

    public Proposition partOfConnections2s(Set<Proposition> propositions) {
        this.partOfConnections2s = propositions;
        return this;
    }

    public Proposition addPartOfConnections2(Proposition proposition) {
        this.partOfConnections2s.add(proposition);
        proposition.setProposition2(this);
        return this;
    }

    public Proposition removePartOfConnections2(Proposition proposition) {
        this.partOfConnections2s.remove(proposition);
        proposition.setProposition2(null);
        return this;
    }

    public void setPartOfConnections2s(Set<Proposition> propositions) {
        this.partOfConnections2s = propositions;
    }

    public Proposition getProposition1() {
        return proposition1;
    }

    public Proposition proposition1(Proposition proposition) {
        this.proposition1 = proposition;
        return this;
    }

    public void setProposition1(Proposition proposition) {
        this.proposition1 = proposition;
    }

    public Proposition getProposition2() {
        return proposition2;
    }

    public Proposition proposition2(Proposition proposition) {
        this.proposition2 = proposition;
        return this;
    }

    public void setProposition2(Proposition proposition) {
        this.proposition2 = proposition;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proposition)) {
            return false;
        }
        return id != null && id.equals(((Proposition) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Proposition{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", text='" + getText() + "'" +
            ", connectionType='" + getConnectionType() + "'" +
            "}";
    }
}
