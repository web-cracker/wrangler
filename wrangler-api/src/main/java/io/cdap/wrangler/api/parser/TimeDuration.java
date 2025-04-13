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
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

 package io.cdap.wrangler.api.parser;

 import com.google.gson.JsonElement;
 import com.google.gson.JsonPrimitive;
 
 import java.util.Locale;
 
 public class TimeDuration implements Token {
     private final String original;
     private final long nanoseconds;
 
     public TimeDuration(String value) {
         this.original = value;
         this.nanoseconds = parseDuration(value);
     }
 
     private long parseDuration(String input) {
         input = input.trim().toLowerCase(Locale.ENGLISH);
         double number;
         long multiplier;
 
         if (input.endsWith("ms")) {
             number = Double.parseDouble(input.replace("ms", ""));
             multiplier = 1_000_000;
         } else if (input.endsWith("s")) {
             number = Double.parseDouble(input.replace("s", ""));
             multiplier = 1_000_000_000;
         } else if (input.endsWith("m")) {
             number = Double.parseDouble(input.replace("m", ""));
             multiplier = 60L * 1_000_000_000;
         } else if (input.endsWith("h")) {
             number = Double.parseDouble(input.replace("h", ""));
             multiplier = 3600L * 1_000_000_000;
         } else if (input.endsWith("d")) {
             number = Double.parseDouble(input.replace("d", ""));
             multiplier = 86400L * 1_000_000_000;
         } else {
             throw new IllegalArgumentException("Unsupported time duration format: " + input);
         }
 
         return (long) (number * multiplier);
     }
 
     public long getNanoseconds() {
         return nanoseconds;
     }
 
     @Override
     public TokenType type() {
         return TokenType.TIME_DURATION;
     }
 
     @Override
     public String value() {
         return original;
     }
 
     @Override
     public JsonElement toJson() {
         return new JsonPrimitive(original);
     }
 }
 
 