/*
 * Copyright © 2025 Cask Data, Inc.
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


package io.cdap.wrangler.steps.transform;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.RecipeParser;
import io.cdap.wrangler.api.RecipePipeline;
import io.cdap.wrangler.parser.GrammarBasedParser;
import io.cdap.wrangler.registry.CompositeDirectiveRegistry;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AggregateStatsTest {

    @Test
    public void testAggregateStatsDirective() throws Exception {
        List<Row> rows = Arrays.asList(
            new Row("data_transfer_size", "2MB").add("response_time", "200ms"),
            new Row("data_transfer_size", "512KB").add("response_time", "800ms"),
            new Row("data_transfer_size", "1.5MB").add("response_time", "1s")
        );

        String recipe = "aggregate-stats :data_transfer_size :response_time total_size_mb total_time_sec";

        CompositeDirectiveRegistry registry = new CompositeDirectiveRegistry();
        RecipeParser parser = new GrammarBasedParser("default", recipe, registry);
        RecipePipeline pipeline = new RecipePipeline(parser, registry);

        List<Row> results = pipeline.execute(rows);

        Assert.assertEquals(1, results.size());

        Row result = results.get(0);

        double expectedMB = (2 * 1024 * 1024 + 512 * 1024 + (long)(1.5 * 1024 * 1024)) / (1024.0 * 1024.0);
        double expectedSec = (200_000_000 + 800_000_000 + 1_000_000_000) / 1_000_000_000.0;

        Assert.assertEquals(expectedMB, (double) result.getValue("total_size_mb"), 0.001);
        Assert.assertEquals(expectedSec, (double) result.getValue("total_time_sec"), 0.001);
    }
}
