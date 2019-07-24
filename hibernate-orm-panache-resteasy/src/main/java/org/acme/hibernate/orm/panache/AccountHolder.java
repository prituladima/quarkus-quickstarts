package org.acme.hibernate.orm.panache;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Cacheable
public class AccountHolder  extends PanacheEntity {
    @Column(length = 40, unique = true)
    public String name;
    @Column
    public int balance;

    public AccountHolder() {
    }

    public AccountHolder(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }
}
