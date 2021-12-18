package com.daou.emoticon;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.event.annotation.BeforeTestExecution;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

public class FindSlowTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {
    private static final long THRESHOLD = 1000L;

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        Method testMethod = extensionContext.getRequiredTestMethod();
        Annotation slowTestAnnotation = testMethod.getAnnotation(SlowTest.class);
        String testMethodName = testMethod.getName();
        ExtensionContext.Store store = getStore(extensionContext);
        long startTime = store.remove("START_TIME", long.class);
        long durationTime = System.currentTimeMillis() - startTime;
        if (durationTime > THRESHOLD && Objects.isNull(slowTestAnnotation)) {
            System.out.printf("please consider mark method [%s] with @SlowTest.\n", testMethodName);
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        ExtensionContext.Store store = getStore(extensionContext);
        store.put("START_TIME", System.currentTimeMillis());
    }

    private ExtensionContext.Store getStore(ExtensionContext extensionContext) {
        String testClassName = extensionContext.getRequiredTestClass().getName();
        String testMethodName = extensionContext.getRequiredTestMethod().getName();
        return extensionContext.getStore(ExtensionContext.Namespace.create(testClassName, testMethodName));
    }
}
