package com.example.nasapictureoftheday.ui.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.nasapictureoftheday.MainActivity
import com.example.nasapictureoftheday.model.MyApplication
import com.example.nasapictureoftheday.model.PictureOfTheDayData
import com.example.nasapictureoftheday.viewmodel.PictureOfTheDayViewModel
import com.example.nasapictureoftheday.R
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.picture_of_the_day_fragment.*
import kotlinx.android.synthetic.main.picture_of_the_day_fragment.chipGroup
import kotlinx.android.synthetic.main.settings_fragment.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "@@@ PictureOfTheDayFragment"

class PictureOfTheDayFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var viewModel: PictureOfTheDayViewModel

    private val app by lazy { context?.applicationContext as MyApplication }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
        private var isMain = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d(TAG,
            "onCreateView() called with: inflater = $inflater, container = $container, savedInstanceState = $savedInstanceState")
        viewModel = ViewModelProvider(this)[PictureOfTheDayViewModel::class.java]
        return inflater.inflate(R.layout.picture_of_the_day_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG,
            "onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setBottomSheetBehavior(bottom_sheet_container)
        setBottomAppBar(view)
        chipChoseLst()

        input_layout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://ru.wikipedia.org/wiki/${input_edit_text.text.toString()}")
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun chipChoseLst() {
        Log.d(TAG, "chipChoseLst() called")
        val myDateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val today = myDateFormat.format(Calendar.getInstance().time)
        val yesterday = myDateFormat.format(Calendar.getInstance().run {
            add(Calendar.DAY_OF_MONTH, -1)
            time
        })
        val dayBeforeYesterday = myDateFormat.format(Calendar.getInstance().run {
            add(Calendar.DAY_OF_MONTH, -2)
            time
        })
        chipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                when (position) {
                    chip1_from_chip_group.id -> {
                        viewModel.getPictureOfTheDay(app.retrofit, dayBeforeYesterday)
                    }
                    chip2_from_chip_group.id -> {
                        viewModel.getPictureOfTheDay(app.retrofit, yesterday)
                    }
                    chip3_from_chip_group.id -> {
                        viewModel.getPictureOfTheDay(app.retrofit, today)
                    }
                }
            }
        }
    }

    private fun initViewModel() {
        Log.d(TAG, "initViewModel() called")
        viewModel.getPictureOfTheDay(app.retrofit, "")
            .observe(viewLifecycleOwner) { renderData(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d(TAG, "onCreateOptionsMenu() called with: menu = $menu, inflater = $inflater")
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected() called with: item = $item")
        when (item.itemId) {
            R.id.app_bar_fav -> Toast.makeText(
                context, "Favourite",
                Toast.LENGTH_SHORT
            ).show()
            R.id.app_bar_search -> Toast.makeText(
                context, "Search",
                Toast.LENGTH_SHORT
            ).show()
            R.id.app_bar_settings -> activity?.supportFragmentManager
                ?.beginTransaction()
                ?.add(R.id.container, SettingsFragment())
                ?.addToBackStack(null)
                ?.commit()
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar(view: View) {
        Log.d(TAG, "setBottomAppBar() called with: view = $view")
        val context = activity as MainActivity
        context.setSupportActionBar(bottom_app_bar)
        setHasOptionsMenu(true)

        fab.setOnClickListener {
            if (isMain) {
                isMain = false
                bottom_app_bar.navigationIcon = null
                bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                fab.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_outline_arrow_back_24)
                )
                bottom_app_bar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                bottom_app_bar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_baseline_menu)
                bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_baseline_add_24
                    )
                )
                bottom_app_bar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        Log.d(TAG, "setBottomSheetBehavior() called with: bottomSheet = $bottomSheet")
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun renderData(data: PictureOfTheDayData) {
        Log.d(TAG, "renderData() called with: data = $data")
        when (data) {
            is PictureOfTheDayData.Success -> {
                progress_bar.visibility = View.GONE
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    Toast.makeText(context, "Нет ссылки на картинку!", Toast.LENGTH_SHORT).show()
                } else {
                    bottom_sheet_description_header.text = serverResponseData.title
                    bottom_sheet_description.text = serverResponseData.explanation
                    image_view.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                }
            }
            is PictureOfTheDayData.Loading -> {
                progress_bar.visibility = View.VISIBLE
            }
            is PictureOfTheDayData.Error -> {
                progress_bar.visibility = View.GONE
                view?.let {
                    Snackbar.make(it, "Ошибка! ${data.error.message}", Snackbar.LENGTH_INDEFINITE)
                        .setAction(
                            "Перезагрузить"
                        ) { initViewModel() }.show()
                }
            }
        }
    }
}