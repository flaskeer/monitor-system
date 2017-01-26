package com.asterisk.opensource.monitor.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@Builder
@ToString
public class Goods {

    private Long id;

    private String goodsName;

    private int stock;

    private BigDecimal price;

    private Date createTime;


}
