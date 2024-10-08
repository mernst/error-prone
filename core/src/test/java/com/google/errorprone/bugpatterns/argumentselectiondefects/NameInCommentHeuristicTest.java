/*
 * Copyright 2017 The Error Prone Authors.
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

package com.google.errorprone.bugpatterns.argumentselectiondefects;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.google.errorprone.BugPattern;
import com.google.errorprone.BugPattern.SeverityLevel;
import com.google.errorprone.CompilationTestHelper;
import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.bugpatterns.BugChecker.MethodInvocationTreeMatcher;
import com.google.errorprone.matchers.Description;
import com.google.errorprone.util.ASTHelpers;
import com.sun.source.tree.MethodInvocationTree;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for NameInCommentHeuristic
 *
 * @author andrewrice@google.com (Andrew Rice)
 */
@RunWith(JUnit4.class)
public class NameInCommentHeuristicTest {

  /** A {@link BugChecker} which runs the NameInCommentHeuristic and prints the result */
  @BugPattern(
      severity = SeverityLevel.ERROR,
      summary = "Runs NameInCommentHeuristic and prints the result")
  public static class NameInCommentHeuristicChecker extends BugChecker
      implements MethodInvocationTreeMatcher {

    @Override
    public Description matchMethodInvocation(MethodInvocationTree tree, VisitorState state) {
      ImmutableList<Parameter> formal =
          Parameter.createListFromVarSymbols(ASTHelpers.getSymbol(tree).getParameters());
      Stream<Parameter> actual =
          Parameter.createListFromExpressionTrees(tree.getArguments()).stream();

      Changes changes =
          Changes.create(
              formal.stream().map(f -> 1.0).collect(toImmutableList()),
              formal.stream().map(f -> 0.0).collect(toImmutableList()),
              Streams.zip(formal.stream(), actual, ParameterPair::create)
                  .collect(toImmutableList()));

      boolean result =
          !new NameInCommentHeuristic()
              .isAcceptableChange(changes, tree, ASTHelpers.getSymbol(tree), state);
      return buildDescription(tree).setMessage(String.valueOf(result)).build();
    }
  }

  @Test
  public void nameInCommentHeuristic_returnsTrue_whereCommentMatchesFormalParameter() {
    CompilationTestHelper.newInstance(NameInCommentHeuristicChecker.class, getClass())
        .addSourceLines(
            "Test.java",
            """
            abstract class Test {
              abstract void target(Object first);

              void test(Object first) {
                // BUG: Diagnostic contains: true
                target(first /* first */);
              }
            }
            """)
        .doTest();
  }

  @Test
  public void
      nameInCommentHeuristic_returnsTrue_wherePreceedingCommentWithEqualsMatchesFormalParameter() {
    CompilationTestHelper.newInstance(NameInCommentHeuristicChecker.class, getClass())
        .addSourceLines(
            "Test.java",
            """
            abstract class Test {
              abstract void target(Object first);

              void test(Object first) {
                // BUG: Diagnostic contains: true
                target(/* first= */ first);
              }
            }
            """)
        .doTest();
  }

  @Test
  public void
      nameInCommentHeuristic_returnsTrue_wherePreceedingCommentHasEqualsSpacesAndExtraText() {
    CompilationTestHelper.newInstance(NameInCommentHeuristicChecker.class, getClass())
        .addSourceLines(
            "Test.java",
            """
            abstract class Test {
              abstract void target(Object first);

              void test(Object first) {
                // BUG: Diagnostic contains: true
                target(/*note first = */ first);
              }
            }
            """)
        .doTest();
  }

  @Test
  public void nameInCommentHeuristic_returnsFalse_withNoComments() {
    CompilationTestHelper.newInstance(NameInCommentHeuristicChecker.class, getClass())
        .addSourceLines(
            "Test.java",
            """
            abstract class Test {
              abstract void target(Object first);

              void test(Object first) {
                // BUG: Diagnostic contains: false
                target(first);
              }
            }
            """)
        .doTest();
  }

  @Test
  public void nameInCommentHeuristic_returnsFalse_withCommentNotMatchingFormalParameter() {
    CompilationTestHelper.newInstance(NameInCommentHeuristicChecker.class, getClass())
        .addSourceLines(
            "Test.java",
            """
            abstract class Test {
              abstract void target(Object first);

              void test(Object first) {
                // BUG: Diagnostic contains: false
                target(first /* other */);
              }
            }
            """)
        .doTest();
  }

  @Test
  public void nameInCommentHeuristic_returnsTrue_whereLineCommentMatchesFormalParameter() {
    CompilationTestHelper.newInstance(NameInCommentHeuristicChecker.class, getClass())
        .addSourceLines(
            "Test.java",
            """
            abstract class Test {
              abstract void target(Object first, Object second);

              void test(Object first, Object second) {
                // BUG: Diagnostic contains: true
                target(
                    first, // first
                    second);
              }
            }
            """)
        .doTest();
  }
}
