package com.lbtrace.asm.strategy;

import com.android.annotations.NonNull;

/**
 * 字节码修改策略接口
 *
 * @author wanglianbao (wanglianbao@keep.com)
 * @date 2019/4/8
 */
public interface WeaveStrategy {
    void weaveCode(@NonNull String input, @NonNull String output);
}
