package com.lbtrace;

import com.android.build.gradle.AppExtension;
import com.lbtrace.transform.ASMTransform;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Collections;

/**
 * 用于实现字节码修改
 *
 * @author wanglianbao (wanglianbao@keep.com)
 * @date 2019/4/8
 */
public class ASMPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        final AppExtension appExtension =
                (AppExtension) project.getProperties().get("android");
        if (appExtension != null) {
            appExtension.registerTransform(new ASMTransform(), Collections.EMPTY_LIST);
        }
    }
}