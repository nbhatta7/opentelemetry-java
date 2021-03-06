/*
 * Copyright 2020, OpenTelemetry Authors
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

package io.opentelemetry.sdk.metrics;

import io.opentelemetry.metrics.Measure;
import io.opentelemetry.sdk.metrics.common.InstrumentType;
import io.opentelemetry.sdk.metrics.view.Aggregations;

abstract class AbstractMeasure<B extends AbstractBoundInstrument>
    extends AbstractSynchronousInstrument<B> {
  private final boolean absolute;

  AbstractMeasure(
      InstrumentDescriptor descriptor,
      MeterProviderSharedState meterProviderSharedState,
      MeterSharedState meterSharedState,
      boolean absolute) {
    super(
        descriptor,
        meterProviderSharedState,
        meterSharedState,
        new ActiveBatcher(
            getDefaultBatcher(
                descriptor,
                meterProviderSharedState,
                meterSharedState,
                Aggregations.minMaxSumCount())));
    this.absolute = absolute;
  }

  final boolean isAbsolute() {
    return absolute;
  }

  abstract static class Builder<B extends AbstractMeasure.Builder<B>>
      extends AbstractInstrument.Builder<B> implements Measure.Builder {
    private boolean absolute = true;

    Builder(
        String name,
        MeterProviderSharedState meterProviderSharedState,
        MeterSharedState meterSharedState) {
      super(name, meterProviderSharedState, meterSharedState);
    }

    @Override
    public final B setAbsolute(boolean absolute) {
      this.absolute = absolute;
      return getThis();
    }

    final boolean isAbsolute() {
      return this.absolute;
    }
  }

  static InstrumentType getInstrumentType(boolean absolute) {
    return absolute ? InstrumentType.MEASURE_ABSOLUTE : InstrumentType.MEASURE_NON_ABSOLUTE;
  }
}
