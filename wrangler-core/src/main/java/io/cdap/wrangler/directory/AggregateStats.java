/*
 * Copyright © 2025 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */


package io.cdap.wrangler.directory;

import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.UsageDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * A directive that aggregates byte size and time duration values.
 */
public class AggregateStats implements Directive {
    private String byteSizeColumn;
    private String timeDurationColumn;
    private String outputByteSizeColumn;
    private String outputTimeDurationColumn;

    private long totalBytes = 0L;
    private long totalNanos = 0L;
    private int rowCount = 0;

    @Override
    public UsageDefinition define() {
        UsageDefinition.Builder builder = UsageDefinition.builder("aggregate-stats");
        builder.define("byteSizeColumn", TokenType.COLUMN_NAME);
        builder.define("timeDurationColumn", TokenType.COLUMN_NAME);
        builder.define("outputByteSizeColumn", TokenType.TEXT);
        builder.define("outputTimeDurationColumn", TokenType.TEXT);
        return builder.build();
        
    }

    @Override
    public void initialize(Arguments arguments) {
        byteSizeColumn = arguments.value("byteSizeColumn");
        timeDurationColumn = arguments.value("timeDurationColumn");
        outputByteSizeColumn = arguments.value("outputByteSizeColumn");
        outputTimeDurationColumn = arguments.value("outputTimeDurationColumn");
    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) {
        for (Row row : rows) {
            Object byteSizeVal = row.getValue(byteSizeColumn);
            Object timeVal = row.getValue(timeDurationColumn);

            if (byteSizeVal instanceof String) {
                ByteSize bs = new ByteSize((String) byteSizeVal);
                totalBytes += bs.getBytes();
            }

            if (timeVal instanceof String) {
                TimeDuration td = new TimeDuration((String) timeVal);
                totalNanos += td.getNanoseconds();
            }

            rowCount++;
        }

        List<Row> results = new ArrayList<>();
        Row resultRow = new Row();
        resultRow.add(outputByteSizeColumn, totalBytes / (1024.0 * 1024.0)); // MB
        resultRow.add(outputTimeDurationColumn, totalNanos / 1_000_000_000.0); // seconds
        results.add(resultRow);
        return results;
    }

    @Override
    public void destroy() {
        // nothing to cleanup
    }
}
