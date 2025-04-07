package models;

import java.util.List;

@lombok.Data
public class UserListModel {

    String page, per_page, total, total_pages;
    List<Data> data;
    Support support;
}
