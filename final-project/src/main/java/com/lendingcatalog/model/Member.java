package com.lendingcatalog.model;

public class Member {
    private String firstName;
    private String lastName;


public Member (String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
}

public String toString(){
    return firstName + ", " + lastName;
}

public String getFirstName() {
    return firstName;
}

public String getLastName() {
    return lastName;
}

public void setFirstName(String firstName) {
    this.firstName = firstName;
}

public void setLastName(String lastName) {
    this.lastName = lastName;
}
}

