package com.cafeform.dndapp;

import java.lang.reflect.Type;

import org.springframework.ai.tool.execution.ToolCallResultConverter;
import org.springframework.ai.util.json.JsonParser;
import org.springframework.lang.Nullable;

public class PlainStringConverter implements ToolCallResultConverter {

    @Override
    public String convert(@Nullable Object result, @Nullable Type returnType) {
        if (result instanceof String s) {
            return s;
        }
        return JsonParser.toJson(result);
    }

}
