package com.example.nasapictureoftheday.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.picture_of_the_day_fragment.*

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
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[PictureOfTheDayViewModel::class.java]
        return inflater.inflate(R.layout.picture_of_the_day_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setBottomSheetBehavior(bottom_sheet_container)
        setBottomAppBar(view)
//        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))

        input_layout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://ru.wikipedia.org/wiki/${input_edit_text.text.toString()}")
            })
        }
    }

    private fun initViewModel() {
        viewModel.getPictureOfTheDay(app.retrofit).observe(viewLifecycleOwner) { renderData(it) }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> Toast.makeText(context, "Favourite",
                Toast.LENGTH_SHORT).show()
            R.id.app_bar_search -> Toast.makeText(context, "Search",
                Toast.LENGTH_SHORT).show()
            R.id.app_bar_settings -> Toast.makeText(context, "Settings",
                Toast.LENGTH_SHORT).show()
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(bottom_app_bar)
        setHasOptionsMenu(true)

        fab.setOnClickListener {
            if (isMain) {
                isMain = false
                bottom_app_bar.navigationIcon = null
                bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                fab.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_outline_arrow_back_24))
                bottom_app_bar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                bottom_app_bar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_baseline_menu)
                bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                fab.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_baseline_add_24))
                bottom_app_bar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
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
                //TODO Добавить экран загрузки
            }
            is PictureOfTheDayData.Error -> {
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