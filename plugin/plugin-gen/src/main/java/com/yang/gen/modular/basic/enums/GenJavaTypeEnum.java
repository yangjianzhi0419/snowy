package com.yang.gen.modular.basic.enums;

import lombok.Getter;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@Getter
public enum GenJavaTypeEnum {

    /** Integer */
    Integer("Integer"),

    /** Long */
    Long("Long"),

    /** String */
    String("String"),

    /** Boolean */
    Boolean("Boolean"),

    /** Float */
    Float("Float"),

    /** Double */
    Double("Double"),

    /** Date */
    Date("Date"),

    /** BigDecimal */
    BigDecimal("BigDecimal");

    private final String value;

    GenJavaTypeEnum(String value) {
        this.value = value;
    }
}
