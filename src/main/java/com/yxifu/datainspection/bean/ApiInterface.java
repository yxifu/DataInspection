package com.yxifu.datainspection.bean;

import lombok.Data;

/**
 * @author yxifu
 * @date 2020/05/24
 **/
@Data
public class ApiInterface<T> {
    private int error_code = 0;
    private String message;
    private T Data;
}
