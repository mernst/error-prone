/*
 * Copyright 2012 The Error Prone Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.errorprone.bugpatterns;

import com.google.errorprone.CompilationTestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author eaftan@google.com (Eddie Aftandilian)
 */
@RunWith(JUnit4.class)
public class ArrayEqualsTest {

  private final CompilationTestHelper compilationHelper =
      CompilationTestHelper.newInstance(ArrayEquals.class, getClass());

  @Test
  public void positiveCase() {
    compilationHelper
        .addSourceLines(
            "ArrayEqualsPositiveCases.java",
            """
            package com.google.errorprone.bugpatterns.testdata;

            import com.google.common.base.Objects;

            /**
             * @author eaftan@google.com (Eddie Aftandilian)
             */
            public class ArrayEqualsPositiveCases {

              public void intArray() {
                int[] a = {1, 2, 3};
                int[] b = {1, 2, 3};

                // BUG: Diagnostic contains: Arrays.equals(a, b)
                if (a.equals(b)) {
                  System.out.println("arrays are equal!");
                } else {
                  System.out.println("arrays are not equal!");
                }

                // BUG: Diagnostic contains: Arrays.equals(a, b)
                if (Objects.equal(a, b)) {
                  System.out.println("arrays are equal!");
                } else {
                  System.out.println("arrays are not equal!");
                }
              }

              public void objectArray() {
                Object[] a = new Object[3];
                Object[] b = new Object[3];

                // BUG: Diagnostic contains: Arrays.equals(a, b)
                if (a.equals(b)) {
                  System.out.println("arrays are equal!");
                } else {
                  System.out.println("arrays are not equal!");
                }

                // BUG: Diagnostic contains: Arrays.equals(a, b)
                if (Objects.equal(a, b)) {
                  System.out.println("arrays are equal!");
                } else {
                  System.out.println("arrays are not equal!");
                }
              }

              public void firstMethodCall() {
                String s = "hello";
                char[] b = new char[3];

                // BUG: Diagnostic contains: Arrays.equals(s.toCharArray(), b)
                if (s.toCharArray().equals(b)) {
                  System.out.println("arrays are equal!");
                } else {
                  System.out.println("arrays are not equal!");
                }
              }

              public void secondMethodCall() {
                char[] a = new char[3];
                String s = "hello";

                // BUG: Diagnostic contains: Arrays.equals(a, s.toCharArray())
                if (a.equals(s.toCharArray())) {
                  System.out.println("arrays are equal!");
                } else {
                  System.out.println("arrays are not equal!");
                }
              }

              public void bothMethodCalls() {
                String s1 = "hello";
                String s2 = "world";

                // BUG: Diagnostic contains: Arrays.equals(s1.toCharArray(), s2.toCharArray())
                if (s1.toCharArray().equals(s2.toCharArray())) {
                  System.out.println("arrays are equal!");
                } else {
                  System.out.println("arrays are not equal!");
                }
              }
            }\
            """)
        .doTest();
  }

  @Test
  public void java7PositiveCase() {
    compilationHelper
        .addSourceLines(
            "ArrayEqualsPositiveCases2.java",
            """
            package com.google.errorprone.bugpatterns.testdata;

            import java.util.Objects;

            /**
             * Tests that only run with Java 7 and above.
             *
             * @author eaftan@google.com (Eddie Aftandilian)
             */
            public class ArrayEqualsPositiveCases2 {

              public void intArray() {
                int[] a = {1, 2, 3};
                int[] b = {1, 2, 3};

                // BUG: Diagnostic contains: Arrays.equals(a, b)
                if (Objects.equals(a, b)) {
                  System.out.println("arrays are equal!");
                } else {
                  System.out.println("arrays are not equal!");
                }
              }

              public void objectArray() {
                Object[] a = new Object[3];
                Object[] b = new Object[3];

                // BUG: Diagnostic contains: Arrays.equals(a, b)
                if (Objects.equals(a, b)) {
                  System.out.println("arrays are equal!");
                } else {
                  System.out.println("arrays are not equal!");
                }
              }
            }\
            """)
        .doTest();
  }

  @Test
  public void negativeCase() {
    compilationHelper
        .addSourceLines(
            "ArrayEqualsNegativeCases.java",
            """
            package com.google.errorprone.bugpatterns.testdata;

            import com.google.common.base.Objects;

            /**
             * @author eaftan@google.com (Eddie Aftandilian)
             */
            public class ArrayEqualsNegativeCases {
              public void neitherArray() {
                Object a = new Object();
                Object b = new Object();

                if (a.equals(b)) {
                  System.out.println("Objects are equal!");
                } else {
                  System.out.println("Objects are not equal!");
                }

                if (Objects.equal(a, b)) {
                  System.out.println("Objects are equal!");
                } else {
                  System.out.println("Objects are not equal!");
                }
              }

              public void firstArray() {
                Object[] a = new Object[3];
                Object b = new Object();

                if (a.equals(b)) {
                  System.out.println("arrays are equal!");
                } else {
                  System.out.println("arrays are not equal!");
                }

                if (Objects.equal(a, b)) {
                  System.out.println("Objects are equal!");
                } else {
                  System.out.println("Objects are not equal!");
                }
              }

              public void secondArray() {
                Object a = new Object();
                Object[] b = new Object[3];

                if (a.equals(b)) {
                  System.out.println("arrays are equal!");
                } else {
                  System.out.println("arrays are not equal!");
                }

                if (Objects.equal(a, b)) {
                  System.out.println("Objects are equal!");
                } else {
                  System.out.println("Objects are not equal!");
                }
              }
            }\
            """)
        .doTest();
  }

  @Test
  public void java7NegativeCase() {
    compilationHelper
        .addSourceLines(
            "ArrayEqualsNegativeCases2.java",
            """
            package com.google.errorprone.bugpatterns.testdata;

            import java.util.Objects;

            /**
             * Tests that only run with Java 7 and above.
             *
             * @author eaftan@google.com (Eddie Aftandilian)
             */
            public class ArrayEqualsNegativeCases2 {
              public void neitherArray() {
                Object a = new Object();
                Object b = new Object();

                if (Objects.equals(a, b)) {
                  System.out.println("Objects are equal!");
                } else {
                  System.out.println("Objects are not equal!");
                }
              }

              public void firstArray() {
                Object[] a = new Object[3];
                Object b = new Object();

                if (Objects.equals(a, b)) {
                  System.out.println("arrays are equal!");
                } else {
                  System.out.println("arrays are not equal!");
                }
              }

              public void secondArray() {
                Object a = new Object();
                Object[] b = new Object[3];

                if (Objects.equals(a, b)) {
                  System.out.println("arrays are equal!");
                } else {
                  System.out.println("arrays are not equal!");
                }
              }
            }\
            """)
        .doTest();
  }
}
