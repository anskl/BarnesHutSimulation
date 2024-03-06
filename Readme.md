# Parallel Barnes-Hut algorithm

For gravitational simulation of `N` bodies in 2D space.

Barnes-Hut algorithm takes $O(n log n)$ compared to a direct-sum algorithm which would be $O(n^2)$.

## Input 
The input is a text file which
1. in the first line contains the number `N` of bodies
2. in the 2nd line the size `R` of the universe (the coordinates are be from `-R` to `+R`)
3. following `N` lines with 5 numbers and a name per line: `X`, `Y`, `Vx`, `Vy`, `M`, `S`
    - `X` and `Y` are the coordinates of the body in the plane
    - `Vx` and `Vy` are the velocity components in the `x` and `y` axes
    - `M` is the mass, and `S` is the name of the body.

## Output
