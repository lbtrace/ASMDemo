package com.lbtrace.asm.strategy;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;

/**
 * 类中删除 Method 的 ASM 实现
 *
 * @author wanglianbao (wanglianbao@keep.com)
 * @date 2019/4/15
 */
public class DeleteMethodStrategy implements WeaveStrategy {
    private static final int methodACC = ACC_PUBLIC | ACC_STATIC;
    private static final String methodName = "deleteMethod";
    private static final String methodDesc = "()V";
    @Override
    public void weaveCode(String input, String output) {
        WeaveUtil.weave(input, output, this,
                this.getClass(), DeleteMethodAdapter.class);
    }

    private class DeleteMethodAdapter extends ClassVisitor {
        public DeleteMethodAdapter(int api, ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (access == methodACC
                    && name.equals(methodName)
                    && desc.equals(methodDesc)) {
                return null;
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
        }
    }

}
