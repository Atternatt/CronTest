import com.m2f.cronAnalyzer.module.di.withDependencies
import com.m2f.cronAnalyzer.module.usecase.failure.InvalidArgument
import com.m2f.cronAnalyzer.module.usecase.failure.InvalidFormat
import com.m2f.cronAnalyzer.module.utils.Failure
import com.m2f.cronAnalyzer.module.utils.Success
import com.m2f.cronAnalyzer.module.utils.isFailure
import com.m2f.cronAnalyzer.module.utils.isSucess
import kotlin.contracts.ExperimentalContracts


@OptIn(ExperimentalContracts::class)
fun main(args: Array<String>) {
    withDependencies {
        val currentTime = args[0]
        withInput { command ->
            val cronJob = parseCronJob(command)
            if (cronJob.isSucess()) {
                val result = calculate((cronJob as Success).value, currentTime)
                when {
                    result.isSucess() -> println(result.value)
                    result.isFailure() -> println("The input current time is not valid")
                }
            } else {
                when ((cronJob as Failure).value) {
                    InvalidArgument -> println("There are some invalid arguments")
                    InvalidFormat -> println("The cron job has an invalid format")
                }
            }
        }
    }
}