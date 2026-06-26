package app.bookstore.db;

import app.bookstore.db.models.PostRecord;
import app.bookstore.db.models.ProductRecord;
import app.bookstore.db.utils.BookStoreDBAccessDetails;
import app.bookstore.db.utils.DBClient;
import io.qameta.allure.Step;

import java.util.List;

public class BookStoreDB {
    private final static ThreadLocal<BookStoreDB> bookStoreDb = new ThreadLocal<>();
    private final DBClient database;

    private BookStoreDB() {
        database = new DBClient(BookStoreDBAccessDetails.getDbAccessDetails());
    }

    public static void init() {
        bookStoreDb.set(new BookStoreDB());
    }

    public static void remove() {
        bookStoreDb.remove();
    }

    public static BookStoreDB getDb() {
        return bookStoreDb.get();
    }

    @Step("SELECT products")
    public List<ProductRecord> selectProducts() {
        return database.getResultsForQuery("SELECT * FROM wp_wc_product_meta_lookup",
                ProductRecord.class);
    }

    @Step("SELECT posts")
    public List<PostRecord> selectPosts() {
        return database.getResultsForQuery("SELECT * FROM wp_posts",
                PostRecord.class);
    }

    @Step("SELECT orders")
    public List<PostRecord> selectOrders() {
        return database.getResultsForQuery("SELECT * FROM wp_posts WHERE post_type = 'shop_order'",
                PostRecord.class);
    }

    @Step("SELECT product named {name}")
    public List<PostRecord> selectNameFromPosts(String name) {
        return database.getResultsForQuery("SELECT * FROM wp_posts WHERE post_title = \"" + name + "\"",
                PostRecord.class);
    }

    @Step("SELECT all active products")
    public List<PostRecord> selectActiveProducts() {
        return database.getResultsForQuery("SELECT * from wp_posts where guid LIKE '%post_type=product%'",
                PostRecord.class);
    }

    @Step("SELECT random active product")
    public PostRecord selectRandomActiveProduct() {
        return database.getResultForQuery("SELECT * from wp_posts where guid LIKE '%post_type=product%' LIMIT 1",
                PostRecord.class);
    }

}
