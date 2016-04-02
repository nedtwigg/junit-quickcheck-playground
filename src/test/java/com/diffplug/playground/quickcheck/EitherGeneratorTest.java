/*
 * Copyright 2016 DiffPlug
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
package com.diffplug.playground.quickcheck;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

import com.diffplug.common.base.Either;

@RunWith(JUnitQuickcheck.class)
public class EitherGeneratorTest {
	@Property
	public void testTypes(@From(EitherGenerator.class) Either<String, Integer> either) {
		if (either.isLeft()) {
			assertTrue(either.getLeft() instanceof String);
		} else {
			assertTrue(either.getRight() instanceof Integer);
		}
	}

	@Property
	public void testLefts(@From(EitherGenerator.class) Either<String, Integer> either) {
		assumeTrue(either.isLeft());
		assertTrue(either.getLeft() instanceof String);
	}

	@Property
	public void testRightsGreaterThan5(@From(EitherGenerator.class) Either<String, Integer> either) {
		assumeTrue(either.isRight());
		assumeTrue(either.getRight() > 5);
		assertTrue(either.getRight() > 5);
	}
}
