package com.lbtrace.asm.strategy;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

/**
 * 类中删除 Field 的 ASM 实现
 *
 * @author wanglianbao (wanglianbao@keep.com)
 * @date 2019/4/15
 */
public class DeleteFieldStrategy implements WeaveStrategy {
    private static final String fieldName = "deleteField";

    @Override
    public void weaveCode(String input, String output) {
        WeaveUtil.weave(input, output, this,
                this.getClass(), DeleteFieldAdapter.class);
    }

    private class DeleteFieldAdapter extends ClassVisitor {
        public DeleteFieldAdapter(int api, ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            if (name.equals(fieldName)) {
                return null;
            }
            return super.visitField(access, name, desc, signature, value);
        }
    }

}
