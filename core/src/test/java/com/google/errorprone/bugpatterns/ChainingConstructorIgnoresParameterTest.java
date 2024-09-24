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

package com.google.errorprone.bugpatterns;

import com.google.errorprone.CompilationTestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author cpovirk@google.com (Chris Povirk)
 */
@RunWith(JUnit4.class)
public class ChainingConstructorIgnoresParameterTest {
  private final CompilationTestHelper compilationHelper =
      CompilationTestHelper.newInstance(ChainingConstructorIgnoresParameter.class, getClass());

  @Test
  public void positiveCase() {
    compilationHelper
        .addSourceFile("testdata/ChainingConstructorIgnoresParameterPositiveCases.java")
        .doTest();
  }

  @Test
  public void negativeCase() {
    compilationHelper
        .addSourceFile("testdata/ChainingConstructorIgnoresParameterNegativeCases.java")
        .doTest();
  }
}
