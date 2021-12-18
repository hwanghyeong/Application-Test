package com.daou.emoticon;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@ExtendWith(FindSlowTestExtension.class)
class StudyTest {

    @RegisterExtension
    static FindSlowTestExtension findSlowTestExtension = new FindSlowTestExtension();

    @Test
    @DisplayName("스터디 생성 테스트")
    @Tag("fast")
    @Order(1)
    void create_test() {
        Study study = new Study(-10);

        assertAll(
                () -> assertNotNull(study),
                () -> assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "스터디를 처음 만들면 상태가 " + StudyStatus.DRAFT + " 여야 한다"),
                () -> assertTrue(study.getLimit() > 0, "스터디 최대 참석 인원은 0명보다 많아야 한다.")
        );
    }

    @SlowTest
    @Order(2)
    void create_test_again() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->  new Study(-10));
        String errorMessage = exception.getMessage();
        assertEquals(errorMessage, "limit은 0보다 커야한다.");
    }

    @DisplayName("스터디 만들기")
    @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetition}")
    void repeatTest(RepetitionInfo repetitionInfo) {
        System.out.println("repeat: " + repetitionInfo.getCurrentRepetition() + "/" + repetitionInfo.getTotalRepetitions());
    }

    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(strings = {"날씨가", "점점", "추워지고", "있다"})
    @NullAndEmptySource
    void parameterizedTest(String message) {
        System.out.println(message);
    }




    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(ints = {10, 20, 40})
    void parameterizedTest2(@ConvertWith(StudyConverter.class) Study study) {
        System.out.println(study.getLimit());
    }
    static class StudyConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object o, Class<?> aClass) throws ArgumentConversionException {
            assertEquals(Study.class, aClass, "Study클래스만 변환이 가능함.");
            return new Study(Integer.parseInt(o.toString()));
        }
    }

    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바스터디'", "20, '뷰스터디'"})
    void parameterizedTest3(ArgumentsAccessor accessor) {
        Study study = new Study(accessor.getInteger(0), accessor.getString(1));
        System.out.println(study.getLimit());
    }

    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바스터디'", "20, '뷰스터디'"})
    void parameterizedTest4(@AggregateWith(StudyAggregator.class) Study study) {
        System.out.println(study.getLimit());
    }
    static class StudyAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
            return new Study(accessor.getInteger(0), accessor.getString(1));
        }
    }



    @Test
    void create_test_again2() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->  new Study(-10));
        assertTimeout(Duration.ofMillis(1000), () -> {
            Study study = new Study(10);
            Thread.sleep(1000);
        });
        assertTimeoutPreemptively(Duration.ofMillis(1000), () -> {
            Study study = new Study(10);
            Thread.sleep(1000);
        });
        // TODO ThreadLocal
    }

    @Test
    @EnabledOnOs(OS.MAC)
    @DisabledOnOs(OS.LINUX)
    @DisabledOnJre(JRE.JAVA_8)
    void create_test_assume() {
        String testEnv = System.getenv("TEST_ENV");

        assumingThat("LOCAL".equalsIgnoreCase(testEnv), () -> {
            Study study = new Study(-10);
            assertNotNull(study );
        });

        assumeTrue("LOCAL".equalsIgnoreCase(testEnv));

        Study study = new Study(-10);
        assertNotNull(study );
    }


    // static void로 사용해야함
   @BeforeAll
    static void beforeAll() {
        System.out.println("before all");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("after all");
    }


    @BeforeEach
    void beforeEach() {
        System.out.println("before each");
    }

    @AfterEach
    void afterEach() {
        System.out.println("after each");
    }
}