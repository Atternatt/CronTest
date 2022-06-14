# CronTest
This project contains a simple implementation of a Cronjob calculator that, given an input that contains a template 
with the info of the time where the job is going to be run and a specific time (normally th current time), it tells you
when is the next time when the job is going to be run.

as examples, given `16:10` as `Current Time`: 
- `30 1 /bin/run_me_daily` to `1:30 tomorrow - /bin/run_me_daily`
- `45 * /bin/run_me_hourly` to `16:45 today - /bin/run_me_hourly`
- `* * /bin/run_me_every_minute` to `16:10 today - /bin/run_me_every_minute`
- `* 19 /bin/run_me_sixty_times` to `19:00 today - /bin/run_me_sixty_times`
- `30 17 /bin/run_me_daily` to `17:30 today - /bin/run_me_daily`
- `9 * /bin/run_me_hourly` to `17:09 today - /bin/run_me_hourly`
- `* 15 /bin/run_me_sixty_times` to `15:00 tomorrow - /bin/run_me_sixty_times`

# Structure
As this is a simple exercise we created a super lightwaight architecture where all the features have been segregated 
in usecase inside a module package called: `com.m2f.cronAnalyzer.module` 
In a real world scenariop this would be an independent module provided to the program and isolated completely but for simplicity we put
everything in a same module divided by packages.

## Module
The module package contains all the business logic and the structure is as follows:

![CronAnalyzer](https://user-images.githubusercontent.com/2378636/173332632-3a65bd44-f618-4adc-ae0c-0b53da9d8386.png)


### Model
Contains all the related domain objects for the program like:
- `CronJob`
- `Time`
- `Command`

### DI
Exposes a `withDependencies` DSL that exposes the needed usecases as implicit receivers for the execution of the program

### UseCase
Contains all the usecases for the correct implementation fo the feature:
- CalculateNextTimeUseCase.kt
- GetInputFromConsolePipeUseCase.kt
- ParseCommandUseCase.kt
- ParseCronJobUseCase.kt
- ParseTimeUseCase.kt

It also contains the domain errors that all usecase can throw

### Utils
It contains a Result object. Representation of a response for a usecase that can be either `Sucess` or `Failure`

> If you see in the code explicit casts like `as Sucess` or `as Failure` is because the implementation of the `kotlin contracts` is still experimental and some times does not automatically casts to the specific type.

## Testing
There are tests for:
- CalculateNextTimeUseCaseTest
- ParseCommandUseCaseTest
- ParseTimeUseCaseTest

# Executable
[cron.jar.zip](https://github.com/Atternatt/CronTest/files/8890211/cron.jar.zip)

download the file, and unzip it.
To execute the jar file go to the comand line and tipe:

cat <youtTestSuit.txt> | java -jar cron.jar `currentTime`
  
As an example:
`cat commands.txt | java -jar cron.jar 16:10`
