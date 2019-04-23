package com.lbtrace.asm.strategy;

import com.android.annotations.NonNull;

import org.apache.http.util.TextUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static org.objectweb.asm.Opcodes.ASM5;

/**
 * 连接 {@link org.objectweb.asm.ClassReader}、{@link org.objectweb.asm.ClassVisitor}
 * 以及 {@link org.objectweb.asm.ClassWriter}
 *
 * @author wanglianbao (wanglianbao@keep.com)
 * @date 2019/4/15
 */
public class WeaveUtil {
    private WeaveUtil() {

    }

    public static void weave(@NonNull String inputClass,
                             @NonNull String outputClass,
                             @NonNull WeaveStrategy weaveStrategy,
                             @NonNull Class outerClazz,
                             @NonNull Class adapterClazz) {
        if (TextUtils.isEmpty(inputClass) || TextUtils.isEmpty(outputClass)
                || outerClazz == null || adapterClazz == null || weaveStrategy == null) {
            throw new IllegalArgumentException("input .class file or output .class file or ClassVisitor is null");
        }

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(inputClass);
            final ClassReader classReader = new ClassReader(inputStream);
            final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            // 调用非静态内部类，需要使用外部类的实例
            final ClassVisitor classVisitor = (ClassVisitor) adapterClazz
                    .getDeclaredConstructor(outerClazz, int.class, ClassVisitor.class)
                    .newInstance(weaveStrategy, ASM5, classWriter);
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
            final byte[] newClassFile = classWriter.toByteArray();
            outputStream = new FileOutputStream(outputClass);
            outputStream.write(newClassFile);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                // Ignore Exception
            }
        }
    }
}
