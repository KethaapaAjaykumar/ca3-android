package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
enum class WorkoutStatus {
    NOT_STARTED, IN_PROGRESS, PAUSED, COMPLETED
}
data class WorkoutState(
    val status: WorkoutStatus = WorkoutStatus.NOT_STARTED,
    val exerciseName: String = "Push-ups",
    val repetitions: Int = 0
)
class WorkoutViewModel : ViewModel() {
    var state by mutableStateOf(WorkoutState())
        private set
    fun startWorkout() {
        state = state.copy(status = WorkoutStatus.IN_PROGRESS, repetitions = 0)
    }

    fun pauseWorkout() {
        if (state.status == WorkoutStatus.IN_PROGRESS) {
            state = state.copy(status = WorkoutStatus.PAUSED)
        }
    }
    fun resumeWorkout() {
        if (state.status == WorkoutStatus.PAUSED) {
            state = state.copy(status = WorkoutStatus.IN_PROGRESS)
        }
    }
    fun completeWorkout() {
        state = state.copy(status = WorkoutStatus.COMPLETED)
    }

    // Function to simulate performing an exercise
    fun performRepetition() {
        if (state.status == WorkoutStatus.IN_PROGRESS) {
            state = state.copy(repetitions = state.repetitions + 1)
        }
    }
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WorkoutScreen()
                }
            }
        }
    }
}
@Composable
fun WorkoutScreen(viewModel: WorkoutViewModel = viewModel()) {
    val state = viewModel.state
    WorkoutContent(
        state = state,
        onStart = { viewModel.startWorkout() },
        onPause = { viewModel.pauseWorkout() },
        onResume = { viewModel.resumeWorkout() },
        onComplete = { viewModel.completeWorkout() },
        onAddRep = { viewModel.performRepetition() }
    )
}
@Composable
fun WorkoutContent(
    state: WorkoutState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onComplete: () -> Unit,
    onAddRep: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = state.exerciseName,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Status", style = MaterialTheme.typography.labelLarge)
                Text(
                    text = state.status.name.replace("_", " "),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                Text(text = "Repetitions", style = MaterialTheme.typography.labelLarge)
                Text(
                    text = "${state.repetitions}",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Control Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onStart,
                enabled = state.status == WorkoutStatus.NOT_STARTED || state.status == WorkoutStatus.COMPLETED
            ) {
                Text("Start")
            }

            Button(
                onClick = onPause,
                enabled = state.status == WorkoutStatus.IN_PROGRESS
            ) {
                Text("Pause")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onResume,
                enabled = state.status == WorkoutStatus.PAUSED
            ) {
                Text("Resume")
            }
            Button(
                onClick = onComplete,
                enabled = state.status == WorkoutStatus.IN_PROGRESS || state.status == WorkoutStatus.PAUSED
            ) {
                Text("Complete")
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        if (state.status == WorkoutStatus.IN_PROGRESS) {
            LargeFloatingActionButton(
                onClick = onAddRep,
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Text("+1 Rep", fontWeight = FontWeight.Bold)
            }
        }
    }
}
