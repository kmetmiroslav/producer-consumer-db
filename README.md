# Producer–Consumer (Java 21) — Virtual Threads + Executor + Bounded FIFO

Notes:
- using **Java 21** and **virtual threads** with **ExecutorService**
- Bounded FIFO queue backed by `ArrayDeque`
- One handler per command + dispatcher registry
- As the assignment specified focus on concurrency I made this version to handle synchronization in the code (easier version would be to use just **BlockingQueue** )

## Run tests
```bash
mvn test
```

## Run demo
Run `com.example.pcdb.DemoApp`.
