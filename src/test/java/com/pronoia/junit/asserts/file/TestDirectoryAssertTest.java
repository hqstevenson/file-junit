/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pronoia.junit.asserts.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.pronoia.junit.file.TestDirectory;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for the  class.
 */
public class TestDirectoryAssertTest {
  static final String TEST_CHILD_DIRECTORY_NAME = "child-directory";
  static final String TEST_FILE_NAME = "test.txt";

  @Rule
  public TestDirectory instance = new TestDirectory();

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertIsEmpty() throws Exception {
    TestDirectoryAssert.assertIsEmpty(instance);

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    try {
      TestDirectoryAssert.assertIsEmpty(instance);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s is not empty - contains [%s]", instance.toFile(), TEST_CHILD_DIRECTORY_NAME);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertNotEmpty() throws Exception {
    try {
      TestDirectoryAssert.assertNotEmpty(instance);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s is empty", instance.toFile());
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    instance.newFile(TEST_FILE_NAME);
    TestDirectoryAssert.assertNotEmpty(instance);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryChildCount() throws Exception {
    TestDirectoryAssert.assertChildCountEquals(instance, 0);

    instance.newFile(TEST_FILE_NAME);
    TestDirectoryAssert.assertChildCountEquals(instance, 1);

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    TestDirectoryAssert.assertChildCountEquals(instance, 2);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryChildFileCount() throws Exception {
    TestDirectoryAssert.assertFileCountEquals(instance, 0);

    instance.newFile(TEST_FILE_NAME);
    TestDirectoryAssert.assertFileCountEquals(instance, 1);

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    TestDirectoryAssert.assertFileCountEquals(instance, 1);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryChildDirectoryCount() throws Exception {
    TestDirectoryAssert.assertChildDirectoryCountEquals(instance, 0);

    instance.newFile(TEST_FILE_NAME);
    TestDirectoryAssert.assertChildDirectoryCountEquals(instance, 1);

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    TestDirectoryAssert.assertChildDirectoryCountEquals(instance, 1);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertContainsFile() throws Exception {
    try {
      TestDirectoryAssert.assertContainsFile(instance, TEST_FILE_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("File %s does not exist in directory %s", TEST_FILE_NAME, instance.toFile());
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    try {
      TestDirectoryAssert.assertContainsFile(instance, TEST_CHILD_DIRECTORY_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("%s in directory %s does not refer to a file", TEST_CHILD_DIRECTORY_NAME, instance.toFile());
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    instance.newFile(TEST_FILE_NAME);
    TestDirectoryAssert.assertContainsFile(instance, TEST_FILE_NAME);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertNotContainsFile() throws Exception {
    TestDirectoryAssert.assertNotContainsFile(instance, TEST_FILE_NAME);

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    // This test will pass because the entry is not a file (it is a directory), but a warning will be logged
    TestDirectoryAssert.assertNotContainsFile(instance, TEST_CHILD_DIRECTORY_NAME);

    instance.newFile(TEST_FILE_NAME);
    TestDirectoryAssert.assertNotContainsFile(instance, "non-existent.txt");
    try {
      TestDirectoryAssert.assertNotContainsFile(instance, TEST_FILE_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("File %s exists in directory %s", TEST_FILE_NAME, instance.toFile());
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertContainsDirectory() throws Exception {
    try {
      TestDirectoryAssert.assertContainsDirectory(instance, TEST_CHILD_DIRECTORY_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s does not exist in directory %s", TEST_CHILD_DIRECTORY_NAME, instance.toFile());
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    instance.newFile(TEST_FILE_NAME);
    TestDirectoryAssert.assertContainsDirectory(instance, TEST_CHILD_DIRECTORY_NAME);

    try {
      TestDirectoryAssert.assertContainsDirectory(instance, TEST_FILE_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("%s in directory %s does not refer to a directory", TEST_FILE_NAME, instance.toFile());
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertNotContainsDirectory() throws Exception {
    TestDirectoryAssert.assertNotContainsDirectory(instance, TEST_CHILD_DIRECTORY_NAME);

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    instance.newFile(TEST_FILE_NAME);

    // This test will pass because the entry is not a directory (it is a file), but a warning will be logged
    TestDirectoryAssert.assertNotContainsDirectory(instance, TEST_FILE_NAME);

    try {
      TestDirectoryAssert.assertNotContainsDirectory(instance, TEST_CHILD_DIRECTORY_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s exists in directory %s", TEST_CHILD_DIRECTORY_NAME, instance.toFile());
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }
}