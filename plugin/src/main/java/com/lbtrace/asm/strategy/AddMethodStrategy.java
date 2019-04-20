package com.lbtrace.asm.strategy;

import org.apache.http.util.TextUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.spec.OAEPParameterSpec;

import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.POP;

/**
 * 类中添加方法的实现
 *
 * @author wanglianbao (wanglianbao@keep.com)
 * @date 2019/4/8
 */
public class AddMethodStrategy implements WeaveStrategy {
    private static final int methodACC = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC;
    private static final String methodName = "addMethod";
    private static final String methodDesc = "()V";
    private boolean isMethodPresent = false;

    @Override
    public void weaveCode(String input, String output) {
        WeaveUtil.weave(input, output, this, this.getClass(), AddMethodAdapter.class);
    }

    private class AddMethodAdapter extends ClassVisitor {
        public AddMethodAdapter(int api, ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (name.equals(methodName) && desc.equals(methodDesc)) {
                isMethodPresent = true;
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
        }

        @Override
        public void visitEnd() {
            if (!isMethodPresent) {
                MethodVisitor methodVisitor = cv.visitMethod(methodACC,
                        methodName, methodDesc, null, null);
                if (methodVisitor != null) {
                    methodVisitor.visitCode();
                    methodVisitor.visitLdcInsn("ASM.ASMAddMethod");
                    methodVisitor.visitLdcInsn("this is add method");
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC,
                            "android/util/Log",
                            "i",
                            "(Ljava/lang/String;Ljava/lang/String;)I",
                            false);
                    methodVisitor.visitInsn(POP);
                    methodVisitor.visitInsn(Opcodes.RETURN);
                    methodVisitor.visitMaxs(2, 0);
                    methodVisitor.visitEnd();
                }
            }
            super.visitEnd();
        }
    }


}
