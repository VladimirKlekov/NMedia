package ru.netology.nmedia.activity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class NewPostFragment : Fragment() {
    //____________________________________________________________________________________________//
    //представляем ViewModel нескольким активити
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    //____________________________________________________________________________________________//

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentNewPostBinding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )
//________________________________________________________________________________________________//
        //Обращаемся к свойсту аргумента и читаем текст.
        arguments?.textArg?.let {
            binding.content.setText(it)
        }
//________________________________________________________________________________________________//

        binding.content.requestFocus()
//________________________________________________________________________________________________//
        //скрытие подсказок, если пост без текста
        if (arguments == null) {
            binding.supportGroup.isInvisible = true
        }
//________________________________________________________________________________________________//
        //закрытие (отмена) редактирования
        binding.closeEditidButton.setOnClickListener {
            //TODO
        }
//________________________________________________________________________________________________//
        //сохранение при нажатии на addPost
        binding.save.setOnClickListener {
            viewModel.editContent(binding.content.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()

//            val intent = Intent()
//            if (TextUtils.isEmpty(binding.content.text)) {
//                activity?.setResult(Activity.RESULT_CANCELED, intent)
//            } else {
//                val content: String = binding.content.text.toString()
//                intent.putExtra(Intent.EXTRA_TEXT, content)
//                activity?.setResult(Activity.RESULT_OK, intent)
//            }
//            findNavController().navigateUp()
        }
//________________________________________________________________________________________________//
        return binding.root
    }

    //________________________________________________________________________________________________//
        companion object {
        var Bundle.textArg: String? by StringArg
    }

    object StringArg : ReadWriteProperty<Bundle, String?> {
        override fun getValue(thisRef: Bundle, property: KProperty<*>): String? {
            return thisRef.getString(property.name)
        }

        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: String?) {
            thisRef.putString(property.name, value)
        }
    }
}


//
//binding.save.setOnClickListener {
//    if (binding.content.text.isNullOrBlank()) {
//        Toast.makeText(
//
//            this,
//            getString(R.string.empty_post_error),
//            Toast.LENGTH_SHORT
//        ).show()
//        setResult(AppCompatActivity.RESULT_CANCELED)
//
//    } else {
//        val result = Intent().putExtra(Intent.EXTRA_TEXT, binding.content.text.toString())
//        val text = binding.content.text.toString()
//        setResult(AppCompatActivity.RESULT_OK, result)
//        viewModel.editContent(text)
//        viewModel.save()
//        binding.content.clearFocus()
//        AndroidUtils.hideKeyboard(binding.content)
//        binding.content.setText("")
//    }
//    finish()
//}
//}
//}
//val text = binding.content.text.toString()