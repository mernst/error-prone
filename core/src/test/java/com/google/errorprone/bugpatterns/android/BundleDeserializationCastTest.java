/*
 * Copyright 2016 The Error Prone Authors.
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

package com.google.errorprone.bugpatterns.android;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.CompilationTestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author epmjohnston@google.com (Emily P.M. Johnston)
 */
@RunWith(JUnit4.class)
public class BundleDeserializationCastTest {
  private final CompilationTestHelper compilationHelper =
      CompilationTestHelper.newInstance(BundleDeserializationCast.class, getClass())
          .addSourceFile("testdata/stubs/android/os/Bundle.java")
          .addSourceFile("testdata/stubs/android/os/Parcel.java")
          .addSourceFile("testdata/stubs/android/os/Parcelable.java")
          .setArgs(ImmutableList.of("-XDandroidCompatible=true"));

  @Test
  public void positiveCaseGetCustomList() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            """
            import android.os.Bundle;
            import java.util.LinkedList;

            public class Test {
              void test() {
                Bundle bundle = new Bundle();
                // BUG: Diagnostic matches: X
                LinkedList myList = (LinkedList) bundle.getSerializable("key");
              }
            }
            """)
        .expectErrorMessage(
            "X",
            Predicates.and(
                Predicates.containsPattern("LinkedList may be transformed"),
                Predicates.containsPattern("cast to List")))
        .doTest();
  }

  @Test
  public void positiveCaseGetCustomMap() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            """
            import android.os.Bundle;
            import java.util.Hashtable;

            public class Test {
              void test() {
                Bundle bundle = new Bundle();
                // BUG: Diagnostic matches: X
                Hashtable myMap = (Hashtable) bundle.getSerializable("key");
              }
            }
            """)
        .expectErrorMessage(
            "X",
            Predicates.and(
                Predicates.containsPattern("Hashtable may be transformed"),
                Predicates.containsPattern("cast to Map")))
        .doTest();
  }

  @Test
  public void negativeCaseGetArrayList() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            """
            import android.os.Bundle;
            import java.util.ArrayList;

            public class Test {
              void test() {
                Bundle bundle = new Bundle();
                ArrayList<String> myList = (ArrayList<String>) bundle.getSerializable("key");
              }
            }
            """)
        .doTest();
  }

  @Test
  public void negativeCaseGetHashMap() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            """
            import android.os.Bundle;
            import java.util.HashMap;

            public class Test {
              void test() {
                Bundle bundle = new Bundle();
                HashMap myMap = (HashMap) bundle.getSerializable("key");
              }
            }
            """)
        .doTest();
  }

  @Test
  public void negativeCaseGetParcelableList() {
    compilationHelper
        .addSourceFile("testdata/CustomParcelableList.java")
        .addSourceLines(
            "Test.java",
            """
            import android.os.Bundle;
            import java.util.List;
            import com.google.errorprone.bugpatterns.android.testdata.CustomParcelableList;

            public class Test {
              void test() {
                Bundle bundle = new Bundle();
                CustomParcelableList myList = (CustomParcelableList) bundle.getSerializable("key");
              }
            }
            """)
        .doTest();
  }

  @Test
  public void negativeCaseNoCast() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            """
            import android.os.Bundle;

            public class Test {
              void test() {
                Bundle bundle = new Bundle();
                bundle.getSerializable("key");
              }
            }
            """)
        .doTest();
  }

  @Test
  public void negativeCaseOtherCast() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            """
            import android.os.Bundle;
            import java.lang.Integer;

            public class Test {
              void test() {
                Bundle bundle = new Bundle();
                Integer myObj = (Integer) bundle.getSerializable("key");
              }
            }
            """)
        .doTest();
  }

  @Test
  public void negativeCaseGetList() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            """
            import android.os.Bundle;
            import java.util.List;

            public class Test {
              void test() {
                Bundle bundle = new Bundle();
                List myList = (List) bundle.getSerializable("key");
              }
            }
            """)
        .doTest();
  }

  @Test
  public void negativeCaseGetMap() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            """
            import android.os.Bundle;
            import java.util.Map;

            public class Test {
              void test() {
                Bundle bundle = new Bundle();
                Map myMap = (Map) bundle.getSerializable("key");
              }
            }
            """)
        .doTest();
  }

  @Test
  public void negativeCaseGetPrimitive() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            """
            import android.os.Bundle;

            public class Test {
              void test() {
                Bundle bundle = new Bundle();
                int myInt = (int) bundle.getSerializable("key");
              }
            }
            """)
        .doTest();
  }

  @Test
  public void negativeCaseGetPrimitiveArray() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            """
            import android.os.Bundle;

            public class Test {
              void test() {
                Bundle bundle = new Bundle();
                int[] myArray = (int[]) bundle.getSerializable("key");
              }
            }
            """)
        .doTest();
  }

  @Test
  public void negativeCaseGetReferenceArray() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            """
            import android.os.Bundle;
            import java.util.TreeMap;

            public class Test {
              void test() {
                Bundle bundle = new Bundle();
                TreeMap[] myArray = (TreeMap[]) bundle.getSerializable("key");
              }
            }
            """)
        .doTest();
  }

  @Test
  public void positiveCaseGetCustomCharSequenceArray() {
    compilationHelper
        .addSourceLines(
            "CustomCharSequence.java",
            """
            public class CustomCharSequence implements CharSequence {
              @Override
              public int length() {
                return 0;
              }

              @Override
              public char charAt(int index) {
                return 0;
              }

              @Override
              public CharSequence subSequence(int start, int end) {
                return null;
              }
            }
            """)
        .addSourceLines(
            "Test.java",
            """
            import android.os.Bundle;

            public class Test {
              void test() {
                Bundle bundle = new Bundle();
                // BUG: Diagnostic matches: X
                CustomCharSequence[] cs = (CustomCharSequence[]) bundle.getSerializable("key");
              }
            }
            """)
        .expectErrorMessage(
            "X",
            Predicates.and(
                Predicates.containsPattern("CustomCharSequence\\[\\] may be transformed"),
                Predicates.containsPattern("cast to CharSequence\\[\\]")))
        .doTest();
  }

  @Test
  public void negativeCaseGetStringArray() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            """
            import android.os.Bundle;

            public class Test {
              void test() {
                Bundle bundle = new Bundle();
                String[] myArray = (String[]) bundle.getSerializable("key");
              }
            }
            """)
        .doTest();
  }
}
