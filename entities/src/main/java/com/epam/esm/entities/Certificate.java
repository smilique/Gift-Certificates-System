package com.epam.esm.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity(name = "Certificate")
@Table(name = "gift_certificate")
public class Certificate extends RepresentationModel<Tag> implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", precision = 9, scale = 2)
    private BigDecimal price;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "create_date", insertable = false)
    @Generated(value = GenerationTime.INSERT)
    private ZonedDateTime createDate;
    @Column(name = "last_update_date", insertable = false)
    @Generated(value = GenerationTime.ALWAYS)
    private ZonedDateTime lastUpdateDate;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Tag.class)
    @JoinTable(name = "tagged_gift_certificate",
            joinColumns = {@JoinColumn(name = "gift_certificate_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})
    @JsonProperty("tags")
    private List<Tag> tags;

    public Certificate() {
    }

    public Certificate(Long id, String name, String description, BigDecimal price, Long duration,
                       ZonedDateTime createDate, ZonedDateTime lastUpdateDate, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    public Certificate(Long id, String name, String description, BigDecimal price, Long duration,
                       ZonedDateTime createDate, ZonedDateTime lastUpdateDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public Certificate(Long id, String name, String description, BigDecimal price, Long duration,
                       String createDate, String lastUpdateDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = ZonedDateTime
                .parse(createDate);
        this.lastUpdateDate = ZonedDateTime
                .parse(lastUpdateDate);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(ZonedDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void setTags(Tag... tags) {
        setTags(Arrays.asList(tags));
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void addTags(Tag... additionalTags) {
        tags.addAll(
                Arrays.asList(additionalTags));
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", tags=" + tags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Certificate that = (Certificate) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && (price.compareTo(that.price) == 0)
                && Objects.equals(duration, that.duration)
                && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration, tags);
    }
}
