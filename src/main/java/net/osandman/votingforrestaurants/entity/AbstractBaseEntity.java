package net.osandman.votingforrestaurants.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.util.ProxyUtils;
import org.springframework.lang.Nullable;

@MappedSuperclass
@Access(AccessType.FIELD)
@ToString
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseEntity implements Persistable<Integer>, HasId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    protected Integer id;

    @Override
    public Integer id() {
        return id;
    }

    @Override
    public boolean isNew() {
        return HasId.super.isNew();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !getClass().equals(ProxyUtils.getUserClass(o))) {
            return false;
        }
        AbstractBaseEntity that = (AbstractBaseEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += null == getId() ? 0 : getId().hashCode() * 31;
        return hashCode;
    }
}
