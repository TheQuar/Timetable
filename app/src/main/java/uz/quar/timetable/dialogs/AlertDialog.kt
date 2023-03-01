package uz.quar.timetable.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import uz.quar.timetable.databinding.AlerDismissDialogLayoutBinding

class AlertDialog(
    context: Context,
    private val title: String,
    private val description: String,
    private val onConfirm: (() -> Unit)? = null,
    private val onDismiss: (() -> Unit)? = null,
) : Dialog(context) {

    private val binding = AlerDismissDialogLayoutBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            setCancelable(false)

            //set view configuration
            window?.let {
                with(it) {
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    setGravity(Gravity.CENTER)
                }
            }

            binding.apply {
                alertTitle.text = title
                alertDescription.text = description

                confirmBtn.setOnClickListener {
                    onConfirm?.let { it1 -> it1() }
                    dismiss()
                }

                cancelBtn.setOnClickListener {
                    onDismiss?.let { it1 -> it1() }
                    dismiss()
                }
            }
        }
    }


}