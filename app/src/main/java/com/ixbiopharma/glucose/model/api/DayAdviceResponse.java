package com.ixbiopharma.glucose.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * DayAdviceResponse
 *
 * Created by ivan on 25.05.17.
 */

@Getter
@Setter
public class DayAdviceResponse {

    @SerializedName("morning")
    @Expose
    String morning;

    @SerializedName("day")
    @Expose
    String day;

    @SerializedName("evening")
    @Expose
    String evening;
}
