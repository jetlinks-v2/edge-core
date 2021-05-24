package org.jetlinks.edge.core.driver.function;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MethodInvokeFunctionDriverTest {


    @Test
    void test() {
        doTest("testVoid", Collections.emptyMap()).as(StepVerifier::create)
                                                  .expectComplete()
                                                  .verify();

    }


    @Test
    void test2() {

        doTest("testVoidIntArg", Collections.singletonMap("arg", "10"),int.class)
            .as(StepVerifier::create)
            .expectComplete()
            .verify();

    }

    @Test
    void testAdd() {

        doTest("testAdd", Collections.singletonMap("arg", "10"),int.class)
            .as(StepVerifier::create)
            .expectNext(11)
            .verifyComplete();

    }

    @Test
    void testList() {

        doTest("testList", Collections.singletonMap("arg", Arrays.asList("1","2",3)), List.class)
            .as(StepVerifier::create)
            .expectNext(1,2,3)
            .verifyComplete();

    }


    private Flux<Object> doTest(String methodName, Map<String, Object> args, Class<?>... argType) {
        MethodInvokeFunctionDriver driver = new MethodInvokeFunctionDriver(
            "test", "test", new TestFunctionClass(), ReflectionUtils.findMethod(TestFunctionClass.class, methodName, argType)
        );
        System.out.println(driver.getInputs());
        System.out.println(driver.getOutput());
        return driver.invoke(args);
    }


    public static class TestFunctionClass {

        public void testVoid() {
            System.out.println(1);
        }

        public void testVoidIntArg(int arg) {
            System.out.println("testVoidIntArg:" + arg);
        }

        public int testAdd(int arg) {
           return  arg+1;
        }

        public Flux<Integer> testList(List<Integer> arg) {
            return Flux.fromIterable(arg);
        }
    }
}