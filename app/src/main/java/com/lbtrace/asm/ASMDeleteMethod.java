package com.lbtrace.asm;

import android.util.Log;

/**
 * 使用 ASM 删除类的方法示例类
 *
 * @author wanglianbao (wanglianbao@keep.com)
 * @date 2019/4/8
 */
public class ASMDeleteMethod {
    public static final String LOG_TAG = "ASM.ASMDeleteMethod";

    private ASMDeleteMethod() {

    }

    public static void deleteMethod() {
        Log.i(LOG_TAG, "this is delete method");
    }
}
