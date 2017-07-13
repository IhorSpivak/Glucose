package com.ixbiopharma.glucose.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * RegisterRequest
 * <p>
 * Created by ivan on 15.05.17.
 */

public class RegisterRequest extends BaseRequest {

    @SerializedName("first_name")
    @Expose
    String first_name;
    @SerializedName("last_name")
    @Expose
    String last_name;
    @SerializedName("email")
    @Expose
    String email;
    @SerializedName("password")
    @Expose
    String password;
    @SerializedName("height")
    @Expose
    double height;
    @SerializedName("weight")
    @Expose
    double weight;
    @SerializedName("join_reason")
    @Expose
    int join_reason;
    @SerializedName("age")
    @Expose
    int age;

    public RegisterRequest() {
    }

    public RegisterRequest(String first_name, String last_name, String email, String password, double height, double weight, int join_reason, int age) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.height = height;
        this.weight = weight;
        this.join_reason = join_reason;
        this.age = age;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getJoin_reason() {
        return join_reason;
    }

    public void setJoin_reason(int join_reason) {
        this.join_reason = join_reason;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
