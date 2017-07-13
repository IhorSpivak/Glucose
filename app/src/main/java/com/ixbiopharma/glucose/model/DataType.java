package com.ixbiopharma.glucose.model;

import java.util.Date;

import io.realm.RealmModel;

/**
 * DataType
 *
 * Created by ivan on 10.04.17.
 */

public interface DataType extends RealmModel {

    @Type int getDataType();

    int getId();

    void setId(int id);

    Date getDate();

    double getValue();

    String getSubTitle();

}
