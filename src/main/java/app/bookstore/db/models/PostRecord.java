package app.bookstore.db.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostRecord {
    @JsonAlias("post_title")
    private String name;
    @JsonAlias("post_name")
    private String slug;
    @JsonAlias("post_status")
    private String status;
    @JsonAlias("ID")
    private int id;
    @JsonAlias("post_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date postDate;
}
