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
    }
}
