package com.ixbiopharma.glucose.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * UserInfo
 *
 * Created by ivan on 15.05.17.
 */

@AllArgsConstructor
@Getter
@Setter
public class UserInfo {

    String firstName;
    String email;
    int userId;
}
