package com.zxuhan.aicode.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

/**
 * Code generation type enumeration.
 */
@Getter
public enum CodeGenTypeEnum {

    HTML("Single-file HTML", "html"),
    MULTI_FILE("Multi-file native", "multi_file"),
    VUE_PROJECT("Vue project", "vue_project");

    private final String text;
    private final String value;

    CodeGenTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * Find an enum by its value.
     *
     * @param value enum string value
     * @return matching enum or null
     */
    public static CodeGenTypeEnum getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (CodeGenTypeEnum anEnum : CodeGenTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}