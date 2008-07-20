package gr.dsigned.jmvc.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Post extends gr.dsigned.jmvc.db.Model {

    public Post() throws Exception {
        this.tableName = "posts";
    }

    public ArrayList<LinkedHashMap<String, String>> getLatestPosts(int numberToFetch) throws SQLException {
        db.from("posts");
        db.orderBy("pub_date", "DESC");
        db.limit(numberToFetch);
        return db.get();
    }

    public ArrayList<LinkedHashMap<String, String>> getPostsInInterval(Date from, Date to) throws SQLException {
        db.from("posts");
        db.orderBy("pub_date", "DESC");
        return db.get();
    }
}

        