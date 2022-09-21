# Description
RoundRobin simulator made in Java + Swing, using MVC pattern.

# Features
* Configurable clock: automatic clock with period between 100 ms and 5000 ms, or manual clock with command-triggered cycles.
* Process creation: they can be added to the list of active processes with their name and execution time in quantum units.
* Automatic listing of active processes and updating of remaining execution time: every time the clock ticks.
* Calculation if mean return time and mean waiting time.

# Usage
After compiling, execute from console:

```sh
java RoundRobin <quantum> [<clock period>]
```

Where `<quantum>` must be an integer greater than cero, and `<clock period>` must be an integer between 100 and 5000.

If `<clock period>` is omitted, manual clock is used.

# App view examples
Initial window using automatic clock:
<p align="center">
  <img src="https://raw.githubusercontent.com/facundolaffont/round-robin-simulator/main/img/autoClockInitView.png"/>
</p>

Initial window using manual clock:
<p align="center">
  <img src="https://raw.githubusercontent.com/facundolaffont/round-robin-simulator/main/img/manualClockInitView.png"/>
</p>

Processes listing and mean times:
<p align="center">
  <img src="https://raw.githubusercontent.com/facundolaffont/round-robin-simulator/main/img/processesAndMeanTimes.png"/>
</p>

# Next
+ Add icon.
