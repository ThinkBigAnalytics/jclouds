/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.googlecomputeengine.functions.internal;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.tryFind;

import java.util.Iterator;

import org.jclouds.googlecomputeengine.domain.ListPage;
import org.jclouds.googlecomputeengine.options.ListOptions;
import org.jclouds.http.HttpRequest;
import org.jclouds.rest.InvocationContext;
import org.jclouds.rest.internal.GeneratedHttpRequest;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Iterators;

@Beta
public abstract class BaseToIteratorOfListPage<T, I extends BaseToIteratorOfListPage<T, I>>
      implements Function<ListPage<T>, Iterator<ListPage<T>>>, InvocationContext<I> {

   private GeneratedHttpRequest request;

   @Override
   public Iterator<ListPage<T>> apply(ListPage<T> input) {
      if (input.nextPageToken() == null) {
         return input.isEmpty() ? Iterators.<ListPage<T>>emptyIterator() : Iterators.singletonIterator(input);
      }

      Optional<Object> listOptions = tryFind(request.getInvocation().getArgs(), instanceOf(ListOptions.class));

      return new AdvancingIterator<T>(input,
            fetchNextPage((String) request.getCaller().get().getArgs().get(0), (ListOptions) listOptions.orNull()));
   }

   protected abstract Function<String, ListPage<T>> fetchNextPage(String projectName, ListOptions listOptions);

   @SuppressWarnings("unchecked")
   @Override
   public I setContext(HttpRequest request) {
      this.request = GeneratedHttpRequest.class.cast(request);
      return (I) this;
   }
}
