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
public class AssistedInjectAndInjectOnConstructorsTest {

  private final CompilationTestHelper compilationHelper =
      CompilationTestHelper.newInstance(AssistedInjectAndInjectOnConstructors.class, getClass());

  @Test
  public void positiveCase() {
    compilationHelper
        .addSourceLines(
            "AssistedInjectAndInjectOnConstructorsPositiveCases.java",
"""
package com.google.errorprone.bugpatterns.inject.testdata;

import com.google.inject.assistedinject.AssistedInject;

/**
 * @author sgoldfeder@google.com (Steven Goldfeder)
 */
public class AssistedInjectAndInjectOnConstructorsPositiveCases {
  /**
   * Class has a constructor annotated with @javax.inject.Inject and another constructor annotated
   * with @AssistedInject.
   */
  // BUG: Diagnostic contains: AssistedInjectAndInjectOnConstructors
  public class TestClass1 {
    @javax.inject.Inject
    public TestClass1() {}

    @AssistedInject
    public TestClass1(int n) {}
  }

  /**
   * Class has a constructor annotated with @com.google.inject.Inject and another constructor
   * annotated with @AssistedInject.
   */
  // BUG: Diagnostic contains: AssistedInjectAndInjectOnConstructors
  public class TestClass2 {
    @com.google.inject.Inject
    public TestClass2() {}

    @AssistedInject
    public TestClass2(int n) {}
  }

  /**
   * Class has a constructor annotated with @com.google.inject.Inject, another constructor annotated
   * with @AssistedInject, and a third constructor with no annotation.
   */
  // BUG: Diagnostic contains: AssistedInjectAndInjectOnConstructors
  public class TestClass3 {
    @com.google.inject.Inject
    public TestClass3() {}

    @AssistedInject
    public TestClass3(int n) {}

    public TestClass3(String s) {}
  }
}\
""")
        .doTest();
  }

  @Test
  public void negativeCase() {
    compilationHelper
        .addSourceLines(
            "AssistedInjectAndInjectOnConstructorsNegativeCases.java",
"""
package com.google.errorprone.bugpatterns.inject.testdata;

import com.google.inject.assistedinject.AssistedInject;

/**
 * @author sgoldfeder@google.com (Steven Goldfeder)
 */
public class AssistedInjectAndInjectOnConstructorsNegativeCases {
  /** Class has a single constructor with no annotation. */
  public class TestClass1 {
    TestClass1() {}
  }

  /** Class has a constructor with a @javax.inject.Inject annotation. */
  public class TestClass2 {
    @javax.inject.Inject
    public TestClass2() {}
  }

  /** Class has a constructor with a @com.google.inject.Inject annotation. */
  public class TestClass3 {
    @com.google.inject.Inject
    public TestClass3() {}
  }

  /** Class has a constructor annotated with @AssistedInject */
  public class TestClass4 {
    @AssistedInject
    public TestClass4() {}
  }

  /** Class has a constructor with a @AssistedInject annotation as well as an injectable field */
  public class TestClass5 {
    @javax.inject.Inject private int n;

    @AssistedInject
    public TestClass5() {}
  }

  /**
   * Class has a constructor annotated with @javax.inject.Inject and another constructor annotated
   * with @AssistedInject. The warning is suppressed.
   */
  @SuppressWarnings("AssistedInjectAndInjectOnConstructors")
  public class TestClass6 {
    @javax.inject.Inject
    public TestClass6() {}

    @AssistedInject
    public TestClass6(int n) {}
  }
}\
""")
        .doTest();
  }
}
