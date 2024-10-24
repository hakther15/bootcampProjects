package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

/**
 * Model class representing an application user.
 *
 * Contains information about the user - their id, username, address information,
 * password (hashed) and authorities (user roles).
 */
public class User {

   private int userId;
   private String username;
   @JsonIgnore
   private String hashedPassword;
   private String address;
   private String city;
   private String stateCode;
   private String ZIP;
   private String role;

   public User(int userId, String username, String hashedPassword, String address, String city, String stateCode, String ZIP, String role) {
      this.userId = userId;
      this.username = username;
      this.hashedPassword = hashedPassword;
      this.address = address;
      this.city = city;
      this.stateCode = stateCode;
      this.ZIP = ZIP;
      this.role = role;
   }

   public User(String username, String hashedPassword, String address, String city, String stateCode, String ZIP, String role) {
      this(0, username, hashedPassword, address, city, stateCode, ZIP, role);
   }

   public User() {

   }

   public int getId() {
      return userId;
   }

   public void setId(int userId) {
      this.userId = userId;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getHashedPassword() {
      return hashedPassword;
   }

   public void setHashedPassword(String hashedPassword) {
      this.hashedPassword = hashedPassword;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getCity() {
      return city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public String getStateCode() {
      return stateCode;
   }

   public void setStateCode(String stateCode) {
      this.stateCode = stateCode;
   }

   public String getZIP() {
      return ZIP;
   }

   public void setZIP(String ZIP) {
      this.ZIP = ZIP;
   }

   public String getRole() {
      return role;
   }

   public void setRole(String role) {
      this.role = role != null && !role.startsWith("ROLE_")
              ? "ROLE_" + role.toUpperCase()
              : role;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      User user = (User) o;
      return userId == userId &&
              Objects.equals(username, user.username) &&
              Objects.equals(hashedPassword, user.hashedPassword) &&
              Objects.equals(address, user.address) &&
              Objects.equals(city, user.city) &&
              Objects.equals(stateCode, user.stateCode) &&
              Objects.equals(ZIP, user.ZIP) &&
              Objects.equals(role, user.role);
   }

   @Override
   public int hashCode() {
      return Objects.hash(userId, username, hashedPassword, address, city, stateCode, ZIP, role);
   }

   @Override
   public String toString() {
      return "User{" +
              "id=" + userId +
              ", username='" + username + '\'' +
              ", hashedPassword='" + hashedPassword + '\'' +
              ", address='" + address + '\'' +
              ", city='" + city + '\'' +
              ", state='" + stateCode + '\'' +
              ", zipCode='" + ZIP + '\'' +
              ", role='" + role + '\'' +
              '}';
   }
}