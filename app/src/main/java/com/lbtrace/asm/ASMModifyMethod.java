package com.lbtrace.asm;

import android.util.Log;

/**
 * 使用 ASM 修改类的方法示例类
 *
 * @author wanglianbao (wanglianbao@keep.com)
 * @date 2019/4/8
 */
public class ASMModifyMethod {
    public static final String LOG_TAG = "ASM.ASMModifyMethod";

    private ASMModifyMethod() {

    }

    public static void modifyMethod() {
        Log.i(LOG_TAG, "this is modify method");
    }
}
