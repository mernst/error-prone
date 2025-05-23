/*
 * Copyright 2013 The Error Prone Authors.
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

package com.google.errorprone.bugpatterns.inject;

import com.google.errorprone.CompilationTestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author sgoldfeder@google.com (Steven Goldfeder)
 */
@RunWith(JUnit4.class)
public class MoreThanOneQualifierTest {

  private final CompilationTestHelper compilationHelper =
      CompilationTestHelper.newInstance(MoreThanOneQualifier.class, getClass());

  @Test
  public void positiveCase() {
    compilationHelper
        .addSourceLines(
            "MoreThanOneQualifierPositiveCases.java",
"""
package com.google.errorprone.bugpatterns.inject.testdata;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.inject.BindingAnnotation;
import java.lang.annotation.Retention;
import javax.inject.Qualifier;

/**
 * @author sgoldfeder@google.com (Steven Goldfeder)
 */
public class MoreThanOneQualifierPositiveCases {

  /**
   * A class in which the class, a constructor, a field, a method, and a method parameter each have
   * two com.google.inject.BindingAnnotation annotations.
   */
  // BUG: Diagnostic contains: remove
  @Foo1
  // BUG: Diagnostic contains: remove
  @Foo2
  public class TestClass1 {
    // BUG: Diagnostic contains: remove
    @Foo1
    // BUG: Diagnostic contains: remove
    @Foo2
    private int n;

    // BUG: Diagnostic contains: remove
    @Foo1
    // BUG: Diagnostic contains: remove
    @Foo2
    public TestClass1() {}

    // BUG: Diagnostic contains: remove
    @Foo1
    // BUG: Diagnostic contains: remove
    @Foo2
    public void setN(
        // BUG: Diagnostic contains: remove
        @Foo1
            // BUG: Diagnostic contains: remove
            @Foo2
            int n) {}
  }

  /**
   * A class in which the class, a constructor, a field, a method, and a method parameter each have
   * two javax.inject.Qualifier annotations.
   */

  // BUG: Diagnostic contains: remove
  @Bar1
  // BUG: Diagnostic contains: remove
  @Bar2
  public class TestClass2 {
    // BUG: Diagnostic contains: remove
    @Bar1
    // BUG: Diagnostic contains: remove
    @Bar2
    private int n;

    // BUG: Diagnostic contains: remove
    @Bar1
    // BUG: Diagnostic contains: remove
    @Bar2
    public TestClass2() {}

    // BUG: Diagnostic contains: remove
    @Bar1
    // BUG: Diagnostic contains: remove
    @Bar2
    public void setN(
        // BUG: Diagnostic contains: remove
        @Bar1
            // BUG: Diagnostic contains: remove
            @Bar2
            int n) {}
  }

  /**
   * A class in which the class, a constructor, a field, a method, and a method parameter each have
   * one javax.inject.Qualifier annotation and one com.google.inject.BindingAnnotation annotation.
   */

  // BUG: Diagnostic contains: remove
  @Foo1
  // BUG: Diagnostic contains: remove
  @Bar1
  public class TestClass3 {
    // BUG: Diagnostic contains: remove
    @Foo1
    // BUG: Diagnostic contains: remove
    @Bar1
    private int n;

    // BUG: Diagnostic contains: remove
    @Foo1
    // BUG: Diagnostic contains: remove
    @Bar1
    public TestClass3() {}

    // BUG: Diagnostic contains: remove
    @Foo1
    // BUG: Diagnostic contains: remove
    @Bar1
    public void setN(
        // BUG: Diagnostic contains: remove
        @Foo1
            // BUG: Diagnostic contains: remove
            @Bar1
            int n) {}
  }

  @Qualifier
  @Retention(RUNTIME)
  public @interface Foo1 {}

  @Qualifier
  @Retention(RUNTIME)
  public @interface Foo2 {}

  @BindingAnnotation
  @Retention(RUNTIME)
  public @interface Bar1 {}

  @BindingAnnotation
  @Retention(RUNTIME)
  public @interface Bar2 {}
}\
""")
        .doTest();
  }

  @Test
  public void negativeCase() {
    compilationHelper
        .addSourceLines(
            "MoreThanOneQualifierNegativeCases.java",
"""
package com.google.errorprone.bugpatterns.inject.testdata;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.inject.BindingAnnotation;
import java.lang.annotation.Retention;
import javax.inject.Qualifier;

/**
 * @author sgoldfeder@google.com (Steven Goldfeder)
 */
public class MoreThanOneQualifierNegativeCases {
  /** A class in with no annotations on any of its members. */
  public class TestClass1 {
    private int n;

    public TestClass1() {}

    public void setN(int n) {}
  }

  /**
   * A class in which a single javax.inject.Qualifier annotation is on the class, on a constructor,
   * on a field, on a method, and on a method parameter.
   */
  @Foo
  public class TestClass2 {
    @Foo private int n;

    @Foo
    public TestClass2() {}

    @Foo
    public void setN(@Foo int n) {}
  }

  /**
   * A class in which a single com.google.inject.BindingAnnotation annotation is on the class, on a
   * constructor, on a field, on a method, and on a method parameter.
   */
  @Bar
  public class TestClass3 {
    @Bar private int n;

    @Bar
    public TestClass3() {}

    @Bar
    public void setN(@Bar int n) {}
  }

  @Qualifier
  @Retention(RUNTIME)
  public @interface Foo {}

  @BindingAnnotation
  @Retention(RUNTIME)
  public @interface Bar {}
}\
""")
        .doTest();
  }
}
