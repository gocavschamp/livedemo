package com.fish.live.search.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SearchHistoryBean {
    @Id(autoincrement = true)
    Long id;
    @Unique
    private String name;
    @Generated(hash = 1913301682)
    public SearchHistoryBean(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 1570282321)
    public SearchHistoryBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
