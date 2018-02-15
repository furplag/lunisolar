/**
 * Copyright (C) 2017+ furplag (https://github.com/furplag)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.furplag.time.lunisolar.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Streamr<T> {

  public static final <E> Streamr<E> of(E[] elements) {
    return new Streamr<E>(streamify(elements)) {};
  }

  public static final <E> Streamr<E> of(Collection<E> collection) {
    return new Streamr<E>(streamify(collection)) {};
  }

  public static final <T> Streamr<T> with(Stream<T> stream) {
    return new Streamr<T>(stream) {};
  }

  /**
   * do less coding in case of {@link Collection#stream()} .
   *
   * @param collection
   * @return
   */
  public static <T> Stream<T> streamify(final Collection<T> collection) {
    return collection == null ? Stream.empty() : collection.stream();
  }

  @SafeVarargs
  public static <T> Stream<T> streamify(final T... elements) {
    return elements == null ? Stream.empty() : Arrays.stream(elements);
  }

  private Stream<T> stream;

  private Stream<Predicate<T>> filter;

  private Stream<UnaryOperator<T>> modifier;

  private Streamr(Stream<T> stream) {
    this.stream = Optional.ofNullable(stream).orElse(Stream.empty());
    filter = Stream.empty();
    modifier = Stream.empty();
  }

  @SafeVarargs
  public final Streamr<T> filter(Predicate<T>... predicates) {
    filter = streamify(predicates);

    return this;
  }

  public final Streamr<T> filtering() {
    List<Predicate<T>> predicates = new ArrayList<>();
    filter.forEach(f -> {stream = (stream.filter(f)); predicates.add(f);});
    filter = streamify(predicates);

    return this;
  }

  public T first(Comparator<T> comparator) {
    return (comparator != null ? stream.sorted(comparator) : stream).findFirst().orElse(null);
  }

  public T last(Comparator<T> comparator) {
    return (comparator != null ? stream.sorted(comparator.reversed()) : stream).findFirst().orElse(null);
  }

  @SafeVarargs
  public final Streamr<T> modifier(UnaryOperator<T>... operators) {
    modifier = streamify(operators);

    return this;
  }

  public final Streamr<T> modify() {
    List<UnaryOperator<T>> operators = new ArrayList<>();
    modifier.forEach(f -> {stream = (stream.map(f)); operators.add(f);});
    modifier = streamify(operators);

    return this;
  }

  public final <C extends Collection<T>> C toCollection(Supplier<C> collectionFactory) {
    return stream.collect(Collectors.toCollection(collectionFactory));
  }

  protected final Stream<T> get() {
    return stream;
  }

  protected final void set(Stream<T> stream) {
    this.stream = stream;
  }
}
