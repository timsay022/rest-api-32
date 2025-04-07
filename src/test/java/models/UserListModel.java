package models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class UserListModel {

    String page, total;
    @JsonProperty("per_page") String perPage;
    @JsonProperty("total_pages") String totalPages;
    List<Data> data;
    Support support;
}
