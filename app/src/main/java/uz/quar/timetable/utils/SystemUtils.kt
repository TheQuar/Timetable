package uz.quar.timetable.utils

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import uz.quar.timetable.dialogs.AlertDialog
import uz.quar.timetable.dialogs.LoaderDialog


private var loaderDialog: LoaderDialog? = null

fun <A : Activity> Activity.openNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        startActivity(it)
    }
}

fun Activity.loaderDialog(boolean: Boolean) {
    if (loaderDialog == null) loaderDialog = LoaderDialog(this)

    if (boolean) loaderDialog?.show() else {
        loaderDialog?.dismiss()
        loaderDialog = null
    }
}

fun Activity.showDialog(
    title: String,
    description: String,
    onConfirm: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
) {
    AlertDialog(this, title, description, onConfirm, onDismiss).show()
}



fun Fragment.activityResult(event: (activityResult: ActivityResult?) -> Unit) =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        event.invoke(it)
    }



