package net.engineeringdigest.journalApp.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("journal_entries")
@Getter
@Setter
public class JournalEntry {

    @Id
    private String id;
    private String title;
    private String content;
    private LocalDate date;

    /*public LocalDate getDate() {
        return date;
    }

    public void setLocalDate(LocalDate date) {
        this.date = date;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }*/
}
