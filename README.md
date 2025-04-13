# Wrangler Assignment - CDAP Wrangler Parser & Directive Enhancement

## 🚀 Overview

This project enhances the **CDAP Wrangler** tool by adding custom parsers and a new directive:

- ✅ Byte Size Parser (`10KB`, `1.5MB`, `2GB`, etc.)
- ✅ Time Duration Parser (`100ms`, `3s`, `2h`, etc.)
- ✅ `aggregate-stats` directive to compute total size and time

---

## 📂 What Was Added

### 1. ByteSize Token
- **Location**: `wrangler-api/src/main/java/io/cdap/wrangler/api/parser/ByteSize.java`
- **Function**: Parses strings like `512KB`, `1MB` to canonical `bytes`.

### 2. TimeDuration Token
- **Location**: `wrangler-api/src/main/java/io/cdap/wrangler/api/parser/TimeDuration.java`
- **Function**: Parses strings like `200ms`, `1s`, `5min` to canonical `nanoseconds`.

### 3. TokenType Update
- **Location**: `TokenType.java`
- **Update**: Added `BYTE_SIZE` and `TIME_DURATION` enum constants.

### 4. Grammar Parser Update
- **Location**: `GrammarBasedParser.java`
- **Methods Added**:
  ```java
  public Object visitByteSizeArg(...) { return new ByteSize(...); }
  public Object visitTimeDurationArg(...) { return new TimeDuration(...); }
  ```

### 5. New Directive: `aggregate-stats`
- **Location**: `wrangler-core/src/main/java/io/cdap/wrangler/directory/AggregateStats.java`
- **Function**:
  - Reads source byte/time columns
  - Accumulates totals
  - Outputs size in MB, time in seconds

---

## 🧪 Unit Testing

- **Location**: `AggregateStatsTest.java`
- **Test Cases**:
  - Byte & time unit parsing
  - Full `aggregate-stats` flow
  - Result validation against expected values

```java
Assert.assertEquals(expectedMB, (double) result.getValue("total_size_mb"), 0.001);
Assert.assertEquals(expectedSec, (double) result.getValue("total_time_sec"), 0.001);
```

---

## 📄 Sample Recipe

```text
aggregate-stats :data_transfer_size :response_time total_size_mb total_time_sec
```

---

## 🧠 Prompts Used

Tracked in `prompts.txt` as required:

```
Prompt: Help me to do this step by step process to complete entire assignment
Prompt: Explain clearly for the step 3 dont skip any give me all what to do
Prompt: Now explain me clearly from step 4
Prompt: modify the code
Prompt: give me final code without any single error forget the dependencies it is not working for this give me the final code
```

---

## 🧹 Final Checklist

- [x] ByteSize Token
- [x] TimeDuration Token
- [x] TokenType enum updated
- [x] Grammar parser support
- [x] New directive: `aggregate-stats`
- [x] Unit tested ✅
- [x] Maven build passed ✅
- [x] README and prompts.txt added

---

## 💬 Author

**Mohammed Zuber Ahamad**  
Full Stack Dev & IoT Innovator — Techzipe  
🔥 Built with precision. Tested like a pro.