package com.example.nasapictureoftheday.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.nasapictureoftheday.MyApplication
import com.example.nasapictureoftheday.PictureOfTheDayData
import com.example.nasapictureoftheday.PictureOfTheDayViewModel
import com.example.nasapictureoftheday.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.main_fragment.*

class PictureOfTheDayFragment : Fragment() {
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
        viewModel = ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    private fun initViewModel(){
        viewModel.getPictureOfTheDay(app.retrofit).observe(viewLifecycleOwner) { renderData(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data){
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    Toast.makeText(context,"Нет ссылки на картинку!", Toast.LENGTH_SHORT).show()
                } else {
                    //Отобразите фото
                    //showSuccess()
                    //Coil в работе: достаточно вызвать у нашего ImageView
                    //нужную extension-функцию и передать ссылку и заглушки для placeholder
                    image_view.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                }
            }
            is PictureOfTheDayData.Loading ->{
                //TODO Добавить экран загрузки
            }
            is PictureOfTheDayData.Error->{
                view?.let { Snackbar.make(it, "Ошибка! ${data.error.message}", Snackbar.LENGTH_INDEFINITE).setAction("Перезагрузить"
                ) { initViewModel() }.show() }
            }
        }
    }
}