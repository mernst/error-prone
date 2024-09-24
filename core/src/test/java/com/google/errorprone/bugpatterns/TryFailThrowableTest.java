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
 * @author adamwos@google.com (Adam Wos)
 */
@RunWith(JUnit4.class)
public class TryFailThrowableTest {

  private final CompilationTestHelper compilationHelper =
      CompilationTestHelper.newInstance(TryFailThrowable.class, getClass());

  @Test
  public void positiveCases() {
    compilationHelper.addSourceFile("testdata/TryFailThrowablePositiveCases.java").doTest();
  }

  @Test
  public void negativeCases() {
    compilationHelper.addSourceFile("testdata/TryFailThrowableNegativeCases.java").doTest();
  }
}
