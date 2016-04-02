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

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.java.lang.IntegerGenerator;
import com.pholser.junit.quickcheck.generator.java.lang.StringGenerator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import com.diffplug.common.base.Either;
import com.diffplug.common.base.MoreCollectors;

public class Transaction {
	private Either<String, Integer> id;
	private String details;
	private int amount;

	@Override
	public String toString() {
		return id.fold(str -> str, i -> Integer.toString(i)) + " $" + amount + " " + details;
	}

	public static class TransactionGenerator extends Generator<Transaction> {
		public TransactionGenerator() {
			super(Transaction.class);
		}

		@Override
		public Transaction generate(SourceOfRandomness random, GenerationStatus status) {
			StringGenerator stringGenNoControlCodes = new StringGenerator() {
				@Override
				public String generate(SourceOfRandomness random, GenerationStatus status) {
					String result = super.generate(random, status);
					return MoreCollectors.codePointsToString(result.codePoints().filter(i -> i > 0x7f));
				}
			};
			Transaction transaction = new Transaction();
			transaction.id = EitherGenerator.generate(random, status, stringGenNoControlCodes, IntegerGenerator.class);
			transaction.amount = random.nextInt();
			transaction.details = stringGenNoControlCodes.generate(random, status);
			return transaction;
		}
	}
}
