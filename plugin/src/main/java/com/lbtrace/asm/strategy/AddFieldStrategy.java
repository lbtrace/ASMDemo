package com.lbtrace.asm.strategy;

import org.apache.http.util.TextUtils;
import org.objectweb.asm.ClassReader;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ASM5;


/**
 * 类中添加字段的实现
 *
 * @author wanglianbao (wanglianbao@keep.com)
 * @date 2019/4/8
 */
public class AddFieldStrategy implements WeaveStrategy {
    private static final String TARGET_CLASS_FILE = "ASMAddField.class";
    private static int fieldACC = ACC_PUBLIC;
    private static final String fieldName = "addField";
    private static final String fieldDesc = "Ljava/lang/Object;";
    private boolean isFieldPresent = false;

    @Override
    public void weaveCode(String input, String output) {
        WeaveUtil.weave(input, output, this, this.getClass(), AddFieldAdapter.class);
    }

    private class AddFieldAdapter extends ClassVisitor {
        public AddFieldAdapter(int api, ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            if (name.equals(fieldName)) {
                isFieldPresent = true;
            }
            return super.visitField(access, name, desc, signature, value);
        }

        @Override
        public void visitEnd() {
            if (!isFieldPresent) {
                FieldVisitor fieldVisitor =
                        cv.visitField(fieldACC, fieldName, fieldDesc, null, null);
                if (fieldVisitor != null) {
                    fieldVisitor.visitEnd();
                }
            }
            super.visitEnd();
        }
    }

}
