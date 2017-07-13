package com.ixbiopharma.glucose.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * JournalDataTypeWrapper
 * <p>
 * Created by ivan on 27.05.17.
 */

@Getter
@Setter
@AllArgsConstructor
public class JournalDataTypeWrapper {
    List<DataType> dataList;
}
