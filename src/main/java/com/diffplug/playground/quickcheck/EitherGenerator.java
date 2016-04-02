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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.pholser.junit.quickcheck.generator.ComponentizedGenerator;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import com.diffplug.common.base.Either;
import com.diffplug.common.base.Errors;

@SuppressWarnings("rawtypes")
public class EitherGenerator extends ComponentizedGenerator<Either> {
	public EitherGenerator() {
		super(Either.class);
	}

	@Override
	public Either generate(SourceOfRandomness random, GenerationStatus status) {
		if (random.nextBoolean()) {
			return Either.createLeft(componentGenerators().get(0).generate(random, status));
		} else {
			return Either.createRight(componentGenerators().get(1).generate(random, status));
		}
	}

	@Override
	public List<Either> doShrink(SourceOfRandomness random, Either larger) {
		if (larger.isLeft()) {
			return componentGenerators().get(0).shrink(random, larger.getLeft()).stream().map(Either::createLeft).collect(Collectors.toList());
		} else {
			return componentGenerators().get(1).shrink(random, larger.getRight()).stream().map(Either::createRight).collect(Collectors.toList());
		}
	}

	@Override
	public int numberOfNeededComponents() {
		return 2;
	}

	/** Generates an Either using either two generators, two generator classes with no-arg constructors, or any combination thereof. */
	public static <L, R> Either<L, R> generate(SourceOfRandomness random, GenerationStatus status, Generator<? extends L> leftGen, Generator<? extends R> rightGen) {
		EitherGenerator eitherGen = new EitherGenerator();
		eitherGen.addComponentGenerators(Arrays.asList(leftGen, rightGen));
		@SuppressWarnings("unchecked")
		Either<L, R> cast = (Either<L, R>) eitherGen.generate(random, status);
		return cast;
	}

	@SuppressWarnings("unchecked")
	private static <T> Generator<T> instantiate(Class<? extends Generator<? extends T>> genClass) {
		return Errors.rethrow().get(() -> (Generator<T>) genClass.newInstance());
	}

	/** Generates an Either using either two generators, two generator classes with no-arg constructors, or any combination thereof. */
	public static <L, R> Either<L, R> generate(SourceOfRandomness random, GenerationStatus status, Class<? extends Generator<? extends L>> leftGenClass, Class<? extends Generator<? extends R>> rightGenClass) {
		return generate(random, status, instantiate(leftGenClass), instantiate(rightGenClass));
	}

	/** Generates an Either using either two generators, two generator classes with no-arg constructors, or any combination thereof. */
	public static <L, R> Either<L, R> generate(SourceOfRandomness random, GenerationStatus status, Generator<? extends L> leftGen, Class<? extends Generator<? extends R>> rightGenClass) {
		return generate(random, status, leftGen, instantiate(rightGenClass));
	}

	/** Generates an Either using either two generators, two generator classes with no-arg constructors, or any combination thereof. */
	public static <L, R> Either<L, R> generate(SourceOfRandomness random, GenerationStatus status, Class<? extends Generator<? extends L>> leftGenClass, Generator<? extends R> rightGen) {
		return generate(random, status, instantiate(leftGenClass), rightGen);
	}
}
