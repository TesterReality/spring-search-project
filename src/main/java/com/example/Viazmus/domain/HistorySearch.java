package com.example.Viazmus.domain;

import javax.persistence.*;

/*
<ul th:each="historysearch : ${historysearchs}">
    <li th:text="${historysearch.id}"></li>
    <li th:text="${historysearch.text}"></li>
</ul>
 */
@Entity // This tells Hibernate to make a table out of this class
public class HistorySearch {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "usr_id")
    private User user;

    private String text;

    public HistorySearch() {
    }
    public HistorySearch(String text,User user) {
        this.text = text;
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
