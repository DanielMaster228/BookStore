package ru.javabegin.BookStore.entity;

import java.util.ArrayList;

public class account {

    private int money;

    private ArrayList<books> books;

    public boolean checkId(int id) {
        for(books i: books) {
            if(i.getId() == id) {
                return false;
            }
        }
        return true;
    }

    public books GetBookById(int id) {
        for(books i: books) {
            if(i.getId() == id) {
                return i;
            }
        }
        return null;
    }
    public ArrayList<ru.javabegin.BookStore.entity.books> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<ru.javabegin.BookStore.entity.books> books) {
        this.books = books;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
