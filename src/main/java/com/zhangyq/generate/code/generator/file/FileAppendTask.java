package com.zhangyq.generate.code.generator.file;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.zhangyq.generate.code.common.ValueContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author zhangyq01
 * @ClassName: FileAppendTask
 * @date 2024/3/6
 */
public class FileAppendTask implements Runnable {
    private final String text;

    public FileAppendTask(String text) {
        this.text = text;
    }

    @Override
    public void run() {
        try {
            StringBuilder sb = new StringBuilder();
            List<String> strings = Files.readAllLines(ValueContext.getPath());
            for (int i = 0; i < strings.size(); i++) {
                if (Pattern.matches("^}", strings.get(i))) {
                    sb.append(text);
                    sb.append("\n");
                    sb.append("}");
                    break;
                } else {
                    sb.append(strings.get(i));
                    sb.append("\n");
                }
            }
            Files.write(ValueContext.getPath(), sb.toString().getBytes());
            VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(
                    ValueContext.getPath().toString());
            new OpenFileDescriptor(ValueContext.getEvent().getProject(), virtualFile).navigate(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}