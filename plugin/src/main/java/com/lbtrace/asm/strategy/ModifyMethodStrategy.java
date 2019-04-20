package com.lbtrace.asm.strategy;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LSTORE;
import static org.objectweb.asm.Opcodes.LSUB;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.POP;
import static org.objectweb.asm.Opcodes.RETURN;

/**
 * Des
 *
 * @author wanglianbao (wanglianbao@keep.com)
 * @date 2019/4/15
 */
public class ModifyMethodStrategy implements WeaveStrategy {
    private static final int methodACC = ACC_PUBLIC | ACC_STATIC;
    private static final String methodName = "modifyMethod";
    private static final String methodDesc = "()V";

    @Override
    public void weaveCode(String input, String output) {
        WeaveUtil.weave(input, output, this,
                this.getClass(), ModifyMethodAdapter.class);
    }

    private class ModifyMethodAdapter extends ClassVisitor {
        public ModifyMethodAdapter(int api, ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

            if (mv != null
                    && access == methodACC
                    && name.equals(methodName)
                    && desc.equals(methodDesc)) {
                mv = new MethodVisitorImpl(ASM5, methodACC, methodDesc, mv);
            }
            return mv;
        }
    }

    private class MethodVisitorImpl extends LocalVariablesSorter {
        private int start;
        private int end;

        protected MethodVisitorImpl(int api, int access, String desc, MethodVisitor mv) {
            super(api, access, desc, mv);
        }

        @Override
        public void visitCode() {
            super.visitCode();
            mv.visitMethodInsn(INVOKESTATIC,
                    "java/lang/System",
                    "currentTimeMillis",
                    "()J",
                    false);
            start = newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(LSTORE, start);
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == RETURN || opcode == ATHROW) {
                mv.visitMethodInsn(INVOKESTATIC,
                        "java/lang/System",
                        "currentTimeMillis",
                        "()J",
                        false);
                end = newLocal(Type.LONG_TYPE);
                mv.visitVarInsn(LSTORE, end);
                mv.visitLdcInsn("ASM.ASMModifyMethod");
                mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
                mv.visitInsn(DUP);
                mv.visitMethodInsn(INVOKESPECIAL,
                        "java/lang/StringBuilder",
                        "<init>",
                        "()V",
                        false);
                mv.visitLdcInsn("method consume time: ");
                mv.visitMethodInsn(INVOKEVIRTUAL,
                        "java/lang/StringBuilder",
                        "append",
                        "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                        false);
                mv.visitVarInsn(LLOAD, end);
                mv.visitVarInsn(LLOAD, start);
                mv.visitInsn(LSUB);
                mv.visitMethodInsn(INVOKEVIRTUAL,
                        "java/lang/StringBuilder",
                        "append",
                        "(J)Ljava/lang/StringBuilder;",
                        false);
                mv.visitLdcInsn("ms");
                mv.visitMethodInsn(INVOKEVIRTUAL,
                        "java/lang/StringBuilder",
                        "append",
                        "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                        false);
                mv.visitMethodInsn(INVOKEVIRTUAL,
                        "java/lang/StringBuilder",
                        "toString",
                        "()Ljava/lang/String;",
                        false);
                mv.visitMethodInsn(INVOKESTATIC,
                        "android/util/Log",
                        "i",
                        "(Ljava/lang/String;Ljava/lang/String;)I",
                        false);
                mv.visitInsn(POP);
            }
            super.visitInsn(opcode);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(maxStack + 4, maxLocals + 4);
        }
    }

}
