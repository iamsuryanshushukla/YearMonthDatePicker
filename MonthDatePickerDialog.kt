    @SuppressLint("SetTextI18n", "DefaultLocale")
    fun yearMonthDateCalendar() {

       
            // Inflate the custom layout with DataBindingUtil
            val dialog = Dialog(baseActivity)
            val dialogBinding =
                MonthDatePickerDialogBinding.inflate(LayoutInflater.from(context))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

            dialog.setContentView(dialogBinding.root)
            dialogBinding.btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            // Configure NumberPickers
            val currentYear = viewModel.year.value ?: calendarStart.get(Calendar.YEAR)
            val currentMonth = viewModel.month.value
                ?: calendarStart.get(Calendar.MONTH) // Months are 0-indexed
            val currentDay = viewModel.day.value ?: calendarStart.get(Calendar.DAY_OF_MONTH)

            dialogBinding.yearPicker.apply {
                minValue = currentYear-10
                maxValue = currentYear
                value = currentYear
                wrapSelectorWheel = false
            }

            dialogBinding.monthPicker.apply {
                minValue = 0
                maxValue = 11
                value = currentMonth
                displayedValues = arrayOf(
                    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
                )
                wrapSelectorWheel = true
            }

            dialogBinding.datePicker.apply {
                val maxDays = calendarStart.getActualMaximum(Calendar.DAY_OF_MONTH)
                minValue = 1
                maxValue = maxDays
                value = currentDay
                wrapSelectorWheel = true
            }

            // Add listeners to update the day picker based on year and month changes
            val updateDays = {
                val selectedYear = dialogBinding.yearPicker.value
                val selectedMonth = dialogBinding.monthPicker.value

                // Update calendar instance
                calendarStart.set(Calendar.YEAR, selectedYear)
                calendarStart.set(Calendar.MONTH, selectedMonth)

                // Get max days for the updated month and year
                val maxDays = calendarStart.getActualMaximum(Calendar.DAY_OF_MONTH)
                dialogBinding.datePicker.apply {
                    maxValue = maxDays
                    if (value > maxDays) {
                        value = maxDays // Adjust if current day exceeds max days
                    }
                }
            }

            dialogBinding.yearPicker.setOnValueChangedListener { _, _, _ -> updateDays() }
            dialogBinding.monthPicker.setOnValueChangedListener { _, _, _ -> updateDays() }

            dialogBinding.btnSubmit.setOnClickListener {
                val selectedYear = dialogBinding.yearPicker.value
                val selectedMonth = dialogBinding.monthPicker.value + 1 // Convert to 1-indexed
                val selectedDay = dialogBinding.datePicker.value

                // Format the date as MM/dd/yyyy
                val formattedDate = String.format(
                    "%02d/%02d/%d",
                    selectedMonth,
                    selectedDay,
                    selectedYear
                )

                // Update ViewModel if you are not using viewmodel please remove it and just try to print in log.
                viewModel.year.value = selectedYear
                viewModel.month.value = selectedMonth - 1 // Convert back to 0-indexed
//                viewModel.day.value = selectedDay
//                viewModel.dateString.value = formattedDate

                dialog.dismiss()
            }

            dialog.show()
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialog.window!!.setGravity(Gravity.BOTTOM)
        
    }
