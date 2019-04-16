package com.lbtrace.asm;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 使用 ASM 进行 AOP 编程展示界面
 */
public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "ASM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dump();
    }

    private void dump() {
        Log.i(LOG_TAG, "ASM 修改方法名测试: ");
        Log.i(ASMModifyMethod.LOG_TAG,
                "modifyMethod" + " -----> " + getASMMethodName(ASMModifyMethod.class));
        Log.i(LOG_TAG, "ASM 修改字段名测试: ");
        Log.i(ASMModifyField.LOG_TAG,
                "modifyField" + " -----> " + getASMFieldName(ASMModifyField.class));
        try {
            Method m = ASMAddMethod.class.getDeclaredMethod("addMethod");
            m.invoke(ASMAddMethod.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ASMModifyMethod.modifyMethod();

    }

    @Nullable
    private String getASMMethodName(Class clazz) {
        final Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.getName().contains("Method")) {
                return method.getName();
            }
        }

        return null;
    }

    @Nullable
    private String getASMFieldName(Class clazz) {
        final Field[] fields = clazz.getFields();

        for (Field field : fields) {
            if (field.getName().contains("Field")) {
                return field.getName();
            }

        }

        return null;
    }
}
