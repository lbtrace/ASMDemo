package com.lbtrace.transform;

import com.android.annotations.NonNull;
import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.utils.FileUtils;
import com.lbtrace.asm.strategy.AddFieldStrategy;
import com.lbtrace.asm.strategy.AddMethodStrategy;
import com.lbtrace.asm.strategy.WeaveStrategy;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 使用 ASM 实现的修改字节码的 Transform
 *
 * @author wanglianbao (wanglianbao@keep.com)
 * @date 2019/4/8
 */
public class ASMTransform extends Transform {
    private static final String LOG_TAG = ASMTransform.class.getSimpleName();
    private Map<String, WeaveStrategy> targetClasses =
            new HashMap<>();

    {
        targetClasses.put("ASMAddField.class", new AddFieldStrategy());
        targetClasses.put("ASMAddMethod.class", new AddMethodStrategy());
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);

        final boolean isIncrement = transformInvocation.isIncremental();
        final Collection<TransformInput> inputs = transformInvocation.getInputs();
        final Collection<TransformInput> referencedInputs = transformInvocation.getReferencedInputs();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        for (TransformInput input : inputs) {
            for (JarInput jarInput : input.getJarInputs()) {
                File dest = outputProvider.getContentLocation(
                        jarInput.getFile().getAbsolutePath(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR);
                FileUtils.copyFile(jarInput.getFile(), dest);
            }

            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                File dest = outputProvider.getContentLocation(
                        directoryInput.getName(),
                        directoryInput.getContentTypes(),
                        directoryInput.getScopes(),
                        Format.DIRECTORY);
                try {
                    FileUtils.copyDirectory(directoryInput.getFile(), dest);
                    transformDir(directoryInput.getFile(), dest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getName() {
        return LOG_TAG;
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return true;
    }

    private void transformDir(@NonNull final File inputDir, @NonNull final File outputDir) throws IOException {
        final String inputDirPath = inputDir.getAbsolutePath();
        final String outputDirPath = outputDir.getAbsolutePath();

        if (!inputDir.isDirectory()) {
            return;
        }

        for (File file : FileUtils.getAllFiles(inputDir)) {
            String filePath = file.getAbsolutePath();
            File outputFile = new File(filePath.replace(inputDirPath, outputDirPath));


            if (!targetClasses.containsKey(file.getName())) {
                FileUtils.copyFile(file, outputFile);
                continue;
            }

            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }

            targetClasses.get(file.getName())
                    .weaveCode(file.getAbsolutePath(), outputFile.getAbsolutePath());
        }
    }
}
