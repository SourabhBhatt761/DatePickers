package com.example.datepickers

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.core.util.Pair
import com.example.datepickers.databinding.ActivityMainBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.N)
class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener{

    private lateinit var _binding: ActivityMainBinding
    private val binding get() = _binding
    private lateinit var datePicker: DatePicker
    private lateinit var date: Date
    private val monthList = listOf(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        datePicker = DatePicker(this)
        date = Date()


        clickListeners();

        simpleDateFormattingFunction()

    }

    private fun simpleDateFormattingFunction() {

        val sdf = SimpleDateFormat("yyyy / MM / dd", Locale.getDefault())
        val formattedDate = sdf.format(Date())

        binding.dateTv.text = formattedDate


    }

    private fun clickListeners() {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm ", Locale.getDefault())


        binding.datePicker1.setOnClickListener {
            Log.i("uni", "day of month" + datePicker.dayOfMonth)
            Log.i("uni", "first day of week" + datePicker.firstDayOfWeek)
            Log.i("uni", " month" + datePicker.month)
            Log.i("uni", " maxDate" + datePicker.maxDate)
            Log.i("uni", " minDate" + datePicker.minDate)
            Log.i("uni", " date.time " + date.time)
            Log.i("uni", " calendar.time " + Calendar.getInstance().time)
            Log.i("uni", " calendar.time " + MaterialDatePicker.todayInUtcMilliseconds())
            Log.i("uni", " calendar.time in milli " + Calendar.getInstance().timeInMillis)


            /*convert string to date*/
            val stringDate = "23/2/2022 10:30 "
            val dateToPrint = sdf.parse(stringDate)!!
            binding.dateTv.text = dateToPrint.toString()

            /*convert date to string*/
            val sdf2 = SimpleDateFormat("dd MMMM yyyy hh:mm:ss aa", Locale.getDefault())

            val handler = Handler(mainLooper)
            handler.postDelayed({
                binding.dateTv.text = sdf2.format(dateToPrint)
            }, 3000)


        }

        binding.datePicker2.setOnClickListener {
            DatePickerDialog(
                this,
                this,
                datePicker.year,
                datePicker.month,
                datePicker.dayOfMonth
            ).show()
        }


        binding.materialDatePicker1.setOnClickListener {

            //build the date picker
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds())

            //display the picker
            val picker = builder.build()
            picker.show(supportFragmentManager, "testing")

        }

        binding.materialDatePicker2.setOnClickListener {

            //build the date picker
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setSelection(Calendar.getInstance().timeInMillis)
            restrictUserToClickOnDates(builder)

            //display the picker
            val picker = builder.build()
            picker.show(supportFragmentManager, "testing")


            //after clicking ok btn
            picker.addOnPositiveButtonClickListener {
                binding.dateTv.text = sdf.format(it)
                Log.i("uni","picker.selection after clicking ok ${picker.selection}")
                Log.i("uni","picker.selection after clicking ok ${sdf.format(picker.selection)}")

            }

            picker.addOnCancelListener {
                Log.i("uni","picker cancelled")

            }

            picker.addOnDismissListener {
                Log.i("uni","picker dismissed")
            }

            picker.addOnNegativeButtonClickListener {
                Log.i("uni","picker negative btn clicked")
            }


        }

        binding.materialRangePicker.setOnClickListener {

            //build the date picker
            val builder = MaterialDatePicker.Builder.dateRangePicker()
            builder.setSelection(Pair(MaterialDatePicker.thisMonthInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds()))

            restrictUserToClickOnDatesRangePicker(builder)
            val picker = builder.build()

            picker.isCancelable = false                 //sticky
            picker.show(supportFragmentManager,"testing")


            picker.addOnPositiveButtonClickListener {
                val date = it
                val startDate = date.first
                val endDate = date.second

                binding.dateTv.text = "start date: ${sdf.format(startDate)}   \nend date: ${sdf.format(endDate)}"
            }

        }


        binding.materialDatePicker3.setOnClickListener {

            //build the date picker
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setSelection(getBirthdayDate())

            val picker = builder.build()
            picker.show(supportFragmentManager,"testing")

        }


        binding.materialDatePicker4.setOnClickListener {

            //build the date picker
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            builder.setTheme(R.style.ThemeOverlay_App_MaterialDatePicker)

            val picker = builder.build()
            picker.show(supportFragmentManager,"testing")

        }


    }

    private fun getBirthdayDate(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(1999,4,5)
        calendar.firstDayOfWeek = Calendar.WEDNESDAY
        return  calendar.timeInMillis

    }

    private fun restrictUserToClickOnDatesRangePicker(builder: MaterialDatePicker.Builder<Pair<Long, Long>>) {
        val constraintsBuilder = CalendarConstraints.Builder()
        constraintsBuilder.setValidator(WeekDayValidator(MaterialDatePicker.todayInUtcMilliseconds()))
        builder.setCalendarConstraints(constraintsBuilder.build())

    }

    private fun restrictUserToClickOnDates(builder: MaterialDatePicker.Builder<Long>) {
        val constraintsBuilder = CalendarConstraints.Builder()
        val calendar = Calendar.getInstance()
//        constraintsBuilder.setStart(calendar.timeInMillis)
//        calendar.roll(Calendar.MONTH, 4)                        //user can swipe till next 4 months
//        calendar.roll(Calendar.YEAR, 1)                        //user can swipe till next 1 year
//        constraintsBuilder.setValidator(DateValidatorPointForward.now())  //best approach to block user to select previous dates
//        constraintsBuilder.setValidator(DateValidatorPointBackward.now())  //best approach to block user to select coming dates
        constraintsBuilder.setValidator(WeekDayValidator(calendar.timeInMillis))         //CUSTOM CLASS TO RESTRICT USER !
//        constraintsBuilder.setEnd(calendar.timeInMillis)
        builder.setCalendarConstraints(constraintsBuilder.build())
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {

        Log.i("uni", "p1 $p1")
        Log.i("uni", "p2 $p2")
        Log.i("uni", "p3 $p3")

        binding.dateTv.text = "$p3  ${monthList[p2]}  $p1"

    }

}