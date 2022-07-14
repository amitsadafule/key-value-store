# key-value-store

Welcome to key-value-store!!

## running

    ./gradlew joobyRun

## building

    ./gradlew build

## Napkin-math

###Model considerations

Operation | Latency | rps
---|---|---|
HTTP based server | 10 μs | 100K rps |
In memory write | 50ns | 20000K prs|
Sequential SSD write, +fsync  (8KiB)| 1 ms | 1000 rps|
Sequential SSD read (8 KiB) | 1 μs | 1000K rps |

As per above calculations for single threaded application, max write speed is ~1000 rps and max read speed is 100k rps
