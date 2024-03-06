# Parallel Barnes-Hut algorithm implementation in Java

For gravitational simulation of `N` bodies in 2D space.

Barnes-Hut algorithm takes $O(n*log n)$ compared to a direct-sum algorithm which would be $O(n^2)$.

## Input 
The input is a text file where
1. The first line contains the number `N` of bodies
2. The 2nd line contains the size `R` of the universe (the coordinates are from `-R` to `+R`)
3. following `N` lines with 5 numbers and a name per line: `X`, `Y`, `Vx`, `Vy`, `M`, `S`
    - `X` and `Y` are the coordinates of the body in the plane
    - `Vx` and `Vy` are the velocity components in the `x` and `y` axes
    - `M` is the mass, and `S` is the name of the body.

## How to run
```bash
cd src; 
java Main <input-file> <generations_num> <threads_num> <out-file>
# clean
rm -rf src/BHTree/*.class src/Main.class
```

## Speedup on 4-core Intel machine
* serial implementation vs parallel with 1 thread have the same performance!
* Full run results below


## Sample Runs
|          *run*         |*serial* | *1 thread* | *2 threads* | *4 threads* |
|:-----------------------| -------:| ----------:|------------:|-----------: |
| input4---50----run:1   |   11.85 |      12.31 |        6.47 |       3.43  |
| input4---50----run:2   |   12.41 |      11.92 |        6.26 |       3.45  |
| input4---50----run:3   |   12.30 |      12.34 |        6.41 |       3.55  |
| input4---50----run:4   |   12.02 |      12.12 |        6.38 |       3.53  |
| input4---50----run:5   |   11.95 |      11.90 |        6.48 |       3.55  |
| **Average time (s)**   |   `12.1`|     `12.1` |        `6.4`|       `3.5` |
| **Standard Deviation** |   `0.24`|     `0.21` |       `0.09`|      `0.06` |
| **Speedup**            |         |     `1.00` |       `1.89`|       `3.46`|
| input5----50----run:1  |    11.3 |       11.3 |        5.97 |        3.60 |
| input5----50----run:2  |    10.9 |       11.4 |        6.08 |        3.13 |
| input5----50----run:3  |    11.2 |       11.1 |        6.01 |        3.53 |
| input5----50----run:4  |    10.9 |       11.0 |        6.16 |        3.53 |
| input5----50----run:5  |    11.4 |       10.9 |        6.03 |        3.43 |
| **Average time (s)**   |   `11.2`|     `11.2` |        `6.1`|        `3.5`|
| **Standard Deviation** |   `0.22`|     `0.19` |       `0.07`|       `0.17`|
| **Speedup**            |         |     `1.00` |       `1.84`|       `3.23`|

