
---

## 📦 Modules

| Module    | Responsibility                     |
|-----------|------------------------------------|
| `app`     | Compose UI + ViewModel             |
| `domain`  | UseCases + Repository interface    |
| `data`    | Repository implementation + Trie   |

---

## 📚 Libraries & Resources Used

### 🔷 Jetpack Compose
- `androidx.compose.ui`, `material3`, `navigation-compose`
- [Jetpack Compose Docs](https://developer.android.com/jetpack/compose)

### 🔶 Hilt (Dependency Injection)
- `dagger.hilt.android`
- [Hilt Guide](https://developer.android.com/training/dependency-injection/hilt-android)

### 🔸 Kotlin Coroutines & StateFlow
- `kotlinx.coroutines`
- [StateFlow in Compose](https://developer.android.com/jetpack/compose/state#state-in-viewmodels)

### 🔸 JSON Parsing
- `Gson` with `JsonReader` for memory-efficient streaming parsing
- [Gson Streaming Docs](https://github.com/google/gson/blob/main/UserGuide.md#streaming)

### 🟨 MVI Pattern
- Single `UiState`, `Events`, `SideEffects` for clear separation of concerns
- [MVI with Compose Guide (Google)](https://developer.android.com/jetpack/compose/architecture#state-holder)

---

## 🧠 Data Structure

### 🟩 Trie (Prefix Tree)

- Used for efficient search-as-you-type experience
- TrieNode recursively stores cities and allows prefix matching in `O(L)` time (L = length of query)
- Scales well with 10,000+ cities

---


https://github.com/user-attachments/assets/575c3038-3b1d-465b-8d8a-bdd21b6faa2d
