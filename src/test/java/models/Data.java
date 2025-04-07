package models;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Data {

    String id, email, avatar;
    @JsonProperty("first_name") String firstName;
    @JsonProperty("last_name") String lastName;

}
