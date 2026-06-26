package app.bookstore.db.utils;

import app.bookstore.dto.Config;

public class BookStoreDBAccessDetails implements DBAccessDetail{
    private final String username;
    private final String password;
    private final String connString;

    BookStoreDBAccessDetails(){
        Config config = Config.getInstance();
        this.username = config.getDbUsername();
        this.password = config.getDbPassword();
        this.connString = config.getDbConnString();
    }

    public static DBAccessDetail getDbAccessDetails(){
        return new BookStoreDBAccessDetails();
    }

    @Override
    public String getConnString() {
        return connString;
    }

    @Override
    public String getUser() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
