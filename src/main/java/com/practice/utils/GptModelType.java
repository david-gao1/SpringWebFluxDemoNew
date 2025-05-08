package com.practice.utils;


public enum GptModelType {
    PRO("pro"),//专业模型，默认使用
    PRO_LONG("pro_long"),//长文本模型
    ;

    private String modelName;

    GptModelType(String modelName) {
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }

    // 静态方法，将字符串转换为枚举实例
    public static GptModelType fromString(String modelName) {
        for (GptModelType model : GptModelType.values()) {
            if (model.getModelName().equalsIgnoreCase(modelName)) {
                return model;
            }
        }
        throw new IllegalArgumentException("No enum constant with modelName: " + modelName);
    }
}
